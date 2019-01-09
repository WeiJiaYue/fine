package com.radarwin.framework.sms;

import java.util.List;

/**
 * Created by josh on 15/7/30.
 */
public class SmsInfo {
    protected String sign;
    protected String mobile;
    protected String content;
    protected List<String> mobileList;

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
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

    public List<String> getMobileList() {
        return mobileList;
    }

    public void setMobileList(List<String> mobileList) {
        this.mobileList = mobileList;
    }
}
