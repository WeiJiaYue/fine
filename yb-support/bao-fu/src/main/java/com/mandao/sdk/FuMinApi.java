package com.mandao.sdk;

import com.mandao.sdk.request.*;
import com.mandao.sdk.response.Response;

public interface FuMinApi {

    String REQUEST_CHANNEL = "FMBANK";
    /**
     * 个人实名开户接口名称
     */
    String REAL_OPEN_ACCOUNT = "REAL_OPEN_ACCOUNT";

    /**
     * 用户余额接口名称
     */
    String USER_BALANCE_QUERY = "USER_BALANCE_QUERY";


    /**
     * 企业实名绑卡开户接口名称
     */
    String ENTERPRISE_REAL_OPEN_ACCOUNT = "ENTERPRISE_REAL_OPEN_ACCOUNT";


    /**
     * 网关支付
     */
    String GATEWAY_PAYMENT = "WEB";


    /**
     * 提现接口名称
     */
    String WITHDRAW = "WITHDRAW";

    /**
     * 账户明细查询名称
     */
    String ACCOUNT_DETAIL = "ACCOUNT_DETAILS_QUERY";

    /**
     * 交易订单查询接口名称
     */
    String TRANSACTION = "TRANSACTION_QUERY";

    /**
     * 消费分账/分润接口名称
     */
    String MERCHANT_CONSUMPTION = "MERCHANT_CONSUMPTION";


    /**
     * 获取余额
     *
     * @return
     */
    Response getBalance(BalanceRequest request);

    /**
     * 个人实名注册
     *
     * @return
     */
    Response individualRealOpenAccount(IndividualRealOpenAccountRequest request);


    /**
     * 企业实名开户
     *
     * @return
     */
    Response enterpriseRealOpenAccount(EnterpriseRealOpenAccountRequest request);


    /**
     * 提现
     *
     * @param request
     * @return
     */
    Response withdrawal(WithdrawalRequest request);


    /**
     * 获取账户明细
     *
     * @return
     */
    Response getAccountDetails(AccountDetailRequest request);


    /**
     * 查询交易订单状态
     *
     * @return
     */
    Response getTransactionProcessing(TransactionProcessingRequest request);


    /**
     * 网关支付
     *
     * @param request
     * @return
     */
    String gatewayPay(Request request);


    /**
     * 接口类型
     */
    enum ApiType {
        /**
         * 网关类
         */
        GATEWAY,


        /**
         * 直连
         */
        SERVICE

    }


}
