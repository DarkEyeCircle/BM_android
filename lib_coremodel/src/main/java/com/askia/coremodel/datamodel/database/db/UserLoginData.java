package com.askia.coremodel.datamodel.database.db;

import java.io.Serializable;

/**
 * 用户登陆信息
 * HHY
 * ObservableField 目的为实现数据改变自动改变View
 */

public class UserLoginData implements Serializable {
    //设备mac
    private String devMac;
    //设备型号
    private String devModel;
    //是否显示欢迎页面
    private boolean showWelcomPage;
    //设备系统版本号
    private String SDKVersionName;
    //程序版本号
    private String versionNum;
    //设备是否 rooted
    private boolean isDeviceRooted;
    //设备厂商
    private String manufacturer;
    //用户名
    private String phoneNum = "";
    //密码
    private String userPwd = "";

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public String getDevMac() {
        return devMac;
    }

    public void setDevMac(String devMac) {
        this.devMac = devMac;
    }

    public String getDevModel() {
        return devModel;
    }

    public void setDevModel(String devModel) {
        this.devModel = devModel;
    }

    public boolean isShowWelcomPage() {
        return showWelcomPage;
    }

    public void setShowWelcomPage(boolean showWelcomPage) {
        this.showWelcomPage = showWelcomPage;
    }

    public String getVersionNum() {
        return versionNum;
    }

    public void setVersionNum(String versionNum) {
        this.versionNum = versionNum;
    }

    public String getSDKVersionName() {
        return SDKVersionName;
    }

    public void setSDKVersionName(String SDKVersionName) {
        this.SDKVersionName = SDKVersionName;
    }

    public boolean isDeviceRooted() {
        return isDeviceRooted;
    }

    public void setDeviceRooted(boolean deviceRooted) {
        isDeviceRooted = deviceRooted;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    @Override
    public String toString() {
        return "UserLoginData{" +
                "devMac='" + devMac + '\'' +
                ", devModel='" + devModel + '\'' +
                ", showWelcomPage=" + showWelcomPage +
                ", SDKVersionName='" + SDKVersionName + '\'' +
                ", versionNum='" + versionNum + '\'' +
                ", isDeviceRooted=" + isDeviceRooted +
                ", manufacturer='" + manufacturer + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                ", userPwd='" + userPwd + '\'' +
                '}';
    }
}


