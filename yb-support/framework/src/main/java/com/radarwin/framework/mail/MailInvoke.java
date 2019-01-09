package com.radarwin.framework.mail;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by josh on 15/7/30.
 */
public class MailInvoke implements Runnable {

    private static Logger logger = LogManager.getLogger(MailInvoke.class);

    private AbstractMail mail;

    private boolean batch = false;

    public MailInvoke(AbstractMail mail) {
        this.mail = mail;
    }

    public MailInvoke(AbstractMail mail, boolean batch) {
        this.mail = mail;
        this.batch = batch;
    }

    @Override
    public void run() {
        MailResult mailResult = null;
        if (this.batch) {
            mailResult = this.mail.batchSendMail();
        } else {
            mailResult = this.mail.sendMail();
        }
        if (this.mail.getEmailCallBack() != null) {
            try {
                this.mail.getEmailCallBack().call(mailResult, mail.getCallBackParamMap());
            } catch (Exception e) {
                logger.error(ExceptionUtils.getStackTrace(e));
            }
        }
    }
}
