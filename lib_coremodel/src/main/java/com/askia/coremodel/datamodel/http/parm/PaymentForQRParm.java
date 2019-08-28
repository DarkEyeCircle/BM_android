package com.askia.coremodel.datamodel.http.parm;

public class PaymentForQRParm {

    /**
     * String receiveUserId 接收用户id
     * String money  设置完的金额
     */

    private String receiveUserId;
    private String money;

    public String getReceiveUserId() {
        return receiveUserId;
    }

    public void setReceiveUserId(String receiveUserId) {
        this.receiveUserId = receiveUserId;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }
}
