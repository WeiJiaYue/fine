package com.radarwin.framework.sms;

import com.radarwin.framework.util.StringUtil;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

/**
 * Created by josh on 15/7/30.
 */

/**
 * 基于创瑞短信供应商的实现
 */
public final class DefaultSms extends Sms {

    private static final String url = "http://web.cr6868.com/asmx/smsservice.aspx?";

    private static final Logger logger = LogManager.getLogger();

    public DefaultSms(String userName, String password) {
        super(userName, password);
    }

    /**
     * 发送单条短信
     */
    @Override
    protected SmsResult sendSms(SmsInfo smsInfo) {
        StringBuilder sb = new StringBuilder((StringUtil.isBlank(getSmsUrl()) ? url : getSmsUrl()));
        SmsResult smsResult = new SmsResult();
        String userName = getUserName();
        String password = getPassword();
        String mobile = smsInfo.getMobile();
        String content = smsInfo.getContent();

        try {
            if (StringUtil.isBlank(userName)) {
                throw new RuntimeException("username is empty");
            } else if (StringUtil.isBlank(password)) {
                throw new RuntimeException("password is empty");
            } else if (StringUtil.isBlank(mobile)) {
                throw new RuntimeException("mobile is empty");
            } else if (StringUtil.isBlank(content)) {
                throw new RuntimeException("content is empty");
            }

            sb.append("name=").append(getUserName());
            sb.append("&pwd=").append(getPassword());
            sb.append("&mobile=").append(smsInfo.getMobile());
            sb.append("&content=").append(URLEncoder.encode(content, "UTF-8"));
            sb.append("&stime=");
            sb.append("&sign=").append(URLEncoder.encode(smsInfo.getSign(), "UTF-8"));
            sb.append("&type=pt&extno=");

            doSend(sb.toString(), smsResult, smsInfo.getMobile(), smsInfo.getContent());
        } catch (Exception e) {
            smsResult.setErrorMsg(ExceptionUtils.getStackTrace(e));
            logger.error(smsResult.getErrorMsg());
        }
        return smsResult;
    }

    /**
     * 相同短信内容发送不同手机，每次批量200条
     */
    @Override
    protected SmsResult sendSmsBatchMobile() {
        StringBuilder sb = new StringBuilder((StringUtil.isBlank(getSmsUrl()) ? url : getSmsUrl()));
        SmsInfo smsInfo = getSmsInfo();
        SmsResult smsResult = new SmsResult();
        String userName = getUserName();
        String password = getPassword();
        List<String> mobileList = smsInfo.getMobileList();
        String content = smsInfo.getContent();

        try {
            if (StringUtil.isBlank(userName)) {
                throw new RuntimeException("username is empty");
            } else if (StringUtil.isBlank(password)) {
                throw new RuntimeException("password is empty");
            } else if (mobileList == null || mobileList.size() == 0) {
                throw new RuntimeException("mobileList is empty");
            } else if (mobileList.size() > batchSize) {
                throw new RuntimeException("mobile batch size should less then " + batchSize);
            } else if (StringUtil.isBlank(content)) {
                throw new RuntimeException("content is empty");
            }

            sb.append("name=").append(getUserName());
            sb.append("&pwd=").append(getPassword());
            sb.append("&mobile=");
            for (String mobile : mobileList) {
                sb.append(mobile).append(StringUtil.COMMA);
            }
            sb.deleteCharAt(sb.lastIndexOf(StringUtil.COMMA));
            sb.append("&content=").append(URLEncoder.encode(content, "UTF-8"));
            sb.append("&stime=");
            sb.append("&sign=").append(URLEncoder.encode(smsInfo.getSign(), "UTF-8"));
            sb.append("&type=pt&extno=");

            doSend(sb.toString(), smsResult, null, smsInfo.getContent());
        } catch (Exception e) {
            smsResult.setErrorMsg(ExceptionUtils.getStackTrace(e));
            logger.error(smsResult.getErrorMsg());
        }
        return smsResult;
    }

    private void doSend(String interfaceUrl, SmsResult smsResult, String mobile, String content) throws Exception {
        URL url = new URL(interfaceUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        InputStream is = url.openStream();

        // 返回格式 code,sendid,invalidcount,successcount,blackcount,msg
        String s = convertStreamToString(is);
        String[] ss = s.split(StringUtil.COMMA);

        smsResult.setErrorCode(ss[0]);

        if ("0".equals(smsResult.getErrorCode())) {
            smsResult.setSmsId(ss[1]);
            smsResult.setErrorCnt(ss[2]);
            smsResult.setSuccessCnt(ss[3]);
            smsResult.setBlackCnt(ss[4]);
            smsResult.setErrorMsg(ss[5]);
            smsResult.setMobile(mobile);
            smsResult.setContent(content);

            smsResult.setSuccess(true);
        } else {
            smsResult.setErrorMsg(ss[1] + StringUtil.COMMA + ss[2]);
        }
    }
}
