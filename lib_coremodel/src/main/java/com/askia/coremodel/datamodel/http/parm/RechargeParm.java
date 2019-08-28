package com.askia.coremodel.datamodel.http.parm;

public class RechargeParm {


    /**
     * 充值金额：balance
     * 备注方式：payType
     * 1 - 百盟钱包微信充值
     * 2 - 百盟钱包支付宝充值
     * 3 - 百盟钱包银联充值
     */

    private String balance;
    private String payType;

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }
}
