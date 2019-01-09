package com.mandao.sdk.request;

import com.mandao.sdk.RequiredParam;


/**
 * 账户收支明细请求参数
 */
public class AccountDetailRequest extends Request {

    @RequiredParam
    private Integer pageNo;         //当前页（正整数）

    @RequiredParam
    private Integer pageSize;       //每页显示记录数（正整数，建议每页15条）

    @RequiredParam
    private String tradeStartDate;  //交易查询开始时间yyyy-MM-dd

    @RequiredParam
    private String tradeEndDate;    //交易查询结束时间yyyy-MM-dd


    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getTradeStartDate() {
        return tradeStartDate;
    }

    public void setTradeStartDate(String tradeStartDate) {
        this.tradeStartDate = tradeStartDate;
    }

    public String getTradeEndDate() {
        return tradeEndDate;
    }

    public void setTradeEndDate(String tradeEndDate) {
        this.tradeEndDate = tradeEndDate;
    }
}
