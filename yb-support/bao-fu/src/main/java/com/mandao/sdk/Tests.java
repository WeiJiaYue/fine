package com.mandao.sdk;

import com.mandao.sdk.request.*;
import com.mandao.sdk.response.Response;
import com.radarwin.framework.util.DateUtil;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Date;

public class Tests {

    final static String mobile = "18715626526";
    final static String bankCardNo = "6214851210440538";
    final static String certificateNo = "342531199307141215";


    final String userNo1 = "88";
    final String userNo2 = "99";
    final String userNo3 = "10";
    final String enNo1 = "en88";


    /**
     * 查询交易状态
     */
    @Test
    public void getTransactionProcessing() {
        TransactionProcessingRequest r = new TransactionProcessingRequest();
        r.setUserNo(enNo1);
        r.setPrequestNo("");
        r.setBizType(Dictionary.BizType.RECHARGE);
        r.setPrequestNo("201901111607039437394");
        Response response = new FuMinApiImpl().getTransactionProcessing(r);
        System.out.println(response);
    }


    /**
     * 查询账户明细
     */
    @Test
    public void getAccountDetail() {
        AccountDetailRequest r4 = new AccountDetailRequest();
        r4.setUserNo(enNo1);
        r4.setPageNo(1);
        r4.setPageSize(10);
        r4.setTradeStartDate(DateUtil.convertToString(new Date(), DateUtil.YYYY_MM_DD));
        r4.setTradeEndDate(DateUtil.convertToString(new Date(), DateUtil.YYYY_MM_DD));
        Response response = new FuMinApiImpl().getAccountDetails(r4);
        System.out.println(response);
    }


    /**
     * 查询余额
     */
    @Test
    public void getBalance() {
        BalanceRequest r2 = new BalanceRequest();
        r2.setUserNo(enNo1);
        System.out.println(new FuMinApiImpl().getBalance(r2));
    }


    /**
     * 个人实名开户
     */
    @Test
    public void individualOpenAccount() {
        IndividualRealOpenAccountRequest r = new IndividualRealOpenAccountRequest();
        r.setUserNo("10");
        r.setRealName("余卫家");
        r.setMobile(mobile);
        r.setBankCardNo(bankCardNo);
        r.setIdCardType(Dictionary.IndividualCertType.PRC_ID);
        r.setCertificateNo(certificateNo);
        Response result = new FuMinApiImpl().individualRealOpenAccount(r);
        System.out.println(result);
    }


    //网关支付
    @Test
    public void gatewayPay() {
        GatewayPaymentRequest r3 = new GatewayPaymentRequest();
        r3.setAmount(new BigDecimal("9999"));
        r3.setPageUrl("https://www.xinshetong.com/");
        r3.setMobile("18715626526");
        r3.setRealName("余卫家");
        r3.setNotifyUrl("https://www.xinshetong.com/");
        r3.setSubTradeType("5");
        r3.setUserNo(enNo1);
        System.out.println(new FuMinApiImpl().gatewayPay(r3));
    }


    /**
     * 企业实名开户
     */
    @Test
    public void testEnterpriseRealOpenAccount() {
        EnterpriseRealOpenAccountRequest request = new EnterpriseRealOpenAccountRequest();
        request.setEnterpriseName("深圳市亿诚电子科技有限公司");
        request.setEnterpriseCardType(Dictionary.EnterpriseCertType.THREE_CERTIFICATES);
//        request.setEnterpriseRegistrationNo("91310115MA1H971RXF");
        request.setEnterpriseRegistrationNo("15000000201801290253");
        request.setLegal("徐嘉阳");
        request.setIdCardType(Dictionary.IndividualCertType.PRC_ID);
        request.setLegalIdCardNo("342531199307141215");
        request.setUserNo(enNo1);
        request.setMobile("18715626526");
        request.setAccountName("深圳市亿诚电子科技有限公司");
        request.setBankCode("BCM");
        request.setBankCardNo("6222620210001358695");
        request.setBankName("交通银行");
        request.setAcctType(Dictionary.AccountType.OTHERS.get());
        request.setBusinessType(Dictionary.BusinessType.TO_PUBLIC.get());
        System.out.println(new FuMinApiImpl().enterpriseRealOpenAccount(request));
    }

    /**
     * 消费分账
     */
    @Test
    public void merchantConsumption() {
        MerchantConsumptionRequest request = new MerchantConsumptionRequest();
        request.setUserNo(userNo1);
        request.setOutUserNo(enNo1);
        request.setAmount(new BigDecimal("1.99"));
        request.setPoundageAmount(new BigDecimal("0"));
        request.setLedgerAccountInfo("");
        System.out.println(new FuMinApiImpl().merchantConsumption(request));
    }
}
