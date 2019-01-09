package com.mandao.sdk.response;

public class MerchantConsumptionResponse extends Response {

    private String requestNo;

    public String getRequestNo() {
        return requestNo;
    }

    public void setRequestNo(String requestNo) {
        this.requestNo = requestNo;
    }


    @Override
    public String toString() {
        return super.toString() + "MerchantConsumptionResponse{" +
                "requestNo='" + requestNo + '\'' +
                '}';
    }
}
