package com.radarwin.framework.web.response;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by josh on 15/7/2.
 */
public class ResponseWrapper {

    private int status;

    private Map<String, Object> dataWrapper = new HashMap<>();

    private String msg;

    private boolean success;

    private String sourceUrl;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void addAttribute(String key, Object val) {
        dataWrapper.put(key, val);
    }

    public void removeAttribute(String key) {
        dataWrapper.remove(key);
    }

    public Map<String, Object> getDataWrapper() {
        return dataWrapper;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }
}
