package com.mandao.sdk;

import org.apache.commons.lang.exception.ExceptionUtils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ConfigReader {

    private static final String configPath = "/Users/lewis/GoWorkspace/yb-support/bao-fu/src/main/webapp/System_Config/app.properties";


    private static final String cerPath = "/Users/lewis/GoWorkspace/yb-support/bao-fu/src/main/webapp/CER/";


    private static final Map<String, String> configCache = new HashMap<>();


    public static final String PRIMARY_KEY_FILE;// 商户私钥文件名

    public static final String PUBLIC_KEY_FILE ;// 公钥文件名

    public static final String PRIMARY_KEY_PATH  ; //私钥绝对路径

    public static final String PUBLIC_KEY_PATH ;  //公钥绝对路径

    public static final String PRIMARY_KEY_PWD ;  //商户私钥密码

    public static final String API_SERVICE_URL  ;// 直连接口请求地址

    public static final String API_GATEWAY_URL  ;// 网关接口请求地址

    public static final String PLATFORM_NO ;// 平台编号

    public static final String CERT_SERIAL ;// 证书序号


    static {
        loadPropertyFile(configPath);
        checkCertificate();
        PRIMARY_KEY_FILE = configCache.get("pfx.name");
        PUBLIC_KEY_FILE = configCache.get("cer.name");
        PRIMARY_KEY_PATH = cerPath + PRIMARY_KEY_FILE;
        PUBLIC_KEY_PATH = cerPath + PUBLIC_KEY_FILE;
        PRIMARY_KEY_PWD = configCache.get("pfx.pwd");
        API_SERVICE_URL = configCache.get("request_service_url");
        API_GATEWAY_URL = configCache.get("request_gateway_url");
        PLATFORM_NO = configCache.get("platformNo");
        CERT_SERIAL = configCache.get("certSerial");
    }


    public static void loadPropertyFile(String file) {
        try {
            InputStream is = new FileInputStream(new File(file));
            Properties prop = new Properties();
            prop.load(is);
            for (Map.Entry<Object, Object> entry : prop.entrySet()) {
                configCache.put(entry.getKey().toString(), entry.getValue().toString());
            }
        } catch (Exception e) {
            throw new RuntimeException("配置文件加载错误" + ExceptionUtils.getStackTrace(e));
        }
    }


    public static void checkCertificate() {
        String pfxpath = cerPath + configCache.get("pfx.name");// 商户私钥
        String cerpath = cerPath + configCache.get("cer.name");// 公钥
        String pfxpwd = configCache.get("pfx.pwd");// 商户私钥证书密码

        /* 判定私钥、公钥文件是否存在 */
        File pfxfile = new File(pfxpath);
        File cerfile = new File(cerpath);
        if (!pfxfile.exists()) {
            throw new RuntimeException("私钥文件不存在！");
        }
        if (!cerfile.exists()) {
            throw new RuntimeException("公钥文件不存在！");
        }
    }

    public static Map<String, String> getConfigCache() {
        return configCache;
    }


}
