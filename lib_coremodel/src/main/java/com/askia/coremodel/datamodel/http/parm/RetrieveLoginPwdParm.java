package com.askia.coremodel.datamodel.http.parm;

public class RetrieveLoginPwdParm {

    /**
     * {
     * "mobile":"13656634304",
     * "codeType":"retrieve.code", 固定参数
     * "password":"",
     * "smsCode":"123456"
     * }
     */
    private String mobile;
    private String codeType = "retrieve.code";
    private String password;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }

}
