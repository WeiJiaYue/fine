package com.mandao.sdk.request;


import com.mandao.sdk.FuMinApi;
import com.mandao.sdk.RequiredParam;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * 富民银行请求参数
 */
public abstract class Request implements BaseRequest{


    @RequiredParam
    private String requestNo = getRequestNo();                  //请求流水号

    @RequiredParam
    private String userNo;                                      //平台用户编号

    @RequiredParam
    private String timestamp = getTimestamp();                  //请求时间戳当前时间yyyyMMddHHmmss

    @RequiredParam
    private String requestChannel = FuMinApi.REQUEST_CHANNEL;   //请求频道
    /**
     * 默认为直连接口
     */
    @RequiredParam
    private FuMinApi.ApiType apiType = FuMinApi.ApiType.SERVICE; //接口类型


    public String getRequestNo() {
        requestNo = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date().getTime()) + randomSuffix(4);
        return requestNo;
    }

    public void setRequestNo(String requestNo) {
        this.requestNo = requestNo;
    }

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public String getTimestamp() {
        timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getRequestChannel() {
        return requestChannel;
    }

    public void setRequestChannel(String requestChannel) {
        this.requestChannel = requestChannel;
    }


    public FuMinApi.ApiType getApiType() {
        return apiType;
    }

    public void setApiType(FuMinApi.ApiType apiType) {
        this.apiType = apiType;
    }

    public void validateRequiredParams(Class clz, List<String> error, String... excludeFields) {
        List<String> excludes = Arrays.asList(excludeFields);
        Field[] fields = clz.getDeclaredFields();
        for (Field f : fields) {
            boolean present = f.isAnnotationPresent(RequiredParam.class);
            if (present) {
                try {
                    if (!f.isAccessible()) {
                        f.setAccessible(true);
                    }
                    if(!excludes.isEmpty()){
                        if(excludes.contains(f.getName())){
                            continue;
                        }
                    }
                    Object val = f.get(this);
                    if (val == null) {
                        error.add(f.getName() + "属性值不能为空");
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        if (clz.getSuperclass() != Object.class) {
            validateRequiredParams(clz.getSuperclass(), error,excludeFields);
        }
        if (!error.isEmpty()) {
            throw new IllegalArgumentException(this.getClass().getSimpleName() + error);
        }
    }


    protected String randomSuffix(int n) {
        if (n < 1 || n > 10) {
            throw new IllegalArgumentException("cannot random " + n + " bit number");
        }
        Random ran = new Random();
        if (n == 1) {
            return String.valueOf(ran.nextInt(10));
        }
        int bitField = 0;
        char[] chs = new char[n];
        for (int i = 0; i < n; i++) {
            while (true) {
                int k = ran.nextInt(10);
                if ((bitField & (1 << k)) == 0) {
                    bitField |= 1 << k;
                    chs[i] = (char) (k + '0');
                    break;
                }
            }
        }
        return new String(chs);
    }


}
