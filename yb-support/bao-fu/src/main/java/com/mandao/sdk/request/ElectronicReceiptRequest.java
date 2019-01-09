package com.mandao.sdk.request;


import com.mandao.sdk.Dictionary;
import com.mandao.sdk.RequiredParam;

/**
 * 电子回单请求参数
 */
public class ElectronicReceiptRequest extends Request {


    @RequiredParam
    private String preRequestNo;                //原订单流水号

    @RequiredParam
    private Dictionary.BizType bizType;         /**{@link com.mandao.sdk.Dictionary.BizType}*/


    public String getPreRequestNo() {
        return preRequestNo;
    }

    public void setPreRequestNo(String preRequestNo) {
        this.preRequestNo = preRequestNo;
    }

    public Dictionary.BizType getBizType() {
        return bizType;
    }

    public void setBizType(Dictionary.BizType bizType) {
        this.bizType = bizType;
    }
}
