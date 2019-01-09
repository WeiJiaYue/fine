package com.radarwin.framework.security.login;

import com.radarwin.framework.security.userdetails.UserInfo;

/**
 * Created by josh on 15/8/17.
 */
public interface LoginLogHandler {
    void saveLoginLog(String remoteIp, UserInfo userInfo);
}
