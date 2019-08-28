package com.askia.coremodel.datamodel.http.parm;

public class BuyMemberParm {

    /**
     * id:会员包id
     * payType：支付类型
     * 100270支付宝
     * 100260微信
     */

    private String id;
    private int payType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }
}
