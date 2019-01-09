package com.radarwin.framework.restclient;

import org.apache.http.client.methods.HttpUriRequest;

import java.util.Map;

/**
 * Created by josh on 15/9/23.
 */
public interface RestApi {

    HttpUriRequest buildGetRequest(String host, int port, String path, Map<String, String> params) throws Exception;

    HttpUriRequest buildPostRequest(String host, int port, String path, Map<String, String> params) throws Exception;

    HttpUriRequest buildJsonPostRequest(String host, int port, String path, Map<String, String> params) throws Exception;

    HttpUriRequest buildJsonPostRequest(String host, int port, String path, String jsonParams) throws Exception;

    HttpUriRequest buildJsonRpcPostRequest(String host, int port, String jsonParams) throws Exception;

    HttpUriRequest buildPutRequest(String host, int port, String path, Map<String, String> params) throws Exception;

    HttpUriRequest buildJsonPutRequest(String host, int port, String path, Map<String, String> params) throws Exception;

    HttpUriRequest buildJsonPutRequest(String host, int port, String path, String jsonParams) throws Exception;

    HttpUriRequest buildDeleteRequest(String host, int port, String path, Map<String, String> params) throws Exception;

    HttpUriRequest buildHeadRequest(String host, int port, String path, Map<String, String> params) throws Exception;

    HttpUriRequest buildTraceRequest(String host, int port, String path, Map<String, String> params) throws Exception;

    RestResponse doGet(String host, String path);

    RestResponse doGet(String host, String path, Map<String, String> params);

    RestResponse doGet(String host, int port, String path);

    RestResponse doGet(String host, int port, String path, Map<String, String> params);

    RestResponse doPost(String host, String path);

    RestResponse doPost(String host, String path, Map<String, String> params);

    RestResponse doPost(String host, int port, String path);

    RestResponse doPost(String host, int port, String path, Map<String, String> params);

    RestResponse doJsonPost(String host, String path);

    RestResponse doJsonPost(String host, String path, Map<String, String> params);

    RestResponse doJsonPost(String host, String path, String jsonParams);

    RestResponse doJsonPost(String host, int port, String path);

    RestResponse doJsonPost(String host, int port, String path, Map<String, String> params);

    RestResponse doJsonPost(String host, int port, String path, String jsonParams);

    RestResponse doJsonRpcPost(String host, int port,String jsonParams);

    RestResponse doPut(String host, String path);

    RestResponse doPut(String host, String path, Map<String, String> params);

    RestResponse doPut(String host, int port, String path);

    RestResponse doPut(String host, int port, String path, Map<String, String> params);

    RestResponse doJsonPut(String host, String path);

    RestResponse doJsonPut(String host, String path, Map<String, String> params);

    RestResponse doJsonPut(String host, String path, String jsonParams);

    RestResponse doJsonPut(String host, int port, String path);

    RestResponse doJsonPut(String host, int port, String path, Map<String, String> params);

    RestResponse doJsonPut(String host, int port, String path, String jsonParams);

    RestResponse doDelete(String host, String path);

    RestResponse doDelete(String host, String path, Map<String, String> params);

    RestResponse doDelete(String host, int port, String path);

    RestResponse doDelete(String host, int port, String path, Map<String, String> params);

    RestResponse doHead(String host, String path);

    RestResponse doHead(String host, String path, Map<String, String> params);

    RestResponse doHead(String host, int port, String path);

    RestResponse doHead(String host, int port, String path, Map<String, String> params);

    RestResponse doTrace(String host, String path);

    RestResponse doTrace(String host, String path, Map<String, String> params);

    RestResponse doTrace(String host, int port, String path);

    RestResponse doTrace(String host, int port, String path, Map<String, String> params);

    void destroy();

    RestResponse execute(HttpUriRequest request);

    RestResponse execute(RestResponse restResponse, HttpUriRequest request);
}
