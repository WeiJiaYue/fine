package com.mandao.sdk.response;



import com.mandao.sdk.Dictionary;

import java.math.BigDecimal;

/**
 * 余额明细
 */
public class BalanceResponse extends Response {


    private BigDecimal sumBalance;              //合计金额

    private BigDecimal sumFrozenAmount;         //合计冻结金额

    private Dictionary.AccountState accountState;//余额账户状态

    private BigDecimal balance;                 //余额账户余额

    private BigDecimal availableBalance;        //余额账户可用金额=balance-frozenAmount

    private BigDecimal frozenAmount;            //余额账户冻结金额

    private String innerAccountState;           //待请账户状态

    private BigDecimal innerBalance;            //待清分账余额

    private BigDecimal innerFrozenAmount;       //待清分账冻结金额

    private BigDecimal innnerAvailableBalance;  //待清分账可用余额=innerBalance-innerFrozenAmount


    public BigDecimal getSumBalance() {
        return sumBalance;
    }

    public void setSumBalance(BigDecimal sumBalance) {
        this.sumBalance = sumBalance;
    }

    public BigDecimal getSumFrozenAmount() {
        return sumFrozenAmount;
    }

    public void setSumFrozenAmount(BigDecimal sumFrozenAmount) {
        this.sumFrozenAmount = sumFrozenAmount;
    }

    public Dictionary.AccountState getAccountState() {
        return accountState;
    }

    public void setAccountState(Dictionary.AccountState accountState) {
        this.accountState = accountState;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(BigDecimal availableBalance) {
        this.availableBalance = availableBalance;
    }

    public BigDecimal getFrozenAmount() {
        return frozenAmount;
    }

    public void setFrozenAmount(BigDecimal frozenAmount) {
        this.frozenAmount = frozenAmount;
    }

    public String getInnerAccountState() {
        return innerAccountState;
    }

    public void setInnerAccountState(String innerAccountState) {
        this.innerAccountState = innerAccountState;
    }

    public BigDecimal getInnerBalance() {
        return innerBalance;
    }

    public void setInnerBalance(BigDecimal innerBalance) {
        this.innerBalance = innerBalance;
    }

    public BigDecimal getInnerFrozenAmount() {
        return innerFrozenAmount;
    }

    public void setInnerFrozenAmount(BigDecimal innerFrozenAmount) {
        this.innerFrozenAmount = innerFrozenAmount;
    }

    public BigDecimal getInnnerAvailableBalance() {
        return innnerAvailableBalance;
    }

    public void setInnnerAvailableBalance(BigDecimal innnerAvailableBalance) {
        this.innnerAvailableBalance = innnerAvailableBalance;
    }


    @Override
    public String toString() {
        return super.toString()+"BalanceResponse{" +
                "sumBalance=" + sumBalance +
                ", sumFrozenAmount=" + sumFrozenAmount +
                ", accountState='" + accountState + '\'' +
                ", balance=" + balance +
                ", availableBalance=" + availableBalance +
                ", frozenAmount=" + frozenAmount +
                ", innerAccountState='" + innerAccountState + '\'' +
                ", innerBalance=" + innerBalance +
                ", innerFrozenAmount=" + innerFrozenAmount +
                ", innnerAvailableBalance=" + innnerAvailableBalance +
                '}';
    }
}
