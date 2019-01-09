package com.mandao.sdk.response;

import java.math.BigDecimal;

public class TransactionProcessingResponse extends Response {


    private BigDecimal amount;  //交易金额

    private String createTime;  //交易创建时间yyyy-MM-dd HH：mm：ss

    private String finishTime;  //交易完成时间yyyy-MM-dd HH：mm：ss

    private String requestNo;   //请求流水号【备注：此处为交易时请求流水号


    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public String getRequestNo() {
        return requestNo;
    }

    public void setRequestNo(String requestNo) {
        this.requestNo = requestNo;
    }


    @Override
    public String toString() {
        return super.toString() + "TransactionProcessingResponse{" +
                "amount=" + amount +
                ", createTime='" + createTime + '\'' +
                ", finishTime='" + finishTime + '\'' +
                ", requestNo='" + requestNo + '\'' +
                '}';
    }
}

