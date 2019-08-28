package com.askia.coremodel.datamodel.http.parm;

public class TransferParm {

    /**
     * 转账方式：transferWay：1 2 3 4
     * <p>
     * 1 - 百盟钱包到百盟钱包：
     * <p>
     * - 对方百盟账号：adverseAccount
     * <p>
     * 2 - 百盟钱包到银行卡：
     * <p>
     * - 银行卡账号：bankAccount
     * <p>
     * - 银行卡姓名：bankName
     * <p>
     * - 银行卡开户行：openingBank
     * <p>
     * - 银行卡开户所在城市：bankCity
     * <p>
     * 3 - 百盟钱包到支付宝：
     * <p>
     * - 支付宝账号：alipayAccount
     * <p>
     * - 支付宝姓名：alipayName
     * <p>
     * 4 - 百盟钱包到微信：
     * <p>
     * <p>
     * 转账金额：money
     */

    private String transferWay;
    private String adverseAccount;

    private String bankAccount;
    private String bankName;
    private String openingBank;
    private String bankCity;

    private String alipayAccount;
    private String alipayName;

    private String money;

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getTransferWay() {
        return transferWay;
    }

    public void setTransferWay(String transferWay) {
        this.transferWay = transferWay;
    }

    public String getAdverseAccount() {
        return adverseAccount;
    }

    public void setAdverseAccount(String adverseAccount) {
        this.adverseAccount = adverseAccount;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getOpeningBank() {
        return openingBank;
    }

    public void setOpeningBank(String openingBank) {
        this.openingBank = openingBank;
    }

    public String getBankCity() {
        return bankCity;
    }

    public void setBankCity(String bankCity) {
        this.bankCity = bankCity;
    }

    public String getAlipayAccount() {
        return alipayAccount;
    }

    public void setAlipayAccount(String alipayAccount) {
        this.alipayAccount = alipayAccount;
    }

    public String getAlipayName() {
        return alipayName;
    }

    public void setAlipayName(String alipayName) {
        this.alipayName = alipayName;
    }
}
