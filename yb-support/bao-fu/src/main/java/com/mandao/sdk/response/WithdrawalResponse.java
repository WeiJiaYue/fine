package com.mandao.sdk.response;

public class WithdrawalResponse extends Response {

    private String requestNo;

    public String getRequestNo() {
        return requestNo;
    }

    public void setRequestNo(String requestNo) {
        this.requestNo = requestNo;
    }


    @Override
    public String toString() {
        return super.toString() + "WithdrawalResponse{" +
                "requestNo='" + requestNo + '\'' +
                '}';
    }
}
