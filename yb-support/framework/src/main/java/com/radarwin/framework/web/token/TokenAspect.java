package com.radarwin.framework.web.token;

import com.radarwin.framework.util.SecurityUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by josh on 15-6-10.
 */
@Aspect
@Component
public class TokenAspect {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;

    @Pointcut(value = "@annotation(com.radarwin.framework.web.token.Token)")
    public void tokenExpression() {
    }

    @Around(value = "tokenExpression() && @annotation(token)")
    public Object doAround(ProceedingJoinPoint pjd, Token token) {
        Object result = null;

        if (token.operate().equals(TokenOperate.VALIDATE)) {

            if (validateToken()) {
                try {
                    result = pjd.proceed();
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
                return result;
            }

        } else if (token.operate().equals(TokenOperate.GENERATE)) {

            try {
                result = pjd.proceed();
            } catch (Throwable e) {
                throw new RuntimeException(e);
            } finally {
                generateToken(result);
            }

        } else if (token.operate().equals(TokenOperate.BOTH)) {

            if (validateToken()) {
                try {
                    result = pjd.proceed();
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                } finally {
                    generateToken(result);
                }
                return result;
            }
        }

        return result;
    }

    /**
     * 验证 token
     *
     * @return
     */
    private boolean validateToken() {
        return WebToken.validateToken(request, response);
    }

    /**
     * controller 方法的参数要有HttpServletRequest
     * controller 方法返回的参数是Map<String,Object> 且不为空
     * 同时满足以上两者才会生成token
     *
     * @param obj
     */
    private void generateToken(Object obj) {

        // 是ajax请求并且不是map类型就抛出异常
        if (SecurityUtil.isAjaxRequest(request)) {
            // 为空，抛出异常
            if (obj == null) {
                throw new RuntimeException("generate token error value return by method is null");
            } else if (!(obj instanceof Map)) {
                throw new RuntimeException("generate token error method returned value is not Map<String,Object>");
            }
        }
        WebToken.generateToken(request, response, obj);
    }
}
