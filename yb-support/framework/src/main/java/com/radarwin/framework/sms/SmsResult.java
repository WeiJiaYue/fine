package com.radarwin.framework.sms;

/**
 * Created by josh on 15/7/30.
 */
public class SmsResult {

    private boolean success = false; // 是否成功
    private String errorCode; // 错误编码
    private String errorMsg; // 错误信息
    private String smsId; // 短信唯一标识
    private String successCnt; // 成功数量
    private String errorCnt; // 失败数量
    private String blackCnt; // 黑名单数量
    private String mobile; // 发送手机号
    private String content; // 发送内容

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getSmsId() {
        return smsId;
    }

    public void setSmsId(String smsId) {
        this.smsId = smsId;
    }

    public String getSuccessCnt() {
        return successCnt;
    }

    public void setSuccessCnt(String successCnt) {
        this.successCnt = successCnt;
    }

    public String getErrorCnt() {
        return errorCnt;
    }

    public void setErrorCnt(String errorCnt) {
        this.errorCnt = errorCnt;
    }

    public String getBlackCnt() {
        return blackCnt;
    }

    public void setBlackCnt(String blackCnt) {
        this.blackCnt = blackCnt;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
