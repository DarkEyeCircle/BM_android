package com.askia.coremodel.datamodel.http.parm;

public class ModifySecurityPwdParm {

    /**
     * "securityPassword":"",
     * "newSecurityPassword":"123456"
     */
    private String securityPassword;

    private String newSecurityPassword;

    public String getSecurityPassword() {
        return securityPassword;
    }

    public void setSecurityPassword(String securityPassword) {
        this.securityPassword = securityPassword;
    }

    public String getNewSecurityPassword() {
        return newSecurityPassword;
    }

    public void setNewSecurityPassword(String newSecurityPassword) {
        this.newSecurityPassword = newSecurityPassword;
    }
}
