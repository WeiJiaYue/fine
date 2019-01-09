package com.radarwin.framework.security.handler;

import com.radarwin.framework.util.SecurityUtil;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Josh on 14-4-9.
 */
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {

    private String accessDeniedPage;

    public String getAccessDeniedPage() {
        return accessDeniedPage;
    }

    public void setAccessDeniedPage(String accessDeniedPage) {
        this.accessDeniedPage = accessDeniedPage;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {

        boolean isAjaxRequest = SecurityUtil.isAjaxRequest(request);

        if (isAjaxRequest) {
            // angular-js 在status 为 200-299 会进入success回调函数，否则进入error毁掉函数
            //response.setStatus(SecurityUtil.ACCESS_DENIED_STATUS);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            PrintWriter printWriter = response.getWriter();
            //printWriter.print(SecurityUtil.ACCESS_DENIED_MSG);
            printWriter.print("{\"accessDenied\":true}");
            printWriter.flush();
            printWriter.close();
        } else {
            String path = request.getContextPath();
            String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path;
            if (accessDeniedPage.indexOf("/") == 0) {
                response.sendRedirect(basePath + accessDeniedPage);
            } else {
                response.sendRedirect(basePath + "/" + accessDeniedPage);
            }
        }
    }
}
