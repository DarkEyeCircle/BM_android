package com.askia.coremodel.datamodel.http.parm;


public class RegisterParm {

    /* */
    /**
     * "mobile":"13656634308",
     * "codeType":"register.code",固定参数
     * "password":"crazy.6688",
     * "smsCode":"12345",
     * "introducer":"q1w2e3r4" 邀请码非必填
     */
    private String mobile;
    private String codeType = "register.code";
    private String password;
    private String smsCode;
    private String introducer;

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

    public String getIntroducer() {
        return introducer;
    }

    public void setIntroducer(String introducer) {
        this.introducer = introducer;
    }
}
