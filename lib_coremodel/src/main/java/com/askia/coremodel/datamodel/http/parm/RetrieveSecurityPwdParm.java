package com.askia.coremodel.datamodel.http.parm;

public class RetrieveSecurityPwdParm {

    /**
     * "mobile":"13656634304",
     * "codeType":"retrieveSecurityPassword.code", 固定参数
     * "securityPassword":"",
     * "smsCode":"123456"
     */
    private String mobile;
    private String codeType = "retrieveSecurityPassword.code";
    private String securityPassword;
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

    public String getSecurityPassword() {
        return securityPassword;
    }

    public void setSecurityPassword(String securityPassword) {
        this.securityPassword = securityPassword;
    }

    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }
}
