package com.mandao.sdk.response;

public abstract class Response {



    private String respCode;

    private String respMsg;

    /**
     * see {@link com.mandao.sdk.Dictionary.TradingStatus}
     */
    private String status;


    public String getRespCode() {
        return respCode;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    public String getRespMsg() {
        return respMsg;
    }

    public void setRespMsg(String respMsg) {
        this.respMsg = respMsg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Response{" +
                "respCode='" + respCode + '\'' +
                ", respMsg='" + respMsg + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
