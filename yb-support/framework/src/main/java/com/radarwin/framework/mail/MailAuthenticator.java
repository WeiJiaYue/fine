package com.radarwin.framework.mail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * Created by josh on 15/7/30.
 */
class MailAuthenticator extends Authenticator {
    private String userName;
    private String password;

    MailAuthenticator(String username, String password) {
        this.userName = username;
        this.password = password;
    }

    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(userName, password);
    }
}
