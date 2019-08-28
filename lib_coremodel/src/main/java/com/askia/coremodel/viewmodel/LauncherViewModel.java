package com.askia.coremodel.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.askia.coremodel.datamodel.database.db.UserLoginData;
import com.askia.coremodel.datamodel.database.repository.DBRepository;
import com.askia.coremodel.util.DeviceUtils;

public class LauncherViewModel extends AndroidViewModel {

    //生命周期观察的数据
    private MutableLiveData<UserLoginData> mLiveUserLoginData = new MutableLiveData<>();

    public LauncherViewModel(@NonNull Application application) {
        super(application);
        init();
    }

    public MutableLiveData<UserLoginData> getmLiveUserLoginData() {
        return mLiveUserLoginData;
    }

    //保存用户登陆数据
    public void storeUserLoginData(UserLoginData userLoginData) {
        DBRepository.StoreUserLoginData(userLoginData);
    }

    //获取用户登陆数据
    public UserLoginData getUserLoginDataFromDB() {
        return DBRepository.QueryUserLoginData();
    }

    //初始化
    private void init() {
        UserLoginData userLoginData = getUserLoginDataFromDB();
        if (userLoginData == null) { //用户第一次安装
            userLoginData = new UserLoginData();
            userLoginData.setDeviceRooted(DeviceUtils.isDeviceRooted());
            userLoginData.setDevMac(DeviceUtils.getMacAddress());
            userLoginData.setDevModel(DeviceUtils.getModel());
            userLoginData.setShowWelcomPage(true);
            userLoginData.setManufacturer(DeviceUtils.getManufacturer());
            userLoginData.setSDKVersionName(DeviceUtils.getSDKVersionName());
            try {
                userLoginData.setVersionNum(getApplication().getPackageManager().getPackageInfo(
                        getApplication().getPackageName(), 0).versionName);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            userLoginData.setPhoneNum("");
            userLoginData.setUserPwd("");
            storeUserLoginData(userLoginData);
        } else {
            //判断版本迭代
            try {
                String versionName = getApplication().getPackageManager().getPackageInfo(
                        getApplication().getPackageName(), 0).versionName;
                if (!TextUtils.equals(versionName, userLoginData.getVersionNum())) {

                    DBRepository.SetShowWelcomPageAndVersionNum(true, versionName);
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        //setValue必须发生在主线程 如果当前线程是子线程可以使用postValue
        mLiveUserLoginData.setValue(getUserLoginDataFromDB());
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
