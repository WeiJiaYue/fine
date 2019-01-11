package com.mandao.sdk;

import com.mandao.sdk.request.*;
import com.mandao.sdk.response.*;
import com.mandao.sign.Signer;
import com.mandao.http.HttpUtil;
import com.radarwin.framework.util.JsonUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.mandao.sdk.ConfigReader.*;

public class FuMinApiImpl implements FuMinApi {


    @Override
    public Response getBalance(BalanceRequest request) {
        return JsonUtil.jsonToObject(execute(USER_BALANCE_QUERY, request), BalanceResponse.class);
    }


    @Override
    public Response individualRealOpenAccount(IndividualRealOpenAccountRequest request) {
        return JsonUtil.jsonToObject(execute(REAL_OPEN_ACCOUNT, request), IndividualRealOpenAccountResponse.class);
    }


    @Override
    public Response enterpriseRealOpenAccount(EnterpriseRealOpenAccountRequest request) {
        return JsonUtil.jsonToObject(execute(ENTERPRISE_REAL_OPEN_ACCOUNT, request), EnterpriseRealOpenAccountResponse.class);
    }


    @Override
    public String gatewayPay(GatewayPaymentRequest request) {
        request.setApiType(ApiType.GATEWAY);
        return execute(GATEWAY_PAYMENT, request);
    }


    @Override
    public Response withdrawal(WithdrawalRequest request) {
        return JsonUtil.jsonToObject(execute(WITHDRAW, request), WithdrawalResponse.class);
    }


    @Override
    public Response getAccountDetails(AccountDetailRequest request) {
        return JsonUtil.jsonToObject(execute(ACCOUNT_DETAIL, request), AccountDetailResponse.class);
    }


    @Override
    public Response getTransactionProcessing(TransactionProcessingRequest request) {
        return JsonUtil.jsonToObject(execute(TRANSACTION, request), TransactionProcessingResponse.class);
    }


    @Override
    public Response merchantConsumption(MerchantConsumptionRequest request) {
        return JsonUtil.jsonToObject(execute(MERCHANT_CONSUMPTION, request), MerchantConsumptionResponse.class);
    }


    @Override
    public Response getElectronicReceipt(ElectronicReceiptRequest request) {
        return JsonUtil.jsonToObject(execute(ELECTRONIC_RECEIPT, request), ElectronicReceiptResponse.class);
    }

    @Override
    public Response getPaymentVoucher(PaymentVoucherRequest request) {
        return JsonUtil.jsonToObject(execute(PAYMENTVOUCHER_DOWNLOAD, request), PaymentVoucherResponse.class);
    }


    @Override
    public Response getTradeBillFile(TradeBillRequest request) {
        return JsonUtil.jsonToObject(execute(DOWNLOAD_TRADEBILL_FILE, request), TradeBillResponse.class);
    }


    protected String execute(String api, Request request) {
        request.validateRequiredParams(request.getClass(), new ArrayList<String>());
        String reqData = JsonUtil.objectToJson(request);
        /* 业务报文加密签名 */
        String signature = Signer.sign(reqData, PRIMARY_KEY_PATH, PRIMARY_KEY_PWD);
        /* 组装请求报文 */
        Map<String, String> postParam = new HashMap<>();
        postParam.put("serviceName", api);// 接口名称
        postParam.put("platformNo", PLATFORM_NO);// 平台编号
        postParam.put("reqData", reqData);// 业务数据报文，JSON 格式
        postParam.put("certSerial", CERT_SERIAL);// 证书序号
        postParam.put("signature", signature);// 签名结果

        if (request.getApiType().equals(ApiType.SERVICE)) {
            return HttpUtil.requestForm(API_SERVICE_URL, postParam, PUBLIC_KEY_PATH);
        } else if (request.getApiType().equals(ApiType.GATEWAY)) {
            return HttpUtil.htmlForm(API_GATEWAY_URL, postParam);
        }
        return null;
    }


}
