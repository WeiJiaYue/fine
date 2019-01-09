package com.radarwin.framework.security.handler;

import com.radarwin.framework.security.AbstractAuthImageService;
import com.radarwin.framework.util.SecurityUtil;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.util.Assert;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by josh on 15/9/11.
 */
public class AuthenticationFailureHandlerImpl implements AuthenticationFailureHandler {

    private String defaultFailureUrl;

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    private boolean forwardToDestination = false;

    private boolean allowSessionCreation = true;

    private String loginNameParameter = "username";

    private boolean showAuthImage = false;

    private AbstractAuthImageService authImageService;

    public AuthenticationFailureHandlerImpl() {
    }

    public AuthenticationFailureHandlerImpl(String defaultFailureUrl) {
        this.setDefaultFailureUrl(defaultFailureUrl);
    }

    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String loginName = request.getParameter(loginNameParameter);
        if (authImageService != null) {
            String authImageKey = authImageService.getAuthImageKey(request, loginName);
            String loginFailureCntKey = authImageService.getLoginFailureCntKey(request, loginName);
            authImageService.cacheLoginFailureCntAndCacheShowAuthImage(request, loginFailureCntKey, authImageKey);
        }

        if (this.defaultFailureUrl == null) {
            response.sendError(401, "Authentication Failed: " + exception.getMessage());
        } else {
            this.saveException(request, exception);
            boolean isAjaxRequest = SecurityUtil.isAjaxRequest(request);
            if (authImageService != null) {
                showAuthImage = authImageService.showAuthImage(request, loginName);
            }
            if (isAjaxRequest) {
                StringBuilder sb = new StringBuilder();
                sb.append("{");
                sb.append("\"success\":false");
                if (showAuthImage) {
                    sb.append(",\"validateAuthImage\":true");
                } else {
                    sb.append(",\"validateAuthImage\":false");
                }
                if ("auth image code incorrect".equals(exception.getMessage())) {
                    sb.append(",\"authCodeErr\":true");
                }
                sb.append("}");
                response.setContentType("application/json;charset=utf-8");
                PrintWriter printWriter = response.getWriter();
                printWriter.print(sb.toString());
                printWriter.flush();
                printWriter.close();
            } else {
                if (this.forwardToDestination) {
                    request.getRequestDispatcher(this.defaultFailureUrl).forward(request, response);
                } else {
                    this.redirectStrategy.sendRedirect(request, response, this.defaultFailureUrl);
                }
            }
        }
    }

    public void setDefaultFailureUrl(String defaultFailureUrl) {
        Assert.isTrue(UrlUtils.isValidRedirectUrl(defaultFailureUrl), "\'" + defaultFailureUrl + "\' is not a valid redirect URL");
        this.defaultFailureUrl = defaultFailureUrl;
    }

    protected final void saveException(HttpServletRequest request, AuthenticationException exception) {
        if (this.forwardToDestination) {
            request.setAttribute("SPRING_SECURITY_LAST_EXCEPTION", exception);
        } else {
            HttpSession session = request.getSession(false);
            if (session != null || this.allowSessionCreation) {
                request.getSession().setAttribute("SPRING_SECURITY_LAST_EXCEPTION", exception);
            }
        }

    }

    protected boolean isUseForward() {
        return this.forwardToDestination;
    }

    public void setUseForward(boolean forwardToDestination) {
        this.forwardToDestination = forwardToDestination;
    }

    public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
        this.redirectStrategy = redirectStrategy;
    }

    protected RedirectStrategy getRedirectStrategy() {
        return this.redirectStrategy;
    }

    protected boolean isAllowSessionCreation() {
        return this.allowSessionCreation;
    }

    public void setAllowSessionCreation(boolean allowSessionCreation) {
        this.allowSessionCreation = allowSessionCreation;
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
