package com.mandao.sdk.response;

import java.math.BigDecimal;

public class BalanceRecord {


    private String tradeDate;   //交易发起时间

    private String requestNo;   //请求流水号【备注：此处为交易时请求流水号】

    private BigDecimal amount;  //交易金额

    private BigDecimal balance; //交易后余额

    private String bizType;     //交易类型

    public String getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(String tradeDate) {
        this.tradeDate = tradeDate;
    }

    public String getRequestNo() {
        return requestNo;
    }

    public void setRequestNo(String requestNo) {
        this.requestNo = requestNo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }
}
