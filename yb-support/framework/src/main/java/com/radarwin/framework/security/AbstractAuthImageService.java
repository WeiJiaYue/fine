package com.radarwin.framework.security;

import com.radarwin.framework.util.AlgorithmUtil;
import com.radarwin.framework.util.IpAddressUtil;
import com.radarwin.framework.util.StringUtil;
import com.yunbao.framework.cache.redis.RedisCache;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.UUID;

/**
 * Created by josh on 15/9/11.
 */
public abstract class AbstractAuthImageService implements AuthImageService {

    private static Logger logger = LogManager.getLogger(AbstractAuthImageService.class);

    // 可以在配置文件指定，最好按照应用分类来制定
    protected String authImageKeyPrefix = "validateCode";

    // 可以在配置文件指定
    protected String loginFailureCntKeyPrefix = "loginFailureCnt";

    // 图片验证码过期时间,默认5分钟,5分钟后过期
    public int imageCodeTimeOut = 5 * 60; // 秒

    // 登陆显示验证码的过期时间,默认24小时,24小时后不显示验证码
    public int showAuthImageTimeOut = 24 * 60 * 60; // 秒

    // 缓存连续登陆失败次数的时间,默认24小时,24小时后过期
    public int loginFailureCntDuration = 24 * 60 * 60; // 秒

    // 连续失败多少次显示图片验证码,默认5次
    public int failureCntShowImage = 5;

    protected String authCodeKeyCookieName = "_auc";

    protected int imageWidth = 60;

    protected int imageHeight = 20;

    protected RedisCache redisCache;

    public void setRedisCache(RedisCache redisCache) {
        this.redisCache = redisCache;
    }

    /**
     * 生成显示图片验证码的key
     *
     * @param request
     * @param loginName
     * @return
     */
    public String getAuthImageKey(HttpServletRequest request, String loginName) {
        String remoteIp = IpAddressUtil.getRemoteIp(request);
        String userAgent = request.getHeader("User-Agent");

        if (StringUtil.isNotBlank(remoteIp) && StringUtil.isNotBlank(userAgent)) {
            return AlgorithmUtil.md5(getAuthImageKeyPrefix() + remoteIp + userAgent);
        }
        if (StringUtil.isNotBlank(loginName)) {
            return AlgorithmUtil.md5(getAuthImageKeyPrefix() + loginName);
        }
        return null;
    }

    /**
     * 从cookie解析验证码的key
     *
     * @param request
     * @return
     */
    public String getAuthCodeKey(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (authCodeKeyCookieName.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    /**
     * 生成登陆失败次数的key
     *
     * @param request
     * @param loginName
     * @return
     */
    public String getLoginFailureCntKey(HttpServletRequest request, String loginName) {
        String authImageKey = getAuthImageKey(request, loginName);
        if (StringUtil.isBlank(authImageKey)) {
            return null;
        }
        return getLoginFailureCntKeyPrefix() + StringUtil.COLON + authImageKey;
    }

    /**
     * 缓存图片验证码
     *
     * @param request
     * @param response
     * @param authCode
     */
    public void cacheAuthImageCode(HttpServletRequest request, HttpServletResponse response, String authCode) {
        if (StringUtil.isBlank(authCode)) {
            return;
        }
        try {
            String authCodeKey = UUID.randomUUID().toString();

            if (redisCache != null) {
                redisCache.addCacheWithExpire(authCodeKey, authCode, getImageCodeTimeOut());
            } else {
                HttpSession session = request.getSession(true);
                session.setAttribute(authCodeKey, authCode);
                session.setMaxInactiveInterval(getImageCodeTimeOut());
            }
            Cookie cookie = new Cookie(authCodeKeyCookieName, authCodeKey);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(getImageCodeTimeOut());
            response.addCookie(cookie);
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
    }

    /**
     * 缓存登陆失败次数
     *
     * @param loginFailureCntKey
     */
    public void cacheLoginFailureCnt(HttpServletRequest request, String loginFailureCntKey) {
        if (StringUtil.isBlank(loginFailureCntKey)) {
            return;
        }
        try {
            if (redisCache != null) {
                String failureCnt = redisCache.getCache(loginFailureCntKey);
                if (StringUtil.isNotBlank(failureCnt)) {
                    redisCache.addCacheWithExpire(loginFailureCntKey, String.valueOf(Integer.valueOf(failureCnt) + 1), getLoginFailureCntDuration());
                } else {
                    redisCache.addCacheWithExpire(loginFailureCntKey, "1", getLoginFailureCntDuration());
                }
            } else {
                HttpSession session = request.getSession(true);
                Object failureCnt = session.getAttribute(loginFailureCntKey);
                if (failureCnt != null) {
                    session.setAttribute(loginFailureCntKey, ((Integer) failureCnt + 1));
                } else {
                    session.setAttribute(loginFailureCntKey, 0);
                }
                session.setMaxInactiveInterval(getLoginFailureCntDuration());
            }
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
    }

    /**
     * 缓存登陆失败次数
     *
     * @param loginFailureCntKey
     */
    public void cacheLoginFailureCntAndCacheShowAuthImage(HttpServletRequest request, String loginFailureCntKey, String authImageKey) {
        if (StringUtil.isBlank(loginFailureCntKey) || StringUtil.isBlank(authImageKey)) {
            return;
        }
        cacheLoginFailureCnt(request, loginFailureCntKey);

        if (exceedFailureCnt(request, loginFailureCntKey)) {
            cacheShowAuthImage(request, authImageKey);
        }
    }

    /**
     * 缓存是否显示图片验证码
     *
     * @param request
     * @param authImageKey
     */
    public void cacheShowAuthImage(HttpServletRequest request, String authImageKey) {
        if (StringUtil.isBlank(authImageKey)) {
            return;
        }
        try {
            if (redisCache != null) {
                redisCache.addCacheWithExpire(authImageKey, "show", getShowAuthImageTimeOut());
            } else {
                HttpSession session = request.getSession(true);
                session.setAttribute(authImageKey, "show");
                session.setMaxInactiveInterval(getShowAuthImageTimeOut());
            }
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
    }

    /**
     * 移除登陆失败次数的缓存
     *
     * @param request
     * @param loginFailureCntKey
     */
    public void removeLoginFailureCntCache(HttpServletRequest request, String loginFailureCntKey) {
        if (StringUtil.isBlank(loginFailureCntKey)) {
            return;
        }
        try {
            if (redisCache != null) {
                redisCache.delCache(loginFailureCntKey);
            } else {
                HttpSession session = request.getSession(true);
                session.removeAttribute(loginFailureCntKey);
            }
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
    }

    /**
     * 获取登陆失败次数
     *
     * @param loginFailureCntKey
     * @return
     */
    public int getLoginFailureCnt(HttpServletRequest request, String loginFailureCntKey) {
        if (StringUtil.isBlank(loginFailureCntKey)) {
            return 0;
        }
        try {
            Object failCnt = null;
            if (redisCache != null) {
                failCnt = redisCache.getCache(loginFailureCntKey);
            } else {
                HttpSession session = request.getSession(true);
                failCnt = session.getAttribute(loginFailureCntKey);
            }
            if (failCnt == null) {
                return 0;
            }
            return Integer.valueOf(String.valueOf(failCnt));
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            return 0;
        }
    }

    /**
     * 验证是否超过显示验证码的登陆失败次数
     *
     * @return
     */
    public boolean exceedFailureCnt(HttpServletRequest request, String loginFailureCntKey) {
        if (StringUtil.isBlank(loginFailureCntKey)) {
            return false;
        }
        try {
            int failCnt = getLoginFailureCnt(request, loginFailureCntKey);
            return failCnt > getFailureCntShowImage();
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            return false;
        }
    }

    /**
     * 判断是否显示图片验证码
     *
     * @return
     */
    public boolean checkShowAuthImage(HttpServletRequest request, String authImageKey) {
        if (StringUtil.isBlank(authImageKey)) {
            return false;
        }
        try {
            Object v = null;
            if (redisCache != null) {
                v = redisCache.getCache(authImageKey);
            } else {
                HttpSession session = request.getSession(true);
                v = session.getAttribute(authImageKey);
            }
            return v != null && StringUtil.isNotBlank(v.toString());
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            return false;
        }
    }

    /**
     * 验证图片验证码是否正确
     *
     * @param request
     * @param authCode
     * @return
     */
    public boolean validateAuthCode(HttpServletRequest request, String authCode) {
        Object v = null;
        String authCodeKey = getAuthCodeKey(request);
        if (StringUtil.isBlank(authCodeKey) || StringUtil.isBlank(authCode)) {
            return false;
        }
        try {
            if (redisCache != null) {
                v = redisCache.getCache(authCodeKey);
            } else {
                v = request.getSession(true).getAttribute(authCodeKey);
            }
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }

        try {
            if (redisCache != null) {
                redisCache.delCache(authCodeKey);
            } else {
                request.getSession(true).removeAttribute(authCodeKey);
            }
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }

        return (v != null && String.valueOf(v).equalsIgnoreCase(authCode));
    }

    public String getAuthImageKeyPrefix() {
        return authImageKeyPrefix;
    }

    public void setAuthImageKeyPrefix(String authImageKeyPrefix) {
        this.authImageKeyPrefix = authImageKeyPrefix;
    }

    public int getImageCodeTimeOut() {
        return imageCodeTimeOut;
    }

    public void setImageCodeTimeOut(int imageCodeTimeOut) {
        this.imageCodeTimeOut = imageCodeTimeOut;
    }

    public int getShowAuthImageTimeOut() {
        return showAuthImageTimeOut;
    }

    public void setShowAuthImageTimeOut(int showAuthImageTimeOut) {
        this.showAuthImageTimeOut = showAuthImageTimeOut;
    }

    public int getFailureCntShowImage() {
        return failureCntShowImage;
    }

    public void setFailureCntShowImage(int failureCntShowImage) {
        this.failureCntShowImage = failureCntShowImage;
    }

    public int getLoginFailureCntDuration() {
        return loginFailureCntDuration;
    }

    public void setLoginFailureCntDuration(int loginFailureCntDuration) {
        this.loginFailureCntDuration = loginFailureCntDuration;
    }

    public String getLoginFailureCntKeyPrefix() {
        return loginFailureCntKeyPrefix;
    }

    public void setLoginFailureCntKeyPrefix(String loginFailureCntKeyPrefix) {
        this.loginFailureCntKeyPrefix = loginFailureCntKeyPrefix;
    }

    public String getAuthCodeKeyCookieName() {
        return authCodeKeyCookieName;
    }

    public void setAuthCodeKeyCookieName(String authCodeKeyCookieName) {
        this.authCodeKeyCookieName = authCodeKeyCookieName;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }
}
