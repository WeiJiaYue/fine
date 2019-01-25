package com.mandao.sdk.request;


import com.mandao.sdk.RequiredParam;
import java.math.BigDecimal;
import java.util.List;

/**
 * 消费分账/分润请求参数
 */
public class MerchantConsumptionRequest extends Request {


    @RequiredParam
    private String outUserNo;                           //出账方用户编号

    private String inUserNo;                            //入账方用户编号

    @RequiredParam
    private BigDecimal amount;                          //交易金额

    @RequiredParam
    private boolean guarantee = false;                  //false非担保(默认) true:担保

    @RequiredParam
    private BigDecimal poundageAmount;                  //手续费

    @RequiredParam
    private List<LedgerAccountInfo> ledgerAccountInfo;  //分账信息（json数组字符串）

    private String feeSplittingInfo;                    //分润信息（json数组字符串）

    private String remark;                              //交易备注


    public String getOutUserNo() {
        return outUserNo;
    }

    public void setOutUserNo(String outUserNo) {
        this.outUserNo = outUserNo;
    }


    public String getInUserNo() {
        return inUserNo;
    }

    public void setInUserNo(String inUserNo) {
        this.inUserNo = inUserNo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public boolean isGuarantee() {
        return guarantee;
    }

    public void setGuarantee(boolean guarantee) {
        this.guarantee = guarantee;
    }

    public BigDecimal getPoundageAmount() {
        return poundageAmount;
    }

    public void setPoundageAmount(BigDecimal poundageAmount) {
        this.poundageAmount = poundageAmount;
    }


    public List<LedgerAccountInfo> getLedgerAccountInfo() {
        return ledgerAccountInfo;
    }

    public void setLedgerAccountInfo(List<LedgerAccountInfo> ledgerAccountInfos) {
        this.ledgerAccountInfo = ledgerAccountInfos;
    }

    public String getFeeSplittingInfo() {
        return feeSplittingInfo;
    }

    public void setFeeSplittingInfo(String feeSplittingInfo) {
        this.feeSplittingInfo = feeSplittingInfo;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }


}
