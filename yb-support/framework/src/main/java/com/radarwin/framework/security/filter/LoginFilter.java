package com.radarwin.framework.security.filter;

import com.radarwin.framework.security.AbstractAuthImageService;
import com.radarwin.framework.util.StringUtil;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by josh on 15/9/12.
 */
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private boolean postOnly = true;
    private AbstractAuthImageService authImageService;
    private String authCodeParameter = "authCode";

    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (this.postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        } else {
            String username = this.obtainUsername(request);
            String password = this.obtainPassword(request);
            if (username == null) {
                username = "";
            }

            if (password == null) {
                password = "";
            }

            if (StringUtil.isBlank(username) || StringUtil.isBlank(password)) {
                throw new BadCredentialsException("username or password should not empty");
            }

            if (authImageService != null) {
                if (authImageService.showAuthImage(request, username)) {
                    if (!authImageService.validateAuthCode(request, request.getParameter(authCodeParameter))) {
                        throw new BadCredentialsException("auth image code incorrect");
                    }
                }
            }

            username = username.trim();
            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
            this.setDetails(request, authRequest);
            return this.getAuthenticationManager().authenticate(authRequest);
        }
    }

    public AbstractAuthImageService getAuthImageService() {
        return authImageService;
    }

    public void setAuthImageService(AbstractAuthImageService authImageService) {
        this.authImageService = authImageService;
    }

    public String getAuthCodeParameter() {
        return authCodeParameter;
    }

    public void setAuthCodeParameter(String authCodeParameter) {
        this.authCodeParameter = authCodeParameter;
    }
}
