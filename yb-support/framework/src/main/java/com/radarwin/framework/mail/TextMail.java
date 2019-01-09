package com.radarwin.framework.mail;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by josh on 15/7/30.
 */
public final class TextMail extends AbstractMail {

    private static final Logger logger = LogManager.getLogger();

    public TextMail(String smtpHost, int smptPort) {
        super(smtpHost, smptPort);
    }

    public TextMail(String smtpHost, int smptPort, boolean validate) {
        super(smtpHost, smptPort, validate);
    }

    public TextMail(String smtpHost, int smptPort, boolean validate, boolean enableSSL) {
        super(smtpHost, smptPort, validate, enableSSL);
    }

    public TextMail(String smtpHost, int smptPort, boolean validate, String userName, String password) {
        super(smtpHost, smptPort, validate, userName, password);
    }

    public TextMail(String smtpHost, int smptPort, boolean validate, boolean enableSSL, String userName, String password) {
        super(smtpHost, smptPort, validate, enableSSL, userName, password);
    }
}
