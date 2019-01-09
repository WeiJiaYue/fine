package com.radarwin.framework.util;

import com.radarwin.framework.security.resource.MyInvocationSecurityMetadataSourceService;
import com.radarwin.framework.security.userdetails.UserInfo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

public class SecurityUtil {

    public static final int ACCESS_DENIED_STATUS = 600;
    public static final int SESSION_TIME_OUT_STATUS = 610;
    public static final int SESSION_EXPIRED_STATUS = 620;

    public static final String ACCESS_DENIED_MSG = "ACCESS_DENIED";
    public static final String SESSION_TIME_OUT_MSG = "TIMEOUT";
    public static final String SESSION_EXPIRED_MSG = "EXPIRED";

    public static boolean hasRole(String roleCodes) {
        boolean bol = false;
        if (StringUtil.isNotEmpty(roleCodes)) {
            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Collection<GrantedAuthority> grantedAuthorities = userInfo.getAuthorities();
            String[] roleCodeArray = roleCodes.split(",");
            for (String roleCode : roleCodeArray) {
                for (GrantedAuthority grantedAuthority : grantedAuthorities) {
                    if (grantedAuthority.getAuthority().equals(roleCode)) {
                        bol = true;
                        break;
                    }
                }
            }
        }
        return bol;
    }

    public static String getGrantedAuthority() {
        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Collection<GrantedAuthority> grantedAuthorities = userInfo.getAuthorities();
        StringBuffer stringBuffer = new StringBuffer();
        for (GrantedAuthority grantedAuthority : grantedAuthorities) {
            stringBuffer.append(grantedAuthority.getAuthority()).append(",");
        }
        String roles = stringBuffer.toString().substring(0, stringBuffer.toString().lastIndexOf(","));
        return roles;
    }

    public static boolean isAjaxRequest(HttpServletRequest request) {
        String accept = request.getHeader("Accept");
        String xmlHttpRequest = request.getHeader("X-Requested-With");

        if ((StringUtil.isNotEmpty(accept) && accept.indexOf("application/json") != -1)
                || (StringUtil.isNotEmpty(xmlHttpRequest) && xmlHttpRequest.indexOf("XMLHttpRequest") != -1)) {
            return true;
        }
        return false;
    }

    public static void refreshResourceRole() {
        MyInvocationSecurityMetadataSourceService myInvocationSecurityMetadataSourceService = ContextUtil.getBean("mySecurityMetadataSource");
        myInvocationSecurityMetadataSourceService.refresh();
    }
}
