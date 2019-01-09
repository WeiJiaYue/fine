/**
 * @author Administrator
 */
/**
 * @author Administrator
 *
 */
package com.mandao.http;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.mandao.sign.Verifier;

public class HttpUtil {

    public static String htmlForm(String url, Map<String, String> params) {
        if (params.isEmpty()) {
            return "参数不能为空！";
        }
        String formString = "<body onLoad=\"document.actform.submit()\" align=\"center\"><br><br><br><br><br><br>正在处理请稍候.....................<form  id=\"actform\" name=\"actform\" method=\"post\" action=\""
                + url + "\">";
        for (String key : params.keySet()) {
            formString += "<input name=\"" + key + "\" type=\"hidden\" value='" + params.get(key) + "'>";
        }
        formString += "</form></body>";
        return formString;
    }

    public static String requestForm(String url, Map<String, String> params, String cer) {
        if (params.isEmpty()) {
            return "参数不能为空！";
        }
        String postParms = "";
        for (String key : params.keySet()) {
            if (!key.isEmpty()) {
                postParms += key + "=" + params.get(key) + "&";
            }

        }
        postParms = postParms.substring(0, postParms.length() - 1);

        HttpSendModel httpSendModel = new HttpSendModel(url + "?" + postParms);
        System.out.println("【请求参数】：" + url + "?" + postParms);
        httpSendModel.setMethod(HttpMethod.POST);
        SimpleHttpResponse response = null;
        try {
            response = doRequest(httpSendModel, "utf-8", cer);
        } catch (Exception e) {
            return e.getMessage();
        }
        return response.getEntityString();

    }

    public static SimpleHttpResponse doRequest(HttpSendModel httpSendModel, String charSet, String cer)
            throws Exception {

        // 创建默认的httpClient客户端端
        SimpleHttpClient simpleHttpclient = new SimpleHttpClient();

        try {
            return doRequest(simpleHttpclient, httpSendModel, charSet, cer);
        } finally {
            simpleHttpclient.getHttpclient().getConnectionManager().shutdown();
        }

    }


    public static SimpleHttpResponse doRequest(SimpleHttpClient simpleHttpclient, HttpSendModel httpSendModel,
                                               String charSet, String cer) throws Exception {

        HttpRequestBase httpRequest = buildHttpRequest(httpSendModel);

        if (httpSendModel.getUrl().startsWith("https://")) {
            simpleHttpclient.enableSSL();
        }

        try {
            HttpResponse response = simpleHttpclient.getHttpclient().execute(httpRequest);
            int statusCode = response.getStatusLine().getStatusCode();
            if (isRequestSuccess(statusCode)) {
                // 验签操作
                String signature = response.getHeaders("signature")[0].getValue();

                System.out.println("=======返回报文签名signature：" + signature);
                String message = EntityUtils.toString(response.getEntity(), charSet);
                Boolean flag = Verifier.verifyByCerFile(message, signature, cer);
                if (flag) {
                    return new SimpleHttpResponse(statusCode, message, null);
                } else {
                    System.out.println("验签失败！");
                    return new SimpleHttpResponse(statusCode, "验签失败", null);
                }

            } else {
                return new SimpleHttpResponse(statusCode, null, response.getStatusLine().getReasonPhrase());
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("http请求异常", e);
        }

    }

    /**
     * @param httpSendModel
     * @return
     * @throws Exception
     */
    protected static HttpRequestBase buildHttpRequest(HttpSendModel httpSendModel) throws Exception {
        HttpRequestBase httpRequest;
        if (httpSendModel.getMethod() == null) {
            throw new Exception("请求方式未设定");
        } else if (httpSendModel.getMethod() == HttpMethod.POST) {

            String url = httpSendModel.getUrl();
            String sendCharSet = httpSendModel.getCharSet();
            List<HttpFormParameter> params = httpSendModel.getParams();

            List<NameValuePair> qparams = new ArrayList<NameValuePair>();
            if (params != null && params.size() != 0) {

                for (HttpFormParameter param : params) {
                    qparams.add(new BasicNameValuePair(param.getName(), param.getValue()));
                }

            }

            HttpPost httppost = new HttpPost(url);
            try {
                httppost.setEntity(new UrlEncodedFormEntity(qparams, sendCharSet));
            } catch (UnsupportedEncodingException e) {
                throw new Exception("构建post请求参数失败", e);
            }

            httpRequest = httppost;
        } else if (httpSendModel.getMethod() == HttpMethod.GET) {
            HttpGet httpget = new HttpGet(httpSendModel.buildGetRequestUrl());

            httpRequest = httpget;
        } else {
            throw new Exception("请求方式不支持：" + httpSendModel.getMethod());
        }

        return httpRequest;
    }

    /**
     * 请求是否成功
     *
     * @param statusCode
     * @return
     */
    public static boolean isRequestSuccess(int statusCode) {
        return statusCode == 200;
    }


}