package com.mandao.sdk.request;

import com.mandao.sdk.RequiredParam;

import java.math.BigDecimal;

public class WithdrawalRequest extends Request {

    @RequiredParam
    private BigDecimal amount;                  //金额

    /**
     * 提现账号类型{@link com.mandao.sdk.Dictionary.WithdrawalAccountType}的value值
     */
    @RequiredParam
    private String inAccountType;

    @RequiredParam
    private String bankCardNo;                  //提现卡号

    @RequiredParam
    private String realName;                    //真实姓名

    private String bankCode;                    //提现账户类型为对公时此参数必传

    private String tradeRemark;                 //交易备注

    private String activeCode;                  //短信验证码（时效为10分钟，时效期内最多只允许错五次）

    private String activeCodeSerialNo;          //验证码交易流水号（需配合发送短信接口）

    private BigDecimal poundageAmount;          //手续费


    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getInAccountType() {
        return inAccountType;
    }

    public void setInAccountType(String inAccountType) {
        this.inAccountType = inAccountType;
    }

    public String getBankCardNo() {
        return bankCardNo;
    }

    public void setBankCardNo(String bankCardNo) {
        this.bankCardNo = bankCardNo;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getTradeRemark() {
        return tradeRemark;
    }

    public void setTradeRemark(String tradeRemark) {
        this.tradeRemark = tradeRemark;
    }

    public String getActiveCode() {
        return activeCode;
    }

    public void setActiveCode(String activeCode) {
        this.activeCode = activeCode;
    }

    public String getActiveCodeSerialNo() {
        return activeCodeSerialNo;
    }

    public void setActiveCodeSerialNo(String activeCodeSerialNo) {
        this.activeCodeSerialNo = activeCodeSerialNo;
    }

    public BigDecimal getPoundageAmount() {
        return poundageAmount;
    }

    public void setPoundageAmount(BigDecimal poundageAmount) {
        this.poundageAmount = poundageAmount;
    }
}
