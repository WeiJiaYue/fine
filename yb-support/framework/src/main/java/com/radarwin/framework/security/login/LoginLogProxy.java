package com.radarwin.framework.security.login;

import com.radarwin.framework.security.userdetails.UserInfo;

/**
 * Created by josh on 15/8/17.
 */
public class LoginLogProxy implements Runnable {

    private static LoginLogHandler loginLogHandler;

    private String remoteIp;

    private UserInfo userInfo;

    public LoginLogProxy(String remoteIp, UserInfo userInfo) {
        this.remoteIp = remoteIp;
        this.userInfo = userInfo;
    }

    public static void registerLoginLogHandler(LoginLogHandler loginLogHandlerRef) {
        loginLogHandler = loginLogHandlerRef;
    }

    @Override
    public void run() {
        loginLogHandler.saveLoginLog(remoteIp, userInfo);
    }
}
