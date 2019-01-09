package com.radarwin.framework.web.token;

import com.radarwin.framework.util.SecurityUtil;
import com.radarwin.framework.web.response.ResponseWrapper;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.UUID;

/**
 * Created by josh on 15-6-8.
 */

/**
 * 此token只针对重复提交
 */
public class WebToken {
    public static final String SESSION_TOKEN_NAME = "_st";

    public static final String COOKIE_TOKEN_NAME = "_ct";

    public static final String REQUEST_TOKEN_NAME = "_rt";

    private static int cookieTokenExpire = 60 * 60;


    /**
     * 原则上不是所有请求都要进行 token 验证
     * 使用了ValidateToken的注解 才会对请求进行拦截并验证token
     * <p/>
     * 验证成功之后,将session中的token设置为其他任意的值，
     * 此时用户cookie的token与session的token不一致，下次验证会失败，
     * 直到上次验证通过的请求将session的token清空，
     * 此时会重新生成token（session与cookie的token值一致）
     * <p/>
     * 分布式情况下可以将token放进分布式缓存中,然后进行判断
     *
     * @param request
     * @return
     */
    public static boolean validateToken(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();

        if (session == null) {
            return false;
        }

        // 获取session的token
        //Object sessionToken = session.getAttribute(SESSION_TOKEN_NAME);

        Object cookieToken = getCookieToken(request);

        // 获取request的token （前端需要从cookie中获取token）
        String requestToken = getRequestToken(request);

        if (requestToken == null || cookieToken == null) {
            return false;
        }

        /**
         * 验证session的token和request的token是否一样
         */
        boolean valid = requestToken.equals(cookieToken);

        // 验证通过将session 中 token 的值设为空
        // 表示当前token已经失效
        if (valid) {
            removeCookieToken(response);
            //removeSessionToken(session);
        }

        return valid;
    }

    /**
     * 获取用户 cookie 的token
     *
     * @param request
     * @return
     */
    protected static String getCookieToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length == 0) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (COOKIE_TOKEN_NAME.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    /**
     * 从request 获取token
     *
     * @param request
     * @return
     */
    protected static String getRequestToken(HttpServletRequest request) {
        return request.getParameter(REQUEST_TOKEN_NAME);
    }

    // 当session中的token为空或不存在token时 设置token
    // 加密是否会影响性能
    protected static void generateToken(HttpServletRequest request, HttpServletResponse response, Object object) {
        HttpSession session = request.getSession();
        if (session == null) {
            return;
        }
        String token = UUID.randomUUID().toString();

        // 设置session的token
        //generateSessionToken(session, token);

        generateCookieToken(response, token);

        // 异步请求把token写入controller返回值,controller返回值一般为Map<String,Object> 类型
        if (SecurityUtil.isAjaxRequest(request)) {

            ResponseWrapper responseWrapper = (ResponseWrapper) object;

            // 设置返回的token
            responseWrapper.addAttribute(REQUEST_TOKEN_NAME, token);
        } else {
            request.setAttribute(REQUEST_TOKEN_NAME, token);
        }
    }

    /**
     * 移除 token ，把token设置为null
     *
     * @param session
     */
    public static void removeSessionToken(HttpSession session) {
        if (session == null || session.getAttribute(SESSION_TOKEN_NAME) == null) {
            return;
        }
        session.setAttribute(SESSION_TOKEN_NAME, null);
    }

    /**
     * 移除 cookie 的token
     *
     * @param response
     */
    public static void removeCookieToken(HttpServletResponse response) {
        Cookie cookie = new Cookie(COOKIE_TOKEN_NAME, null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }


    /**
     * 把token写入session
     *
     * @param session
     * @param token
     */
    private static void generateSessionToken(HttpSession session, String token) {
        session.setAttribute(SESSION_TOKEN_NAME, token);
    }

    /**
     * 生成 cookie token
     *
     * @param response
     * @param token
     */
    private static void generateCookieToken(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie(COOKIE_TOKEN_NAME, token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(cookieTokenExpire);
        response.addCookie(cookie);
    }
}
