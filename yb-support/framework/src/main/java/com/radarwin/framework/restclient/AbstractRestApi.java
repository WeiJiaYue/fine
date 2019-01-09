package com.radarwin.framework.restclient;

import com.radarwin.framework.util.JsonUtil;
import com.radarwin.framework.util.StringUtil;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by josh on 15/9/23.
 */
abstract class AbstractRestApi implements RestApi {
    private static Logger logger = LogManager.getLogger(AbstractRestApi.class);

    protected String scheme;

    @Override
    public HttpUriRequest buildGetRequest(String host, int port, String path, Map<String, String> params) throws Exception {
        URIBuilder uriBuilder = buildUri(host, port, path);
        if (params != null && params.size() > 0) {
            Set<Map.Entry<String, String>> set = params.entrySet();
            for (Map.Entry<String, String> entry : set) {
                uriBuilder.setParameter(entry.getKey(), entry.getValue());
            }
        }
        URI uri = uriBuilder.build();
        HttpGet httpGet = new HttpGet(uri);
        httpGet.setConfig(Client.getRequestConfig());
        return httpGet;
    }

    @Override
    public HttpUriRequest buildPostRequest(String host, int port, String path, Map<String, String> params) throws Exception {
        URIBuilder uriBuilder = buildUri(host, port, path);
        HttpPost httpPost = new HttpPost(uriBuilder.build());
        httpPost.setConfig(Client.getRequestConfig());
        setPostAndPutFormEntity(httpPost, params);
        return httpPost;
    }

    @Override
    public HttpUriRequest buildJsonPostRequest(String host, int port, String path, Map<String, String> params) throws Exception {
        URIBuilder uriBuilder = buildUri(host, port, path);
        HttpPost httpPost = new HttpPost(uriBuilder.build());
        httpPost.setConfig(Client.getRequestConfig());
        setPostAndPutJsonEntity(httpPost, params);
        return httpPost;
    }

    @Override
    public HttpUriRequest buildJsonPostRequest(String host, int port, String path, String jsonParams) throws Exception {
        URIBuilder uriBuilder = buildUri(host, port, path);
        HttpPost httpPost = new HttpPost(uriBuilder.build());
        httpPost.setConfig(Client.getRequestConfig());
        StringEntity entity = new StringEntity(jsonParams, "UTF-8");
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        return httpPost;
    }

    @Override
    public HttpUriRequest buildJsonRpcPostRequest(String host, int port, String jsonParams) throws Exception {
        URIBuilder uriBuilder = buildUri(host, port, null);
        HttpPost httpPost = new HttpPost(uriBuilder.build());
        httpPost.setConfig(Client.getRequestConfig());
        StringEntity entity = new StringEntity(jsonParams, "UTF-8");
        entity.setContentType("application/json+rpc");
        httpPost.setEntity(entity);
        return httpPost;
    }

    @Override
    public HttpUriRequest buildPutRequest(String host, int port, String path, Map<String, String> params) throws Exception {
        URIBuilder uriBuilder = buildUri(host, port, path);
        HttpPut httpPut = new HttpPut(uriBuilder.build());
        httpPut.setConfig(Client.getRequestConfig());
        setPostAndPutFormEntity(httpPut, params);
        return httpPut;
    }

    @Override
    public HttpUriRequest buildJsonPutRequest(String host, int port, String path, Map<String, String> params) throws Exception {
        URIBuilder uriBuilder = buildUri(host, port, path);
        HttpPut httpPut = new HttpPut(uriBuilder.build());
        httpPut.setConfig(Client.getRequestConfig());
        setPostAndPutJsonEntity(httpPut, params);
        return httpPut;
    }

    @Override
    public HttpUriRequest buildJsonPutRequest(String host, int port, String path, String jsonParams) throws Exception {
        URIBuilder uriBuilder = buildUri(host, port, path);
        HttpPut httpPut = new HttpPut(uriBuilder.build());
        httpPut.setConfig(Client.getRequestConfig());
        StringEntity entity = new StringEntity(jsonParams, "UTF-8");
        entity.setContentType("application/json");
        httpPut.setEntity(entity);
        return httpPut;
    }

    @Override
    public HttpUriRequest buildDeleteRequest(String host, int port, String path, Map<String, String> params) throws Exception {
        URIBuilder uriBuilder = buildUri(host, port, path);
        if (params != null && params.size() > 0) {
            Set<Map.Entry<String, String>> set = params.entrySet();
            for (Map.Entry<String, String> entry : set) {
                uriBuilder.setParameter(entry.getKey(), entry.getValue());
            }
        }
        URI uri = uriBuilder.build();
        HttpDelete httpDelete = new HttpDelete(uri);
        httpDelete.setConfig(Client.getRequestConfig());
        return httpDelete;
    }

    @Override
    public HttpUriRequest buildHeadRequest(String host, int port, String path, Map<String, String> params) throws Exception {
        URIBuilder uriBuilder = buildUri(host, port, path);
        if (params != null && params.size() > 0) {
            Set<Map.Entry<String, String>> set = params.entrySet();
            for (Map.Entry<String, String> entry : set) {
                uriBuilder.setParameter(entry.getKey(), entry.getValue());
            }
        }
        URI uri = uriBuilder.build();
        HttpHead httpHead = new HttpHead(uri);
        httpHead.setConfig(Client.getRequestConfig());
        return httpHead;
    }

    @Override
    public HttpUriRequest buildTraceRequest(String host, int port, String path, Map<String, String> params) throws Exception {
        URIBuilder uriBuilder = buildUri(host, port, path);
        if (params != null && params.size() > 0) {
            Set<Map.Entry<String, String>> set = params.entrySet();
            for (Map.Entry<String, String> entry : set) {
                uriBuilder.setParameter(entry.getKey(), entry.getValue());
            }
        }
        URI uri = uriBuilder.build();
        HttpTrace httpTrace = new HttpTrace(uri);
        httpTrace.setConfig(Client.getRequestConfig());
        return httpTrace;
    }

    private URIBuilder buildUri(String host, int port, String path) {
        URIBuilder builder = new URIBuilder();
        builder.setScheme(getScheme());
        builder.setHost(host);
        builder.setPath(path);
        if (port > 0) {
            builder.setPort(port);
        }


        return builder;
    }

    @Override
    public RestResponse doGet(String host, String path) {
        return doGet(host, 0, path, null);
    }

    @Override
    public RestResponse doGet(String host, String path, Map<String, String> params) {
        return doGet(host, 0, path, params);
    }

    @Override
    public RestResponse doGet(String host, int port, String path) {
        return doGet(host, port, path, null);
    }

    @Override
    public RestResponse doGet(String host, int port, String path, Map<String, String> params) {
        RestResponse restResponse = new RestResponse();
        try {
            restResponse = execute(restResponse, buildGetRequest(host, port, path, params));
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            restResponse.setException(true);
            restResponse.setExceptionObject(e);
        }
        return restResponse;
    }

    @Override
    public RestResponse doPost(String host, String path) {
        return doPost(host, path, null);
    }

    @Override
    public RestResponse doPost(String host, String path, Map<String, String> params) {
        return doPost(host, 0, path, params);
    }

    @Override
    public RestResponse doPost(String host, int port, String path) {
        return doPost(host, port, path, null);
    }

    @Override
    public RestResponse doPost(String host, int port, String path, Map<String, String> params) {
        RestResponse restResponse = new RestResponse();
        try {

            restResponse = execute(restResponse, buildPostRequest(host, port, path, params));
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            restResponse.setException(true);
            restResponse.setExceptionObject(e);
        }
        return restResponse;
    }

    @Override
    public RestResponse doJsonPost(String host, String path) {
        return doJsonPost(host, 0, path, StringUtil.EMPTY);
    }

    @Override
    public RestResponse doJsonPost(String host, String path, Map<String, String> params) {
        return doJsonPost(host, 0, path, params);
    }

    @Override
    public RestResponse doJsonPost(String host, String path, String jsonParams) {
        return doJsonPost(host, 0, path, jsonParams);
    }

    @Override
    public RestResponse doJsonPost(String host, int port, String path) {
        return doJsonPost(host, port, path, StringUtil.EMPTY);
    }

    @Override
    public RestResponse doJsonPost(String host, int port, String path, Map<String, String> params) {
        RestResponse restResponse = new RestResponse();
        try {
            restResponse = execute(restResponse, buildJsonPostRequest(host, port, path, params));
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            restResponse.setException(true);
            restResponse.setExceptionObject(e);
        }
        return restResponse;
    }

    @Override
    public RestResponse doJsonPost(String host, int port, String path, String jsonParams) {
        RestResponse restResponse = new RestResponse();
        try {
            restResponse = execute(restResponse, buildJsonPostRequest(host, port, path, jsonParams));
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            restResponse.setException(true);
            restResponse.setExceptionObject(e);
        }
        return restResponse;
    }

    @Override
    public RestResponse doJsonRpcPost(String host, int port, String jsonParams) {
        RestResponse restResponse = new RestResponse();
        try {
            restResponse = execute(restResponse, buildJsonRpcPostRequest(host, port, jsonParams));
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            restResponse.setException(true);
            restResponse.setExceptionObject(e);
        }
        return restResponse;
    }

    @Override
    public RestResponse doPut(String host, String path) {
        return doPut(host, 0, path, null);
    }

    @Override
    public RestResponse doPut(String host, String path, Map<String, String> params) {
        return doPut(host, 0, path, params);
    }

    @Override
    public RestResponse doPut(String host, int port, String path) {
        return doPut(host, port, path, null);
    }

    @Override
    public RestResponse doPut(String host, int port, String path, Map<String, String> params) {
        RestResponse restResponse = new RestResponse();
        try {
            restResponse = execute(restResponse, buildPutRequest(host, port, path, params));
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            restResponse.setException(true);
            restResponse.setExceptionObject(e);
        }
        return restResponse;
    }

    @Override
    public RestResponse doJsonPut(String host, String path) {
        return doJsonPut(host, 0, path, StringUtil.EMPTY);
    }

    @Override
    public RestResponse doJsonPut(String host, String path, Map<String, String> params) {
        return doJsonPut(host, 0, path, params);
    }

    @Override
    public RestResponse doJsonPut(String host, String path, String jsonParams) {
        return doJsonPut(host, 0, path, jsonParams);
    }

    @Override
    public RestResponse doJsonPut(String host, int port, String path) {
        return doJsonPut(host, port, path, StringUtil.EMPTY);
    }

    @Override
    public RestResponse doJsonPut(String host, int port, String path, Map<String, String> params) {
        RestResponse restResponse = new RestResponse();
        try {
            restResponse = execute(restResponse, buildJsonPutRequest(host, port, path, params));
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            restResponse.setException(true);
            restResponse.setExceptionObject(e);
        }
        return restResponse;
    }

    @Override
    public RestResponse doJsonPut(String host, int port, String path, String jsonParams) {
        RestResponse restResponse = new RestResponse();
        try {
            restResponse = execute(restResponse, buildJsonPutRequest(host, port, path, jsonParams));
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            restResponse.setException(true);
            restResponse.setExceptionObject(e);
        }
        return restResponse;
    }

    @Override
    public RestResponse doDelete(String host, String path) {
        return doDelete(host, 0, path, null);
    }

    @Override
    public RestResponse doDelete(String host, String path, Map<String, String> params) {
        return doDelete(host, 0, path, params);
    }

    @Override
    public RestResponse doDelete(String host, int port, String path) {
        return doDelete(host, port, path, null);
    }

    @Override
    public RestResponse doDelete(String host, int port, String path, Map<String, String> params) {
        RestResponse restResponse = new RestResponse();
        try {
            restResponse = execute(restResponse, buildDeleteRequest(host, port, path, params));
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            restResponse.setException(true);
            restResponse.setExceptionObject(e);
        }
        return restResponse;
    }

    @Override
    public RestResponse doHead(String host, String path) {
        return doHead(host, 0, path, null);
    }

    @Override
    public RestResponse doHead(String host, String path, Map<String, String> params) {
        return doHead(host, 0, path, params);
    }

    @Override
    public RestResponse doHead(String host, int port, String path) {
        return doHead(host, port, path, null);
    }

    @Override
    public RestResponse doHead(String host, int port, String path, Map<String, String> params) {
        RestResponse restResponse = new RestResponse();
        try {
            restResponse = execute(restResponse, buildHeadRequest(host, port, path, params));
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            restResponse.setException(true);
            restResponse.setExceptionObject(e);
        }
        return restResponse;
    }

    @Override
    public RestResponse doTrace(String host, String path) {
        return doTrace(host, 0, path, null);
    }

    @Override
    public RestResponse doTrace(String host, String path, Map<String, String> params) {
        return doTrace(host, 0, path, params);
    }

    @Override
    public RestResponse doTrace(String host, int port, String path) {
        return doTrace(host, port, path, null);
    }

    @Override
    public RestResponse doTrace(String host, int port, String path, Map<String, String> params) {
        RestResponse restResponse = new RestResponse();
        try {
            restResponse = execute(restResponse, buildTraceRequest(host, port, path, params));
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            restResponse.setException(true);
            restResponse.setExceptionObject(e);
        }
        return restResponse;
    }

    @Override
    public void destroy() {
        try {
            Client.getRestClient().close();
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
    }

    @Override
    public RestResponse execute(RestResponse restResponse, HttpUriRequest request) {

        CloseableHttpResponse closeableHttpResponse = null;
        BufferedInputStream bis = null;
        CloseableHttpClient client = Client.getRestClient();
        try {
            closeableHttpResponse = client.execute(request);
            HttpEntity entity = closeableHttpResponse.getEntity();
            convertResponse(closeableHttpResponse, entity, restResponse);
            InputStream inputStream = entity.getContent();
            if (inputStream != null) {
                bis = new BufferedInputStream(inputStream, 5 * 4096);
                String s = readString(bis);
                restResponse.setContent(s);
            }
            restResponse.setSuccess(true);
        } catch (Exception e) {
            logger.error(e);
            restResponse.setException(true);
            restResponse.setExceptionObject(e);
        } finally {
            closeResponse(closeableHttpResponse, bis);
        }
        return restResponse;
    }

    @Override
    public RestResponse execute(HttpUriRequest request) {

        RestResponse restResponse = new RestResponse();

        CloseableHttpResponse closeableHttpResponse = null;
        BufferedInputStream bis = null;
        CloseableHttpClient client = Client.getRestClient();
        try {
            closeableHttpResponse = client.execute(request);
            HttpEntity entity = closeableHttpResponse.getEntity();
            convertResponse(closeableHttpResponse, entity, restResponse);
            InputStream inputStream = entity.getContent();
            if (inputStream != null) {
                bis = new BufferedInputStream(inputStream, 5 * 4096);
                String s = readString(bis);
                restResponse.setContent(s);
            }
            restResponse.setSuccess(true);
        } catch (Exception e) {
            logger.error(e);
            restResponse.setException(true);
            restResponse.setExceptionObject(e);
        } finally {
            closeResponse(closeableHttpResponse, bis);
        }
        return restResponse;
    }

    private void convertResponse(CloseableHttpResponse closeableHttpResponse, HttpEntity entity, RestResponse restResponse) {
        restResponse.setStatusCode(closeableHttpResponse.getStatusLine().getStatusCode());
        restResponse.setHeaders(closeableHttpResponse.getAllHeaders());
        restResponse.setContentEncoding(entity.getContentEncoding());
        restResponse.setContentType(entity.getContentType());
    }

    private String readString(BufferedInputStream bis) throws Exception {
        StringBuilder tmp = new StringBuilder();
        byte[] contents = new byte[1024];
        int byteRead = 0;
        while ((byteRead = bis.read(contents)) != -1) {
            tmp.append(new String(contents, 0, byteRead, "utf-8"));
        }
        return tmp.toString();
    }

    private void setPostAndPutFormEntity(HttpEntityEnclosingRequestBase httpPostAndPut, Map<String, String> params) throws UnsupportedEncodingException {
        if (params != null && params.size() > 0) {
            Set<Map.Entry<String, String>> set = params.entrySet();
            List<NameValuePair> nvps = new ArrayList<>();
            for (Map.Entry<String, String> entry : set) {
                nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            httpPostAndPut.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
        }
    }

    private void setPostAndPutJsonEntity(HttpEntityEnclosingRequestBase httpPostAndPut, Map<String, String> params) {
        if (params != null && params.size() > 0) {
            StringEntity entity = new StringEntity(JsonUtil.objectToJson(params), "UTF-8");
            entity.setContentType("application/json");
            httpPostAndPut.setEntity(entity);
        }
    }

    private void closeResponse(CloseableHttpResponse closeableHttpResponse, BufferedInputStream bis) {
        if (bis != null) {
            try {
                bis.close();
            } catch (Exception e) {
                logger.error(ExceptionUtils.getStackTrace(e));
            }
        }
        if (closeableHttpResponse != null) {
            try {
                closeableHttpResponse.close();
            } catch (Exception e) {
                logger.error(ExceptionUtils.getStackTrace(e));
            }
        }
    }

    protected String getScheme() {
        return scheme;
    }

    protected void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public static void main(String[] args) {

        RestResponse restResponse = RestExecutor.getHttpsExecutor().doGet("api.zcha.in", "/v1/mainnet/network");

        // RestResponse restResponse = RestExecutor.getHttpsExecutor().doGet("api.zcha.in", "/v1/mainnet/network");

        //RestResponse restResponse = RestExecutor.getHttpsExecutor().doGet("blockexplorer.com", "/api/rawblock/0000000000000000079c58e8b5bce4217f7515a74b170049398ed9b8428beb4a");
        System.out.println(restResponse.getContent());
    }
}
