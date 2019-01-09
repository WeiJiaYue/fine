package com.radarwin.framework.security;

import com.radarwin.framework.security.userdetails.UserInfo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Created by josh on 15/9/9.
 */
public class DefaultCurrentUser extends AbstractCurrentUser {

    @Override
    public String getCurrentUserName() {
        UserInfo userInfo = getObject();
        if (userInfo != null) {
            return userInfo.getUsername();
        }
        return null;
    }

    @Override
    public UserInfo getObject() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return null;
        }
        Object principal = auth.getPrincipal();
        if (principal instanceof UserInfo) {
            return (UserInfo) principal;
        }
        return null;
    }
}
