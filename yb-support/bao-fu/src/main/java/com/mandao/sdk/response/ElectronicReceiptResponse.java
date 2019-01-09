package com.mandao.sdk.response;

public class ElectronicReceiptResponse extends Response {


    private String baseContent; //Pdf转码base64码


    public String getBaseContent() {
        return baseContent;
    }

    public void setBaseContent(String baseContent) {
        this.baseContent = baseContent;
    }
}
