package com.askia.coremodel.datamodel.http.parm;

public class BindNewMobileParm {

    /**
     * {
     * "mobile":"13656634304",
     * "codeType":"determineMobile.code", 固定参数
     * "smsCode":"123456"
     * }
     */

    private String mobile;
    private String codeType = "determineMobile.code";
    private String smsCode;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCodeType() {
        return codeType;
    }

    public void setCodeType(String codeType) {
        this.codeType = codeType;
    }

    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }
}
