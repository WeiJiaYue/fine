package com.radarwin.framework.restclient;

/**
 * Created by josh on 15/9/23.
 */
public class RestExecutor {
    private static RestApi httpProxy = new RestExecutorProxy("http");
    private static RestApi httpsProxy = new RestExecutorProxy("https");

    public static RestApi getHttpExecutor() {
        return httpProxy;
    }

    public static RestApi getHttpsExecutor() {
        return httpsProxy;
    }
}
