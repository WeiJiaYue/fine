package com.radarwin.framework.sms;

/**
 * Created by josh on 15/7/30.
 */
public class SmsInvoke implements Runnable {

    protected static final String TYPE_SINGLE = "single";
    protected static final String TYPE_MULTIMOBILE = "multiMobile";
    protected static final String TYPE_BATCH = "batch";

    private Sms sms;
    private String type;

    public SmsInvoke(Sms sms, String type) {
        this.sms = sms;
        this.type = type;
    }

    @Override
    public void run() {
        SmsCallBack smsCallBack = sms.getSmsCallBack();
        SmsResult smsResult = null;
        switch (this.type) {
            case TYPE_SINGLE:
                smsResult = sms.sendSms(sms.getSmsInfo());
                if (smsCallBack != null) {
                    smsCallBack.call(smsResult,sms.getCallBackParamMap());
                }
                break;
            case TYPE_MULTIMOBILE:
                smsResult = sms.sendSmsBatchMobile();
                if (smsCallBack != null) {
                    smsCallBack.call(smsResult,sms.getCallBackParamMap());
                }
                break;
            case TYPE_BATCH:
                sms.sendSmsBatch();
                break;
            default:
        }
    }
}
