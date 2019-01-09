package com.mandao.sdk.request;

import com.mandao.sdk.Dictionary;
import com.mandao.sdk.RequiredParam;


/**
 * 企业实名绑卡开户请求参数
 */
public class EnterpriseRealOpenAccountRequest extends Request {


    @RequiredParam
    private String enterpriseName;                            //企业名称

    @RequiredParam
    private Dictionary.EnterpriseCertType enterpriseCardType; //企业证件类型

    @RequiredParam
    private String enterpriseRegistrationNo;                  //企业证件号（与企业证件类型对应的企业注册号：营业执照编号、统一社会信用代码、三证合一代码）

    @RequiredParam
    private String legal;                                     //法人姓名

    @RequiredParam
    private Dictionary.IndividualCertType idCardType;         //法人个人证件类型

    @RequiredParam
    private String certificateNo;                             //证件号

    private String cardStartDate;                             //证件签发日 YYYY-MM-DD

    private String cardEndDate;                               //证件到期日'YYYY-MM-DD'。如是长期等，请转成“2999-12-31”

    private String cardIssuingAuthority;                      //签发机关

    private String homeAddr;                                  //户籍地址

    private String activeCode;                                //短信验证码（时效为10分钟，时效期内最多只允许错五次）

    private String activeCodeSerialNo;                        //验证码交易流水号（需配合发送短信接口）

    private String businessLicenseRegistDate;                 //证件注册日期'YYYY-MM-DD'

    private String businessLicenseEndDate;                    //证件到期日期'YYYY-MM-DD'。如果是汉字描述的到期日期，如：永久，长期等，请转成“2999-12-31”


    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    public Dictionary.EnterpriseCertType getEnterpriseCardType() {
        return enterpriseCardType;
    }

    public void setEnterpriseCardType(Dictionary.EnterpriseCertType enterpriseCardType) {
        this.enterpriseCardType = enterpriseCardType;
    }

    public String getEnterpriseRegistrationNo() {
        return enterpriseRegistrationNo;
    }

    public void setEnterpriseRegistrationNo(String enterpriseRegistrationNo) {
        this.enterpriseRegistrationNo = enterpriseRegistrationNo;
    }

    public String getLegal() {
        return legal;
    }

    public void setLegal(String legal) {
        this.legal = legal;
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

    public String getBusinessLicenseRegistDate() {
        return businessLicenseRegistDate;
    }

    public void setBusinessLicenseRegistDate(String businessLicenseRegistDate) {
        this.businessLicenseRegistDate = businessLicenseRegistDate;
    }

    public String getBusinessLicenseEndDate() {
        return businessLicenseEndDate;
    }

    public void setBusinessLicenseEndDate(String businessLicenseEndDate) {
        this.businessLicenseEndDate = businessLicenseEndDate;
    }
}
