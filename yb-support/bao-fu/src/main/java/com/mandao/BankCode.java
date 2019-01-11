package com.mandao;

import com.alibaba.fastjson.JSONObject;

public enum BankCode {


    重庆富民银行("重庆富民银行", "CQFMB"),
    交通银行("交通银行", "BCM"),
    江苏省农村信用社联合社("江苏省农村信用社联合社", "JSNX"),
    江苏农信社("交通银行", "JSNX"),
    江苏农村商业银行("交通银行", "JSNX"),
    桂林市商业银行("桂林市商业银行", "BOGL"),
    桂林银行("桂林市商业银行", "BOGL");


    private String bankName;

    private String swiftCode;

    BankCode(String bankName, String swiftCode) {
        this.bankName = bankName;
        this.swiftCode = swiftCode;
    }

    public final String getSwiftCode() {
        return swiftCode;
    }


    final static String SWIFT_URL = "https://ccdcapi.alipay.com/validateAndCacheCardInfo.json?_input_charset=utf-8&cardNo=bankCardNo&cardBinCheck=true";

    public static String getSwiftCodeFromAliPay(String bankCardNo) {
        String url = SWIFT_URL.replace("bankCardNo", bankCardNo);
        String result = RestClient.doGet(url);
        JSONObject jo = JSONObject.parseObject(result);
        if (jo.containsKey("bank")) {
            return jo.getString("bank");
        }
        return null;
    }

    public static String toLocaleSwift(String bankCode) {
        return bankCode.equals("COMM") ? 交通银行.swiftCode : bankCode;
    }

    public static void main(String[] args) {
        System.out.println(getSwiftCodeFromAliPay("6222620210001358695"));
    }


}
