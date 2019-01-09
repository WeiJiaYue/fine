package com.radarwin.framework.mail;

/**
 * Created by josh on 15/7/30.
 */
public class MailInfo {

    private String from; // 邮件发送地址
    private String to; // 邮件接收地址, 多个地址用逗号分开
    private String cc; // 邮件抄送地址, 多个地址用逗号分开
    private String subject; // 邮件主题
    private String content; // 邮件内容

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
