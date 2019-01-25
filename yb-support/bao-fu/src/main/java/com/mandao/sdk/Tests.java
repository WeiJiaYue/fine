package com.mandao.sdk;

import com.mandao.sdk.request.*;
import com.mandao.sdk.response.Response;
import com.radarwin.framework.util.DateUtil;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.*;

public class Tests {


    final static String mobile18715626527 = "18715626527";

    final static String mobile = "18715626525";
    final static String bankCardNo = "6214851210440538";
    final static String certificateNo = "342531199307141215";


    final String userNo88 = "88";

    final String individualUserNo99 = "individualUserNo99";
    final String individualUserNo66 = "individualUserNo66";

    final String enterprise88 = "enterprise-88";


    /**
     * 查询交易状态
     * 查询网关充值BizType为 Dictionary.BizType.RECHARGE
     * 查询消费分账BizType为 Dictionary.BizType.CONSUME
     * 查询提现BizType为 Dictionary.BizType.WITHDRAW
     */
    @Test
    public void getTransactionProcessing() {
        TransactionProcessingRequest r = new TransactionProcessingRequest();
        r.setUserNo(individualUserNo99);
        r.setBizType(Dictionary.BizType.WITHDRAW);
        r.setPreRequestNo("201901231442567755789");
        Response response = new FuMinApiImpl().getTransactionProcessing(r);
        System.out.println(response);
    }


    /**
     * 查询账户明细
     */
    @Test
    public void getAccountDetail() {
        AccountDetailRequest r4 = new AccountDetailRequest();
        r4.setUserNo(individualUserNo99);
        r4.setPageNo(1);
        r4.setPageSize(10);
        r4.setTradeStartDate("2018-12-12");
        r4.setTradeEndDate(DateUtil.convertToString(new Date(), DateUtil.YYYY_MM_DD));
        Response response = FuMinApi.getInstance().getAccountDetails(r4);
        System.out.println(response);
    }


    /**
     * 查询个人余额
     */
    @Test
    public void getIndividualBalance() {
        BalanceRequest r2 = new BalanceRequest();
        r2.setUserNo(individualUserNo99);
        System.out.println(FuMinApi.getInstance().getBalance(r2));
    }


    /**
     * 查询企业余额
     */
    @Test
    public void getEnterpriseBalance() {
        BalanceRequest r2 = new BalanceRequest();
        r2.setUserNo(enterprise88);
        System.out.println(new FuMinApiImpl().getBalance(r2));
    }


    /**
     * 个人实名开户
     * 手机号码和身份证不一样可以在开设个人虚拟账户
     */
    @Test
    public void individualOpenAccount() {
        IndividualRealOpenAccountRequest r = new IndividualRealOpenAccountRequest();
        r.setUserNo(individualUserNo99);
        r.setRealName("余卫家");
        r.setMobile("18715626526");
        r.setBankCardNo("6270613901000000548");
        r.setIdCardType(Dictionary.IndividualCertType.PRC_ID);
        r.setCertificateNo("342531199307141215");
        Response result = FuMinApi.getInstance().individualRealOpenAccount(r);
        System.out.println(result);
    }

    /**
     * 个人实名绑卡
     */
    /**
     * 个人实名开户
     * 手机号码和身份证不一样可以在开设个人虚拟账户
     */
    @Test
    public void individualRealBindCard() {
        IndividualRealBindCardRequest r = new IndividualRealBindCardRequest();
        r.setUserNo(individualUserNo99);
        r.setRealName("余卫家");
        r.setMobile("18715626526");
        r.setBankCardNo("6270613901000000548");
        r.setIdCardType(Dictionary.IndividualCertType.PRC_ID);
        r.setCertificateNo("342531199307141215");
        Response result = FuMinApi.getInstance().individualRealBindCard(r);
        System.out.println(result);

    }


        //网关支付
    @Test
    public void gatewayPay() {
        GatewayPaymentRequest r3 = new GatewayPaymentRequest();
        r3.setAmount(new BigDecimal("50000"));
        r3.setPageUrl("https://www.xinshetong.com/");
        r3.setMobile("18715626526");
        r3.setRealName("余卫家");
        r3.setNotifyUrl("http://egj3rx.natappfree.cc/notifyByFuMin");
        r3.setSubTradeType("5");
        r3.setUserNo(enterprise88);

        r3.setConsumerType("0");
        List<ProrateInfo> prorateInfos = new ArrayList<>();
        prorateInfos.add(new ProrateInfo(enterprise88, new BigDecimal("50000")));
        r3.setProrateInfo(prorateInfos);
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
        request.setEnterpriseRegistrationNo("15000000201801290253");
        request.setLegal("徐嘉阳");
        request.setIdCardType(Dictionary.IndividualCertType.PRC_ID);
        request.setLegalIdCardNo("342531199307141211");
        request.setUserNo(enterprise88);
        request.setMobile("18715626522");
        request.setAccountName("深圳市亿诚电子科技有限公司");
        request.setBankCode("BCM");
        request.setBankCardNo("622262021000135869");
        request.setBankName("交通银行");
        request.setAcctType(Dictionary.AccountType.OTHERS.get());
        request.setBusinessType(Dictionary.BusinessType.TO_PUBLIC.get());
        System.out.println(new FuMinApiImpl().enterpriseRealOpenAccount(request));
    }

    /**
     * 消费分账
     * 支持多笔
     * 只需在List<LedgerAccountInfo> 泛型集合中添加多笔消费分账明细
     */
    @Test
    public void merchantConsumption() {
        MerchantConsumptionRequest request = new MerchantConsumptionRequest();
        request.setOutUserNo(enterprise88);
        request.setAmount(new BigDecimal("1000"));
        request.setPoundageAmount(new BigDecimal("0"));
        List<LedgerAccountInfo> infos = new ArrayList<>();
//        infos.add(new LedgerAccountInfo(userNo88, new BigDecimal("29.9")));
        infos.add(new LedgerAccountInfo(individualUserNo66, new BigDecimal("1000")));
        request.setLedgerAccountInfo(infos);
        System.out.println(new FuMinApiImpl().merchantConsumption(request));
    }


    @Test
    public void testWithdrawal() {

        WithdrawalRequest request = new WithdrawalRequest();
        request.setAmount(new BigDecimal("68"));
        request.setUserNo(individualUserNo99);
        request.setInAccountType(Dictionary.WithdrawalAccountType.TO_PRIVATE.get());
        request.setBankCardNo("6270613901000000548");
        request.setRealName("富民八");
        System.out.println(FuMinApi.getInstance().withdrawal(request));

    }






    /**
     * 电子回单下载（提现的回单）
     */
    @Test
    public void testGetElectronicReceipt() {
        ElectronicReceiptRequest request = new ElectronicReceiptRequest();

    }


    /**
     * 支付凭证下载（消费分账、分润的凭证）
     */
    @Test
    public void testGetPaymentVoucher() {
        PaymentVoucherRequest request = new PaymentVoucherRequest();

    }


    /**
     * 对账文件下载
     * 对账文件，用于对账的。对账文件会汇总一天内的交易明细，在次日生成。
     */
    @Test
    public void testGetTradeBillFile() {
        TradeBillRequest request = new TradeBillRequest();

    }

}
