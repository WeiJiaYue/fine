package com.mandao.sdk.request;

import com.mandao.sdk.RequiredParam;

import java.math.BigDecimal;
import java.util.List;


/**
 * 网关支付请求参数
 * 网关支付只适用于企业用户
 *
 * 用于企业充值以及企业转账企业
 */
public class GatewayPaymentRequest extends Request {


    @RequiredParam
    private BigDecimal amount;              //金额

    @RequiredParam
    private String mobile;                  //手机号

    @RequiredParam
    private String realName;                //姓名

//    @RequiredParam
    private String pageUrl;                 //前台跳转银联地址

    @RequiredParam
    private boolean guarantee = false;      //false非担保(默认) true:担保

    @RequiredParam
    private String notifyUrl;               //回调地址

    @RequiredParam
    private String subTradeType;            //交易子类型：5充值

    @RequiredParam
    private String consumerType;            //消费类型：1不垫资的充值

    private String tradeRemark;             //交易备注

    private List<ProrateInfo> prorateInfo;             //分账信息（json数组字符串） //consumerType为1，分账用户传自己，为0，传其他用户


    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public boolean isGuarantee() {
        return guarantee;
    }

    public void setGuarantee(boolean guarantee) {
        this.guarantee = guarantee;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getSubTradeType() {
        return subTradeType;
    }

    public void setSubTradeType(String subTradeType) {
        this.subTradeType = subTradeType;
    }

    public String getConsumerType() {
        return consumerType;
    }

    public void setConsumerType(String consumerType) {
        this.consumerType = consumerType;
    }

    public String getTradeRemark() {
        return tradeRemark;
    }

    public void setTradeRemark(String tradeRemark) {
        this.tradeRemark = tradeRemark;
    }


    public List<ProrateInfo> getProrateInfo() {
        return prorateInfo;
    }

    public void setProrateInfo(List<ProrateInfo> prorateInfo) {
        this.prorateInfo = prorateInfo;
    }
}
