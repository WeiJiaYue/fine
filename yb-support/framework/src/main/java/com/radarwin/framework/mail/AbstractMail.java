package com.radarwin.framework.mail;

import com.radarwin.framework.util.StringUtil;
import com.sun.mail.util.MailSSLSocketFactory;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.*;

/**
 * Created by josh on 15/7/30.
 */
public abstract class AbstractMail {

    private static Logger logger = LogManager.getLogger(AbstractMail.class);

    protected static final int batchCnt = 100;

    protected static final String ERR_MSG_EMPTY_FROM = "email from address is empty";

    protected static final String ERR_MSG_EMPTY_TO = "email receive address is empty";

    protected static final String ERR_MSG_EMPTY_SUBJECT = "email subject is empty";

    protected static final String ERR_MSG_EMPTY_CONTENT = "email content is empty";

    protected static final String ERR_MSG_EXCEED_MAX_BATCH_COUNT = "batch send email's count should less than 100";

    protected String smtpHost;
    protected int smptPort;
    protected boolean validate;
    protected boolean enableSSL;
    protected String userName;
    protected String password;

    protected EmailCallBack emailCallBack; // 邮件发送完毕的回调函数，会传入发送结果对象
    protected Map<String, Object> callBackParamMap; // 邮件回调的自定义参数
    protected boolean sendSync = false;

    public MailInfo mailInfo;
    public List<MailInfo> mailInfoList = new ArrayList<>();

    public AbstractMail(String smtpHost, int smptPort) {
        this.smtpHost = smtpHost;
        this.smptPort = smptPort;
    }

    public AbstractMail(String smtpHost, int smptPort, boolean validate) {
        this.smtpHost = smtpHost;
        this.smptPort = smptPort;
        this.validate = validate;
    }

    public AbstractMail(String smtpHost, int smptPort, boolean validate, boolean enableSSL) {
        this.smtpHost = smtpHost;
        this.smptPort = smptPort;
        this.validate = validate;
        this.enableSSL = enableSSL;
    }

    public AbstractMail(String smtpHost, int smptPort, boolean validate, String userName, String password) {
        this.smtpHost = smtpHost;
        this.smptPort = smptPort;
        this.validate = validate;
        this.userName = userName;
        this.password = password;
    }

    public AbstractMail(String smtpHost, int smptPort, boolean validate, boolean enableSSL, String userName, String password) {
        this.smtpHost = smtpHost;
        this.smptPort = smptPort;
        this.validate = validate;
        this.enableSSL = enableSSL;
        this.userName = userName;
        this.password = password;
    }

    protected MailResult sendMail() {
        MailResult mailResult = new MailResult();
        MailInfo mailInfo = getMailInfo();

        if (StringUtil.isBlank(mailInfo.getFrom())) {
            mailResult.setErrMsg(ERR_MSG_EMPTY_FROM);
            logger.error(ERR_MSG_EMPTY_FROM);
            return mailResult;
        }
        if (StringUtil.isBlank(mailInfo.getTo())) {
            mailResult.setErrMsg(ERR_MSG_EMPTY_TO);
            logger.error(ERR_MSG_EMPTY_TO);
            return mailResult;
        }
        if (StringUtil.isBlank(mailInfo.getSubject())) {
            mailResult.setErrMsg(ERR_MSG_EMPTY_SUBJECT);
            logger.error(ERR_MSG_EMPTY_SUBJECT);
            return mailResult;
        }
        if (StringUtil.isBlank(mailInfo.getContent())) {
            mailResult.setErrMsg(ERR_MSG_EMPTY_CONTENT);
            logger.error(ERR_MSG_EMPTY_CONTENT);
            return mailResult;
        }
        try {
            Properties properties = new Properties();
            properties.put("mail.smtp.host", getSmtpHost());
            properties.put("mail.smtp.port", getSmptPort());
            properties.put("mail.smtp.auth", isValidate() ? "true" : "false");

            if (this.isEnableSSL()) {
                MailSSLSocketFactory sf = new MailSSLSocketFactory();
                sf.setTrustAllHosts(true);
                properties.put("mail.smtp.ssl.enable", "true");
                properties.put("mail.smtp.ssl.socketFactory", sf);
            }

            MailAuthenticator mailAuthenticator = null;
            if (isValidate()) {
                mailAuthenticator = new MailAuthenticator(getUserName(), getPassword());
            }
            Session session = Session.getDefaultInstance(properties, mailAuthenticator);
            Message mailMessage = new MimeMessage(session);

            if (this instanceof TextMail) {
                wrapperTextMessage(mailMessage, mailInfo);
            } else {
                wrapperHtmlMessage(mailMessage, mailInfo);
            }
            Transport.send(mailMessage);
            mailResult.setSuccess(true);
            return mailResult;
        } catch (Exception e) {
            mailResult.setErrMsg(ExceptionUtils.getStackTrace(e));
            logger.error(mailResult.getErrMsg());
            return mailResult;
        }
    }


    protected MailResult batchSendMail() {
        MailResult mailResult = new MailResult();
        List<MailInfo> mailInfoList = getMailInfoList();
        if (mailInfoList.size() > batchCnt) {
            mailResult.setErrMsg(ERR_MSG_EXCEED_MAX_BATCH_COUNT);
            logger.error(ERR_MSG_EXCEED_MAX_BATCH_COUNT);
            return mailResult;
        }
        int cnt = 0;

        Properties properties = new Properties();
        properties.put("mail.smtp.host", getSmtpHost());
        properties.put("mail.smtp.port", getSmptPort());
        properties.put("mail.smtp.auth", isValidate() ? "true" : "false");

        try {
            if (this.isEnableSSL()) {
                MailSSLSocketFactory sf = new MailSSLSocketFactory();
                sf.setTrustAllHosts(true);
                properties.put("mail.smtp.ssl.enable", "true");
                properties.put("mail.smtp.ssl.socketFactory", sf);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        MailAuthenticator mailAuthenticator = null;
        if (isValidate()) {
            mailAuthenticator = new MailAuthenticator(getUserName(), getPassword());
        }
        Session session = Session.getDefaultInstance(properties, mailAuthenticator);
        for (MailInfo mailInfo : mailInfoList) {
            if (StringUtil.isBlank(mailInfo.getFrom())
                    || StringUtil.isBlank(mailInfo.getTo())
                    || StringUtil.isBlank(mailInfo.getSubject())
                    || StringUtil.isBlank(mailInfo.getContent())) {
                continue;
            }
            try {
                Message mailMessage = new MimeMessage(session);
                if (this instanceof TextMail) {
                    wrapperTextMessage(mailMessage, mailInfo);
                } else {
                    wrapperHtmlMessage(mailMessage, mailInfo);
                }
                Transport.send(mailMessage);
                cnt++;
            } catch (Exception e) {
                logger.error(ExceptionUtils.getStackTrace(e));
            }
        }
        if (cnt == mailInfoList.size()) {
            mailResult.setSuccess(true);
        } else {
            mailResult.setErrMsg("success:" + cnt + "   total:" + mailInfoList.size());
        }
        return mailResult;
    }

    public void registerEmailCallBack(EmailCallBack emailCallBack) {
        this.emailCallBack = emailCallBack;
    }

    public void registerEmailCallBack(EmailCallBack emailCallBack, Map<String, Object> callBackParamMap) {
        this.emailCallBack = emailCallBack;
        this.callBackParamMap = callBackParamMap;
    }

    private void wrapperTextMessage(Message mailMessage, MailInfo mailInfo) throws Exception {
        Address from = new InternetAddress(mailInfo.getFrom());
        mailMessage.setFrom(from);
        mailMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mailInfo.getTo()));
        if (StringUtil.isNotBlank(mailInfo.getCc())) {
            mailMessage.setRecipients(Message.RecipientType.CC, InternetAddress.parse(mailInfo.getCc()));
        }
        mailMessage.setSubject(mailInfo.getSubject());
        mailMessage.setSentDate(new Date());
        mailMessage.setText(mailInfo.getContent());
    }

    private void wrapperHtmlMessage(Message mailMessage, MailInfo mailInfo) throws Exception {
        Address from = new InternetAddress(mailInfo.getFrom());
        mailMessage.setFrom(from);
        mailMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mailInfo.getTo()));
        if (StringUtil.isNotBlank(mailInfo.getCc())) {
            mailMessage.setRecipients(Message.RecipientType.CC, InternetAddress.parse(mailInfo.getCc()));
        }
        mailMessage.setSubject(mailInfo.getSubject());
        mailMessage.setSentDate(new Date());
        Multipart mainPart = new MimeMultipart();
        BodyPart html = new MimeBodyPart();
        html.setContent(mailInfo.getContent(), "text/html; charset=utf-8");
        mainPart.addBodyPart(html);
        mailMessage.setContent(mainPart);
    }

    protected String getSmtpHost() {
        return smtpHost;
    }

    protected void setSmtpHost(String smtpHost) {
        this.smtpHost = smtpHost;
    }

    protected int getSmptPort() {
        return smptPort;
    }

    protected void setSmptPort(int smptPort) {
        this.smptPort = smptPort;
    }

    protected boolean isValidate() {
        return validate;
    }

    protected void setValidate(boolean validate) {
        this.validate = validate;
    }

    public boolean isEnableSSL() {
        return enableSSL;
    }

    public void setEnableSSL(boolean enableSSL) {
        this.enableSSL = enableSSL;
    }

    protected String getUserName() {
        return userName;
    }

    protected void setUserName(String userName) {
        this.userName = userName;
    }

    protected String getPassword() {
        return password;
    }

    protected void setPassword(String password) {
        this.password = password;
    }

    public MailInfo getMailInfo() {
        return mailInfo;
    }

    public void setMailInfo(MailInfo mailInfo) {
        this.mailInfo = mailInfo;
    }

    public List<MailInfo> getMailInfoList() {
        return mailInfoList;
    }

    public void setMailInfoList(List<MailInfo> mailInfoList) {
        this.mailInfoList = mailInfoList;
    }

    public boolean isSendSync() {
        return sendSync;
    }

    public void setSendSync(boolean sendSync) {
        this.sendSync = sendSync;
    }

    public EmailCallBack getEmailCallBack() {
        return emailCallBack;
    }

    public Map<String, Object> getCallBackParamMap() {
        return callBackParamMap;
    }
}
