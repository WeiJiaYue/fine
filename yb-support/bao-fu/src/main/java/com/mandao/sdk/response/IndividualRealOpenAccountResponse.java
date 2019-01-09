package com.mandao.sdk.response;

public class IndividualRealOpenAccountResponse extends Response {


    private String requestNo;

    private String userNo;

    public String getRequestNo() {
        return requestNo;
    }

    public void setRequestNo(String requestNo) {
        this.requestNo = requestNo;
    }

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    @Override
    public String toString() {
        return super.toString()+
                "IndividualRealOpenAccountResponse{" +
                "requestNo='" + requestNo + '\'' +
                ", userNo='" + userNo + '\'' +
                '}';
    }
}
