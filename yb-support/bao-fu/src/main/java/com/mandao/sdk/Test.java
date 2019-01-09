package com.mandao.sdk;

import com.mandao.sdk.request.*;
import com.mandao.sdk.response.Response;
import com.radarwin.framework.util.DateUtil;

import java.math.BigDecimal;
import java.util.Date;

public class Test {

    final static String mobile = "18715626526";
    final static String bankCardNo = "6214851210440538";
    final static String certificateNo = "342531199307141215";


    final static String userNo1 = "88";
    final static String userNo2 = "99";
    final static String userNo3 = "10";

    public static void main(String[] args) {
//        individualOpenAccount();

        gatewayPay(userNo3);

    }


    private static void getAccountDetail(String userNo, String prequestNo, Dictionary.BizType bizType) {
        TransactionProcessingRequest r = new TransactionProcessingRequest();
        r.setUserNo(userNo);
        r.setPrequestNo(prequestNo);
        r.setBizType(bizType);
        Response response = new FuMinApiImpl().getTransactionProcessing(r);
        System.out.println(response);
    }


    private static void getAccountDetail(String userNo) {
        AccountDetailRequest r4 = new AccountDetailRequest();
        r4.setUserNo(userNo);
        r4.setPageNo(1);
        r4.setPageSize(10);
        r4.setTradeStartDate(DateUtil.convertToString(new Date(), DateUtil.YYYY_MM_DD));
        r4.setTradeEndDate(DateUtil.convertToString(new Date(), DateUtil.YYYY_MM_DD));
        Response response = new FuMinApiImpl().getAccountDetails(r4);
        System.out.println(response);
    }


    private static void getBalance(String userNo) {
        BalanceRequest r2 = new BalanceRequest();
        r2.setUserNo(userNo);
        System.out.println(new FuMinApiImpl().getBalance(r2));
    }

    private static void individualOpenAccount() {
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

    private static void gatewayPay(String userNo) {
        GatewayPaymentRequest r3 = new GatewayPaymentRequest();
        r3.setAmount(new BigDecimal("99.99"));
        r3.setPageUrl("https://www.xinshetong.com/");
        r3.setUserNo(userNo);
        System.out.println(new FuMinApiImpl().gatewayPay(r3));
    }
}
