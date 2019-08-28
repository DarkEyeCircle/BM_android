package com.askia.coremodel.datamodel.database.repository;

import android.content.Context;

import com.askia.coremodel.datamodel.database.db.UserLoginData;

public class DBRepository {

    public static void init(Context context) {
        SharedPreUtil.initSharedPreference(context);
    }

    //查询用户登陆信息
    public static UserLoginData QueryUserLoginData() {
        return SharedPreUtil.getInstance().getUser();
    }

    //保存用户登陆信息
    public static void StoreUserLoginData(UserLoginData userLoginData) {
        SharedPreUtil.getInstance().putUser(userLoginData);
    }

    //设置是否显示欢迎页面
    public static void SetShowWelcomPage(boolean show) {
        UserLoginData userLoginData = SharedPreUtil.getInstance().getUser();
        userLoginData.setShowWelcomPage(show);
        SharedPreUtil.getInstance().putUser(userLoginData);
    }

    public static void SetShowWelcomPageAndVersionNum(boolean show, String versionNum) {
        UserLoginData userLoginData = SharedPreUtil.getInstance().getUser();
        userLoginData.setShowWelcomPage(show);
        userLoginData.setVersionNum(versionNum);
        SharedPreUtil.getInstance().putUser(userLoginData);
    }

    //更新用户名密码
    public static void UpdateUserNumAndPwd(String phoneNum, String passWord) {
        UserLoginData userLoginData = SharedPreUtil.getInstance().getUser();
        userLoginData.setPhoneNum(phoneNum);
        userLoginData.setUserPwd(passWord);
        SharedPreUtil.getInstance().putUser(userLoginData);
    }

    //更新用户名密码
    public static void UpdateUserPwd(String passWord) {
        UserLoginData userLoginData = SharedPreUtil.getInstance().getUser();
        userLoginData.setUserPwd(passWord);
        SharedPreUtil.getInstance().putUser(userLoginData);
    }

}
