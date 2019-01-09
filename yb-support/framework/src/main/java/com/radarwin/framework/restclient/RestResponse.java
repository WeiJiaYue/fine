package com.radarwin.framework.restclient;

import org.apache.http.Header;

/**
 * Created by josh on 15/9/23.
 */
public class RestResponse {
    private String content;
    private long contentLength;
    private int statusCode;
    private boolean success = false;
    private boolean exception = false;
    private Exception exceptionObject;
    private Header contentType;
    private Header contentEncoding;
    private Header[] headers;


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getContentLength() {
        return contentLength;
    }

    public void setContentLength(long contentLength) {
        this.contentLength = contentLength;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isException() {
        return exception;
    }

    public void setException(boolean exception) {
        this.exception = exception;
    }

    public Exception getExceptionObject() {
        return exceptionObject;
    }

    public void setExceptionObject(Exception exceptionObject) {
        this.exceptionObject = exceptionObject;
    }

    public Header getContentType() {
        return contentType;
    }

    public void setContentType(Header contentType) {
        this.contentType = contentType;
    }

    public Header getContentEncoding() {
        return contentEncoding;
    }

    public void setContentEncoding(Header contentEncoding) {
        this.contentEncoding = contentEncoding;
    }

    public Header[] getHeaders() {
        return headers;
    }

    public void setHeaders(Header[] headers) {
        this.headers = headers;
    }
}
