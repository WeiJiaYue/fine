package com.mandao.sdk.request;

import com.mandao.sdk.Dictionary;
import com.mandao.sdk.RequiredParam;


/**
 * 个人实名绑卡开户请求参数
 */
public class IndividualRealOpenAccountRequest extends Request {


    @RequiredParam
    private String realName;                            //真实姓名

    @RequiredParam
    private String mobile;                              //银行预留手机号

    @RequiredParam
    private String bankCardNo;                          //银行卡号仅限个人借记卡卡号

    @RequiredParam
    private Dictionary.IndividualCertType idCardType;   //证件类型

    @RequiredParam
    private String certificateNo;                       //证件号

    private String cardStartDate;                       //证件签发日 YYYY-MM-DD

    private String cardEndDate;                         //证件到期日'YYYY-MM-DD'。如是长期等，请转成“2999-12-31”

    private String cardIssuingAuthority;                //签发机关

    private String homeAddr;                            //户籍地址

    private String activeCode;                          //短信验证码（时效为10分钟，时效期内最多只允许错五次）

    private String activeCodeSerialNo;                  //验证码交易流水号（需配合发送短信接口）


    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getBankCardNo() {
        return bankCardNo;
    }

    public void setBankCardNo(String bankCardNo) {
        this.bankCardNo = bankCardNo;
    }

    public Dictionary.IndividualCertType getIdCardType() {
        return idCardType;
    }

    public void setIdCardType(Dictionary.IndividualCertType idCardType) {
        this.idCardType = idCardType;
    }

    public String getCertificateNo() {
        return certificateNo;
    }

    public void setCertificateNo(String certificateNo) {
        this.certificateNo = certificateNo;
    }

    public String getCardStartDate() {
        return cardStartDate;
    }

    public void setCardStartDate(String cardStartDate) {
        this.cardStartDate = cardStartDate;
    }

    public String getCardEndDate() {
        return cardEndDate;
    }

    public void setCardEndDate(String cardEndDate) {
        this.cardEndDate = cardEndDate;
    }

    public String getCardIssuingAuthority() {
        return cardIssuingAuthority;
    }

    public void setCardIssuingAuthority(String cardIssuingAuthority) {
        this.cardIssuingAuthority = cardIssuingAuthority;
    }

    public String getHomeAddr() {
        return homeAddr;
    }

    public void setHomeAddr(String homeAddr) {
        this.homeAddr = homeAddr;
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
}
