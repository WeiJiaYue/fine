package com.mandao.sdk.response;

import java.util.List;

public class AccountDetailResponse extends Response {



    private int totalSize;                      //总记录数

    private int pageNo;

    private int pageSize;

    private List<BalanceRecord> balanceRecords; //收支明细

    public int getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<BalanceRecord> getBalanceRecords() {
        return balanceRecords;
    }

    public void setBalanceRecords(List<BalanceRecord> balanceRecords) {
        this.balanceRecords = balanceRecords;
    }


    @Override
    public String toString() {
        return super.toString() + "AccountDetailResponse{" +
                "totalSize=" + totalSize +
                ", pageNo=" + pageNo +
                ", pageSize=" + pageSize +
                ", balanceRecords=" + balanceRecords +
                '}';
    }
}
