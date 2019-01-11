package com.mandao.sdk.request;

import com.mandao.BankCode;
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
    private String legalIdCardNo;                             //法人证件号

    @RequiredParam
    private String mobile;                                    //法人手机号

    @RequiredParam
    private String bankCardNo;                                //银行卡号

    @RequiredParam
    private String bankCode;                                  //银行编码

    @RequiredParam
    private String accountName;                               //账户名

    @RequiredParam
    private String bankName;                                  //银行名称

    /**
     * {@link com.mandao.sdk.Dictionary.AccountType}
     */
    @RequiredParam
    private Integer acctType;

    /**
     * {@link com.mandao.sdk.Dictionary.BusinessType}
     */
    @RequiredParam
    private Integer businessType;


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


    public String getLegalIdCardNo() {
        return legalIdCardNo;
    }

    public void setLegalIdCardNo(String legalIdCardNo) {
        this.legalIdCardNo = legalIdCardNo;
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

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        if(bankCardNo!=null){
            this.bankCode= BankCode.getSwiftCodeFromAliPay(bankCardNo);
            if(this.bankCode!=null){
                this.bankCode= BankCode.toLocaleSwift(bankCode);
            }
        }else{
            this.bankCode = bankCode;
        }
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public Integer getAcctType() {
        return acctType;
    }

    public void setAcctType(Integer acctType) {
        this.acctType = acctType;
    }

    public Integer getBusinessType() {
        return businessType;
    }

    public void setBusinessType(Integer businessType) {
        this.businessType = businessType;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
}
