package com.mandao.sdk.request;

import com.mandao.sdk.RequiredParam;

import java.math.BigDecimal;

public class ProrateInfo {

    @RequiredParam
    private String userNo;          //入账方用户编号

    @RequiredParam
    private BigDecimal amount;      //交易金额


    public ProrateInfo(String userNo, BigDecimal amount) {
        this.userNo = userNo;
        this.amount = amount;
    }

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
