package com.askia.coremodel.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableField;

import com.askia.coremodel.datamodel.http.entities.BaseResponseData;
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

public class BindNewMobileViewModel extends ViewModel {

    private MutableLiveData<BaseResponseData> smsLiveData = new MutableLiveData<>();
    private MutableLiveData<User> bindMoileLiveData = new MutableLiveData<>();
    private final CompositeDisposable mDisposable = new CompositeDisposable();
    //页面输入的数据
    private PageData pageData;

    public BindNewMobileViewModel() {
        pageData = new PageData();
    }

    public PageData getPageData() {
        return pageData;
    }

    public MutableLiveData<BaseResponseData> getSmsLiveData() {
        return smsLiveData;
    }

    public MutableLiveData<User> getBindMoileLiveData() {
        return bindMoileLiveData;
    }

    //获取验证码
    public void senSmsCode() {
        if (!NetUtils.isNetConnected()) {
            smsLiveData.setValue(null);
            return;
        }
        GankDataRepository.SendSmsForChangeNewMobile(pageData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .safeSubscribe(new Observer<BaseResponseData>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onNext(BaseResponseData responseData) {
                        smsLiveData.setValue(responseData);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        BaseResponseData responseData = new BaseResponseData();
                        if (e instanceof SocketTimeoutException) {
                            responseData.setCode(ResponseCode.ConnectTimeOut);
                            responseData.setMsg("连接超时，请重试");
                        } else {
                            responseData.setCode(ResponseCode.ServerNotResponding);
                            responseData.setMsg("服务器无响应，请重试");
                        }
                        smsLiveData.setValue(responseData);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    //验证手机号
    public void checkMobile() {
        if (!NetUtils.isNetConnected()) {
            bindMoileLiveData.setValue(null);
            return;
        }
        GankDataRepository.BindNewMobile(pageData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .safeSubscribe(new Observer<User>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onNext(User responseData) {
                        bindMoileLiveData.setValue(responseData);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        User userInfo = new User();
                        userInfo.setCode(ResponseCode.ServerNotResponding);
                        userInfo.setMsg("服务器无响应");
                        bindMoileLiveData.setValue(userInfo);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    public class PageData {

        public PageData() {
            //防止空指针
            phoneNum.set("");
            verificationCode.set("");
        }

        //手机号码
        private ObservableField<String> phoneNum = new ObservableField<>();
        //验证码
        private ObservableField<String> verificationCode = new ObservableField<>();

        public ObservableField<String> getPhoneNum() {
            return phoneNum;
        }

        public void setPhoneNum(ObservableField<String> phoneNum) {
            this.phoneNum = phoneNum;
        }

        public ObservableField<String> getVerificationCode() {
            return verificationCode;
        }

        public void setVerificationCode(ObservableField<String> verificationCode) {
            this.verificationCode = verificationCode;
        }

    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mDisposable.clear();
    }

}
