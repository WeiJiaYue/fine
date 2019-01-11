package com.mandao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by lewis on 2017/10/25.
 */
public class RestClient {


    private static Logger logger = LogManager.getLogger(RestClient.class);


    public static String doGet(String url) {
        return doGet(url,(String)null);
    }


    public static String doGet(String url,Map<String, Object> parameters) {
        return doGet(url,assembly(parameters));
    }

    public static String doGet(String url,String params) {
        return request(url,"GET",params);
    }


    public static String doPost(String url) {
        return doPost(url,null);
    }

    public static String doPost(String url,String params) {
        return request(url,"POST",params);
    }

    private static String request(String requestUrl, String requestMethod, String params) {
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        InputStream inputStream = null;
        try {

            URL url = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod(requestMethod);
            conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            if (null != params) {
                OutputStream outputStream = conn.getOutputStream();
                outputStream.write(params.getBytes("UTF-8"));
                outputStream.close();
            }
            // 从输入流读取返回内容
            inputStream = conn.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            bufferedReader = new BufferedReader(inputStreamReader);
            String str = null;
            StringBuilder buffer = new StringBuilder();
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            conn.disconnect();
            return buffer.toString();
        } catch (ConnectException ce) {
            logger.error("connect overtime：", ce);
        } catch (Exception e) {
            logger.error(e);
        } finally {
            // 释放资源
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (inputStreamReader != null) {

                    inputStreamReader.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    public static String assembly(Map<String, Object> parameters) {
        StringBuilder sb = new StringBuilder();
        Set es = parameters.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            Object v = entry.getValue();
            if (null != v && !"".equals(v)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.delete(sb.lastIndexOf("&"),sb.length());
        return sb.toString();
    }




}
