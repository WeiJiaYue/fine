package com.radarwin.framework.sms;

import com.radarwin.framework.util.StringUtil;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Created by josh on 15/7/30.
 */

/**
 * 短信抽象类，各个短信供应商接口不同，需要各自实现
 */
public abstract class Sms {

    private static final Logger logger = LogManager.getLogger();

    protected static final int batchSize = 200; // 批量发送条数

    protected String userName; // 短信用户名
    protected String password; // 短信密码
    protected SmsInfo smsInfo; // 短信信息，包含手机号，短信内容
    protected List<SmsInfo> smsInfoList; // 不同短信内容发送不同手机需要设置此参数
    protected SmsCallBack smsCallBack; // 短信发送完毕的回调函数，会传入发送结果对象
    protected Map<String, Object> callBackParamMap; // 短信回调的自定义参数
    protected String smsUrl;
    protected boolean sendSync = false;

    public Sms(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public Sms(String userName, String password, String url) {
        this.userName = userName;
        this.password = password;
        this.smsUrl = url;
    }

    /**
     * 发送单条短信
     */
    protected abstract SmsResult sendSms(SmsInfo smsInfo);

    /**
     * 相同短信内容发送不同手机
     */
    protected abstract SmsResult sendSmsBatchMobile();

    /**
     * 不同短信内容发送不同手机
     */
    protected void sendSmsBatch() {
        List<SmsInfo> smsInfoList = getSmsInfoList();
        try {
            if (StringUtil.isBlank(userName)) {
                throw new RuntimeException("username is empty");
            } else if (StringUtil.isBlank(password)) {
                throw new RuntimeException("password is empty");
            } else if (smsInfoList == null || smsInfoList.size() == 0) {
                throw new RuntimeException("smsInfoList is empty");
            }
            for (SmsInfo smsInfo : smsInfoList) {
                try {
                    SmsResult smsResult = sendSms(smsInfo);
                    if (getSmsCallBack() != null) {
                        getSmsCallBack().call(smsResult, getCallBackParamMap());
                    }
                } catch (Exception e) {
                    logger.error(ExceptionUtils.getStackTrace(e));
                }
            }
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
    }

    protected String convertStreamToString(InputStream is) {
        StringBuilder sb1 = new StringBuilder();
        byte[] bytes = new byte[4096];
        int size = 0;

        try {
            while ((size = is.read(bytes)) > 0) {
                String str = new String(bytes, 0, size, "UTF-8");
                sb1.append(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb1.toString();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public SmsInfo getSmsInfo() {
        return smsInfo;
    }

    public void setSmsInfo(SmsInfo smsInfo) {
        this.smsInfo = smsInfo;
    }

    public List<SmsInfo> getSmsInfoList() {
        return smsInfoList;
    }

    public void setSmsInfoList(List<SmsInfo> smsInfoList) {
        this.smsInfoList = smsInfoList;
    }

    public SmsCallBack getSmsCallBack() {
        return smsCallBack;
    }

    public void registerCallBack(SmsCallBack smsCallBack) {
        this.smsCallBack = smsCallBack;
    }

    public Map<String, Object> getCallBackParamMap() {
        return callBackParamMap;
    }

    public void setCallBackParamMap(Map<String, Object> callBackParamMap) {
        this.callBackParamMap = callBackParamMap;
    }

    public String getSmsUrl() {
        return smsUrl;
    }

    public void setSmsUrl(String smsUrl) {
        this.smsUrl = smsUrl;
    }

    public boolean isSendSync() {
        return sendSync;
    }

    public void setSendSync(boolean sendSync) {
        this.sendSync = sendSync;
    }
}
