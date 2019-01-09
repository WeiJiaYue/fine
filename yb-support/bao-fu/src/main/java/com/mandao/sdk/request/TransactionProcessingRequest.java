package com.mandao.sdk.request;

import com.mandao.sdk.Dictionary;
import com.mandao.sdk.RequiredParam;

public class TransactionProcessingRequest extends Request {

    @RequiredParam
    private String prequestNo;          //原订单流水号

    @RequiredParam
    private Dictionary.BizType bizType; //交易订单类型

    public String getPrequestNo() {
        return prequestNo;
    }

    public void setPrequestNo(String prequestNo) {
        this.prequestNo = prequestNo;
    }

    public Dictionary.BizType getBizType() {
        return bizType;
    }

    public void setBizType(Dictionary.BizType bizType) {
        this.bizType = bizType;
    }
}
