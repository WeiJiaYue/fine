package com.mandao.sdk.request;

import com.mandao.sdk.FuMinApi;

import java.util.List;

public interface BaseRequest {


    void validateRequiredParams(Class clz, List<String> error, String... excludeFields);

    FuMinApi.ApiType getApiType();

    String getRequestNo();

    String getTimestamp();

    String getRequestChannel();


}
