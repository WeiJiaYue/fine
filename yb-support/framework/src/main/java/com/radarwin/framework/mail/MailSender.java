package com.radarwin.framework.mail;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by josh on 15/7/30.
 */
public class MailSender {
    private static final ExecutorService executorService = Executors.newFixedThreadPool(200);

    private static Logger logger = LogManager.getLogger(MailSender.class);

    /**
     * 发送邮件
     *
     * @param mail
     */
    public static void sendMail(AbstractMail mail) {
        if (mail.getMailInfo() != null) {
            if (mail.isSendSync()) {
                MailResult mailResult = mail.sendMail();
                if (mail.getEmailCallBack() != null) {
                    try {
                        mail.getEmailCallBack().call(mailResult, mail.getCallBackParamMap());
                    } catch (Exception e) {
                        logger.error(ExceptionUtils.getStackTrace(e));
                    }
                }
            } else {
                executorService.execute(new MailInvoke(mail));
            }
        }
    }

    /**
     * 批量发送邮件
     *
     * @param mail
     */
    public static void batchSendMail(AbstractMail mail) {
        List<MailInfo> mailInfoList = mail.getMailInfoList();

        if (mailInfoList != null && mailInfoList.size() > 0) {
            if (mailInfoList.size() < AbstractMail.batchCnt) {
                if (mail.isSendSync()) {
                    MailResult mailResult = mail.batchSendMail();
                    if (mail.getEmailCallBack() != null) {
                        try {
                            mail.getEmailCallBack().call(mailResult, mail.getCallBackParamMap());
                        } catch (Exception e) {
                            logger.error(ExceptionUtils.getStackTrace(e));
                        }
                    }
                } else {
                    executorService.submit(new MailInvoke(mail, true));
                }
            } else {
                List<MailInfo> list = new ArrayList<>();
                AbstractMail m = null;
                if (mail instanceof TextMail) {
                    m = new TextMail(mail.getSmtpHost(), mail.getSmptPort(),
                            mail.isValidate(), mail.getUserName(), mail.getPassword());
                } else {
                    m = new HtmlMail(mail.getSmtpHost(), mail.getSmptPort(),
                            mail.isValidate(), mail.getUserName(), mail.getPassword());
                }
                for (int i = 0; i < mailInfoList.size(); i++) {
                    list.add(mailInfoList.get(i));
                    if ((i + 1) % 100 == 0) {
                        m.setMailInfoList(list);
                        if (mail.isSendSync()) {
                            MailResult mailResult = mail.batchSendMail();
                            if (mail.getEmailCallBack() != null) {
                                try {
                                    mail.getEmailCallBack().call(mailResult, mail.getCallBackParamMap());
                                } catch (Exception e) {
                                    logger.error(ExceptionUtils.getStackTrace(e));
                                }
                            }
                        } else {
                            executorService.submit(new MailInvoke(mail, true));
                        }
                        if (mail instanceof TextMail) {
                            m = new TextMail(mail.getSmtpHost(), mail.getSmptPort(),
                                    mail.isValidate(), mail.getUserName(), mail.getPassword());
                        } else {
                            m = new HtmlMail(mail.getSmtpHost(), mail.getSmptPort(),
                                    mail.isValidate(), mail.getUserName(), mail.getPassword());
                        }
                        list = new ArrayList<>();
                    }
                }
                if (list.size() > 0) {
                    if (mail.isSendSync()) {
                        MailResult mailResult = mail.batchSendMail();
                        if (mail.getEmailCallBack() != null) {
                            try {
                                mail.getEmailCallBack().call(mailResult, mail.getCallBackParamMap());
                            } catch (Exception e) {
                                logger.error(ExceptionUtils.getStackTrace(e));
                            }
                        }
                    } else {
                        executorService.submit(new MailInvoke(mail, true));
                    }
                }
            }
        }
    }
}
