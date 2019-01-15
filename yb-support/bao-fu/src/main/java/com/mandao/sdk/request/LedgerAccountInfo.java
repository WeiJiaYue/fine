package com.mandao.sdk.request;


import com.mandao.sdk.RequiredParam;

import java.math.BigDecimal;

/**
 * 分账信息 消费分账的{@link MerchantConsumptionRequest}
 */
public class LedgerAccountInfo {


    @RequiredParam
    private String inUserNo;        //入账方用户编号

    @RequiredParam
    private BigDecimal amount;      //交易金额

    private BigDecimal feeAmount;   //手续费


    public LedgerAccountInfo(String inUserNo, BigDecimal amount) {
        this.inUserNo = inUserNo;
        this.amount = amount;
    }

    public String getInUserNo() {
        return inUserNo;
    }

    public void setInUserNo(String inUserNo) {
        this.inUserNo = inUserNo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(BigDecimal feeAmount) {
        this.feeAmount = feeAmount;
    }
}
