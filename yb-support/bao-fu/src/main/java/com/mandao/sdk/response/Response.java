package com.mandao.sdk.response;

import com.mandao.sdk.Dictionary;

public abstract class Response {



    private String respCode;

    private String respMsg;

    /**
     * see {@link com.mandao.sdk.Dictionary.TradingStatus}
     */
    private Dictionary.TradingStatus status;


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

    public Dictionary.TradingStatus getStatus() {
        return status;
    }

    public void setStatus(Dictionary.TradingStatus status) {
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
