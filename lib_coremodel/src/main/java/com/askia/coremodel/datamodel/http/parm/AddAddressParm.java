package com.askia.coremodel.datamodel.http.parm;

public class AddAddressParm {

    /**
     * String userId;用户id
     * String name; 用户姓名
     * String mobile; 手机号
     * String area; 用户所属区域
     * String address; 收货地址
     * String isDefault=1 //默认地址（1为默认地址）
     */

    private String userId; //userID 可不传
    private String name;
    private String mobile;
    private String area;
    private String address;
    private String isDefault;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(boolean isDefault) {
        if (isDefault) {
            this.isDefault = "1";
        } else {
            this.isDefault = "0";
        }

    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
