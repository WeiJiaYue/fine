package com.radarwin.framework.mail;

import java.util.Map;

/**
 * Created by josh on 15/9/16.
 */
public interface EmailCallBack {
    void call(MailResult mailResult, Map<String, Object> callBackParamMap);
}
