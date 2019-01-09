package com.radarwin.framework.sms;

import java.util.Map;

/**
 * Created by josh on 15/7/30.
 */
public interface SmsCallBack {
    void call(SmsResult smsResult, Map<String, Object> callBackParamMap);
}
