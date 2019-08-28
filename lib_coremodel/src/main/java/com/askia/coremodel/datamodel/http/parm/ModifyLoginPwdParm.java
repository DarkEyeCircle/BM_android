package com.askia.coremodel.datamodel.http.parm;

public class ModifyLoginPwdParm {

    /**
     * {
     * "password":"",
     * "newPassword":"123456"
     * }
     */
    private String password;

    private String newPassword;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

}
