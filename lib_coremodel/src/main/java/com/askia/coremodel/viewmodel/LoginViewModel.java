package com.askia.coremodel.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableField;

import com.apkfuns.logutils.LogUtils;
import com.askia.coremodel.datamodel.database.db.UserLoginData;
import com.askia.coremodel.datamodel.database.repository.DBRepository;
import com.askia.coremodel.datamodel.http.entities.ResponseCode;
import com.askia.coremodel.datamodel.http.entities.User;
import com.askia.coremodel.datamodel.http.repository.GankDataRepository;
import com.askia.coremodel.util.NetUtils;

import java.net.SocketTimeoutException;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LoginViewModel extends ViewModel {

    private final CompositeDisposable mDisposable = new CompositeDisposable();
    //生命周期观察的数据
    private MutableLiveData<User> mLiveLoginData;
    private PageData pageData;

    public LiveData<User> getLiveLoginData() {
        return mLiveLoginData;
    }

    //初始化时默认从数据库查询用户登陆信息
    public LoginViewModel() {
        mLiveLoginData = new MutableLiveData<>();
        pageData = new PageData();
        //初始化登陆信息
        UserLoginData userLoginData = getUserLoginDataFromDB();
        pageData.getPhoneNum().set(userLoginData.getPhoneNum());
//        pageData.getPassWord().set(userLoginData.getUserPwd());
        pageData.getPassWord().set("");
    }


    public PageData getPageData() {
        return pageData;
    }

    //从数据库获取登陆数据
    private UserLoginData getUserLoginDataFromDB() {
        return DBRepository.QueryUserLoginData();
    }

    //发起登陆请求
    public void doLogin() {
        if (!NetUtils.isNetConnected()) {
            mLiveLoginData.setValue(null);
            return;
        }
        GankDataRepository.getLoginDataRepository(pageData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .safeSubscribe(new Observer<User>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onNext(User user) {
                        //保存用户密码
                        String phoneNum = pageData.getPhoneNum().get().replaceAll(" ", "");
                        DBRepository.UpdateUserNumAndPwd(phoneNum, pageData.getPassWord().get());
                        mLiveLoginData.setValue(user);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        User user = new User();
                        if (e instanceof SocketTimeoutException) {
                            user.setCode(ResponseCode.ConnectTimeOut);
                            user.setMsg("连接超时，请重试");
                        } else {
                            user.setCode(ResponseCode.ServerNotResponding);
                            user.setMsg("服务器无响应，请重试");
                        }
                        mLiveLoginData.setValue(user);
                    }

                    @Override
                    public void onComplete() {
                        LogUtils.d("onComplete");
                    }
                });
    }

    /*页面内容数据*/
    public class PageData {

        private ObservableField<String> phoneNum = new ObservableField<>();
        private ObservableField<String> passWord = new ObservableField<>();

        public ObservableField<String> getPhoneNum() {
            return phoneNum;
        }

        public void setPhoneNum(ObservableField<String> phoneNum) {
            this.phoneNum = phoneNum;
        }

        public ObservableField<String> getPassWord() {
            return passWord;
        }

        public void setPassWord(ObservableField<String> passWord) {
            this.passWord = passWord;
        }
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        LogUtils.d("=======LoginViewModel--onCleared=========");
        mDisposable.clear();
    }

}
