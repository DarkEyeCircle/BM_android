package com.askia.coremodel.datamodel.http.parm;

public class BindUserCardParm {

    /**
     * {
     * "mobile": "13656634306",
     * "openingBank": "中国招商银行",
     * "codeType":"bindingBank.code", 固定参数
     * "cardNo": "6215591813002418111",
     * "smsCode":"123456"
     * "city":"杭州市"
     * }
     */

    private String mobile;
    private String openingBank;
    private String codeType = "bindingBank.code";
    private String cardNo;
    private String smsCode;
    private String city;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getOpeningBank() {
        return openingBank;
    }

    public void setOpeningBank(String openingBank) {
        this.openingBank = openingBank;
    }

    public String getCodeType() {
        return codeType;
    }

    public void setCodeType(String codeType) {
        this.codeType = codeType;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
