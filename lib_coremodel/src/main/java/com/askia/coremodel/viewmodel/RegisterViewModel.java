package com.askia.coremodel.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableField;

import com.askia.coremodel.datamodel.http.entities.BaseResponseData;
import com.askia.coremodel.datamodel.http.entities.ResponseCode;
import com.askia.coremodel.datamodel.http.repository.GankDataRepository;
import com.askia.coremodel.util.NetUtils;

import java.net.SocketTimeoutException;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RegisterViewModel extends ViewModel {

    private PageData pageData = new PageData();
    private MutableLiveData<BaseResponseData> registLiveData = new MutableLiveData<>();
    private MutableLiveData<BaseResponseData> smsLiveData = new MutableLiveData<>();
    private MutableLiveData<BaseResponseData> alphaLiveData = new MutableLiveData<>();
    private final CompositeDisposable mDisposable = new CompositeDisposable();

    public PageData getPageData() {
        return pageData;
    }

    public MutableLiveData<BaseResponseData> getRegistLiveData() {
        return registLiveData;
    }

    public MutableLiveData<BaseResponseData> getSmsLiveData() {
        return smsLiveData;
    }

    public MutableLiveData<BaseResponseData> getAlphaLiveData() {
        return alphaLiveData;
    }

    public void getAlpha() {
        if (!NetUtils.isNetConnected()) {
            alphaLiveData.setValue(null);
            return;
        }
        GankDataRepository.GetAlpha()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .safeSubscribe(new Observer<BaseResponseData>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onNext(BaseResponseData responseData) {
                        alphaLiveData.setValue(responseData);
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
                        alphaLiveData.setValue(responseData);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    //获取验证码
    public void senSmsCode() {
        if (!NetUtils.isNetConnected()) {
            registLiveData.setValue(null);
            return;
        }
        GankDataRepository.sendSmsCodeRepositoryForRegist(pageData)
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

    //注册账户
    public void registAccount(String alpha) {
        if (!NetUtils.isNetConnected()) {
            registLiveData.setValue(null);
            return;
        }
        GankDataRepository.registUserAccount(pageData, alpha)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .safeSubscribe(new Observer<BaseResponseData>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onNext(BaseResponseData responseData) {
                        registLiveData.setValue(responseData);
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
                        registLiveData.setValue(responseData);
                    }

                    @Override
                    public void onComplete() {
                    }
                });

    }

    //注册页面数据信息存放
    public class PageData {

        public PageData() {
            //防止空指针
            phoneNum.set("");
            passWord.set("");
            verificationCode.set("");
            inviteCode.set("");
        }

        private String realPhoneNum;
        //手机号码
        private ObservableField<String> phoneNum = new ObservableField<>();
        //密码
        private ObservableField<String> passWord = new ObservableField<>();
        //验证码
        private ObservableField<String> verificationCode = new ObservableField<>();
        //邀请码
        private ObservableField<String> inviteCode = new ObservableField<>();

        public String getRealPhoneNum() {
            return realPhoneNum;
        }

        public void setRealPhoneNum(String realPhoneNum) {
            this.realPhoneNum = realPhoneNum;
        }

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

        public ObservableField<String> getVerificationCode() {
            return verificationCode;
        }

        public void setVerificationCode(ObservableField<String> verificationCode) {
            this.verificationCode = verificationCode;
        }

        public ObservableField<String> getInviteCode() {
            return inviteCode;
        }

        public void setInviteCode(ObservableField<String> inviteCode) {
            this.inviteCode = inviteCode;
        }

    }
}
