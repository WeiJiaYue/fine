package com.radarwin.framework.security;

import com.yunbao.framework.cache.redis.RedisCache;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

/**
 * Created by josh on 15/9/11.
 */
public class DefaultAuthImageServiceImpl extends AbstractAuthImageService {

    private static Logger logger = LogManager.getLogger(DefaultAuthImageServiceImpl.class);


    public DefaultAuthImageServiceImpl(RedisCache redisCache) {
        this.redisCache = redisCache;
    }

    /**
     * 判断是否显示验证码
     *
     * @param request
     * @param loginName
     * @return
     */
    @Override
    public boolean showAuthImage(HttpServletRequest request, String loginName) {
        String authImageKey = getAuthImageKey(request, loginName);
        return checkShowAuthImage(request, authImageKey);
    }

    /**
     * 生成验证码
     *
     * @param request
     * @param response
     */
    @Override
    public void generateAuthImage(HttpServletRequest request, HttpServletResponse response) {

        //设置页面不缓存
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        BufferedImage image = new BufferedImage(getImageWidth(), getImageHeight(), BufferedImage.TYPE_INT_RGB);
        //获取图形上下文
        Graphics2D g = image.createGraphics();

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        //生成随机类
        Random random = new Random();

        //设定背景色
        g.setColor(Color.WHITE);

        g.fillRect(0, 0, getImageWidth(), getImageHeight());

        //设定字体
        g.setFont(new Font("Times New Roman", Font.PLAIN, 18));

        //画边框
        Rectangle2D rectangle = new Rectangle2D.Float(0, 0, getImageWidth(), getImageHeight());

        g.draw(rectangle);

        //随机产生50条干扰线，使图象中的认证码不易被其它程序探测到
        for (int i = 0; i < 50; i++) {
            int x = random.nextInt(getImageWidth());
            int y = random.nextInt(getImageHeight());
            int xl = random.nextInt(12);
            int yl = random.nextInt(12);
            g.drawLine(x, y, x + xl, y + yl);
        }
        // 取随机产生的认证码(4位数字)
        String sRand = "";
        String s = "0123456789";
        for (int i = 0; i < 4; i++) {
            char rand = s.charAt(random.nextInt(s.length()));

            sRand += rand;

            // 将认证码显示到图象中
            g.setColor(Color.BLACK);

            AffineTransform affine = new AffineTransform();
            affine.setToRotation(Math.PI / 4 * random.nextDouble(), (getImageWidth() / 4) * i + 18 / 2, getImageHeight() / 2);
            g.setTransform(affine);
            g.drawChars(sRand.toCharArray(), i, 1, ((getImageWidth() - 10) / 4) * i + 5, getImageHeight() / 2 + 18 / 2);
        }

        cacheAuthImageCode(request, response, sRand);

        // 图象生效
        g.dispose();
        ServletOutputStream output = null;
        try {
            output = response.getOutputStream();
            // 输出图象到页面
            ImageIO.write(image, "JPEG", output);
        } catch (IOException e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        } finally {
            try {
                output.close();
            } catch (Exception e) {
                logger.error(ExceptionUtils.getStackTrace(e));
            }
        }
    }
}
