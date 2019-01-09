package com.radarwin.framework.restclient;

/**
 * Created by josh on 15/9/23.
 */
public class RestExecutorProxy extends AbstractRestApi implements RestApi {

    public RestExecutorProxy(String scheme) {
        this.setScheme(scheme);
    }
}
