package com.radarwin.framework.security.handler;

import com.radarwin.framework.security.AbstractAuthImageService;
import com.radarwin.framework.security.login.LoginLogProxy;
import com.radarwin.framework.security.userdetails.UserInfo;
import com.radarwin.framework.util.IpAddressUtil;
import com.radarwin.framework.util.SecurityUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AbstractAuthenticationTargetUrlRequestHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Josh on 14-4-16.
 */
public class AuthenticationSuccessHandlerImpl extends AbstractAuthenticationTargetUrlRequestHandler implements AuthenticationSuccessHandler {

    private boolean enableLoginLog = false;

    private static final ExecutorService executorService = Executors.newFixedThreadPool(200);

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    private AbstractAuthImageService authImageService;

    private String loginNameParameter = "username";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {
        if (enableLoginLog) {
            executorService.submit(new LoginLogProxy(IpAddressUtil.getRemoteIp(request), (UserInfo) authentication.getPrincipal()));
        }
        if (authImageService != null) {
            String loginFailureCntCacheKey = authImageService.getLoginFailureCntKey(request, request.getParameter(loginNameParameter));
            authImageService.removeLoginFailureCntCache(request, loginFailureCntCacheKey);
        }
        this.handle(request, response, authentication);
        this.clearAuthenticationAttributes(request);
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        String targetUrl = this.determineTargetUrl(request, response);
        if (response.isCommitted()) {
            this.logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
        } else {
            boolean isAjaxRequest = SecurityUtil.isAjaxRequest(request);

            if (isAjaxRequest) {
                response.setContentType("application/json; charset=utf-8");
                PrintWriter printWriter = response.getWriter();
                printWriter.print("{\"targetUrl\":\"" + targetUrl + "\",\"success\":true}");
                printWriter.flush();
                printWriter.close();
            } else {
                this.redirectStrategy.sendRedirect(request, response, targetUrl);
            }
        }
    }

    private void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute("SPRING_SECURITY_LAST_EXCEPTION");
        }
    }

    public void setEnableLoginLog(boolean enableLoginLog) {
        this.enableLoginLog = enableLoginLog;
    }

    public String getLoginNameParameter() {
        return loginNameParameter;
    }

    public void setLoginNameParameter(String loginNameParameter) {
        this.loginNameParameter = loginNameParameter;
    }

    public AbstractAuthImageService getAuthImageService() {
        return authImageService;
    }

    public void setAuthImageService(AbstractAuthImageService authImageService) {
        this.authImageService = authImageService;
    }
}
