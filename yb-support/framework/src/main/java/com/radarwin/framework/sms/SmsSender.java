package com.radarwin.framework.sms;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by josh on 15/7/30.
 */
public class SmsSender {
    private static final ExecutorService executorService = Executors.newFixedThreadPool(200);

    /**
     * 发送单条短信
     * 需要设置Sms中的smsInfo
     *
     * @param sms
     */
    public static void sendSms(Sms sms) {
        if (sms.isSendSync()) {
            SmsCallBack smsCallBack = sms.getSmsCallBack();
            SmsResult smsResult = sms.sendSms(sms.getSmsInfo());
            if (smsCallBack != null) {
                smsCallBack.call(smsResult, sms.getCallBackParamMap());
            }
        } else {
            SmsInfo smsInfo = sms.getSmsInfo();
            if (smsInfo != null) {
                executorService.submit(new SmsInvoke(sms, SmsInvoke.TYPE_SINGLE));
            }
        }
    }

    /**
     * 相同短信内容发送不同手机，每次批量200条
     * 需要设置Sms中的smsInfo的mobileList
     *
     * @param sms
     */
    public static void sendSmsBatchMobile(Sms sms) {
        if (sms.isSendSync()) {
            SmsCallBack smsCallBack = sms.getSmsCallBack();
            SmsResult smsResult = sms.sendSmsBatchMobile();
            if (smsCallBack != null) {
                smsCallBack.call(smsResult, sms.getCallBackParamMap());
            }
        } else {
            SmsInfo smsInfo = sms.getSmsInfo();
            if (smsInfo != null && smsInfo.getMobileList() != null && smsInfo.getMobileList().size() > 0) {
                executorService.submit(new SmsInvoke(sms, SmsInvoke.TYPE_MULTIMOBILE));
            }
        }
    }

    /**
     * 不同短信内容发送不同手机,每次批量200条
     * 需要设置Sms中的smsInfoList
     *
     * @param sms
     */
    public static void sendSmsBatch(Sms sms) {
        if (sms.isSendSync()) {
            sms.sendSmsBatch();
        } else {
            List<SmsInfo> smsInfoList = sms.getSmsInfoList();
            if (smsInfoList != null && smsInfoList.size() > 0) {
                executorService.submit(new SmsInvoke(sms, SmsInvoke.TYPE_BATCH));
            }
        }
    }
}
