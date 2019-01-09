package com.radarwin.framework.mail;

/**
 * Created by josh on 15/7/30.
 */
public class MailResult {
    private boolean success =false;
    private String errMsg;

    public MailResult() {

    }

    public MailResult(String errMsg) {
        this.errMsg = errMsg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
}
