package com.askia.coremodel.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableField;

import com.apkfuns.logutils.LogUtils;
import com.askia.coremodel.GlobalUserDataStore;
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

public class SettingSecurityPwdViewModel extends ViewModel {

    private MutableLiveData<BaseResponseData> smsLiveData = new MutableLiveData<>();
    private MutableLiveData<BaseResponseData> setPwdLiveData = new MutableLiveData<>();
    private final CompositeDisposable mDisposable = new CompositeDisposable();
    //页面输入的数据
    private PageData pageData;

    public SettingSecurityPwdViewModel() {
        pageData = new PageData();
    }

    public PageData getPageData() {
        return pageData;
    }

    public MutableLiveData<BaseResponseData> getSmsLiveData() {
        return smsLiveData;
    }

    public MutableLiveData<BaseResponseData> getSetPwdLiveData() {
        return setPwdLiveData;
    }

    //获取验证码
    public void senSmsCode() {
        if (!NetUtils.isNetConnected()) {
            smsLiveData.setValue(null);
            return;
        }
        GankDataRepository.SendSmsCodeForSetSecurityPwd(pageData)
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

    //设置支付密码
    public void settingSecurityPwd() {
        if (!NetUtils.isNetConnected()) {
            setPwdLiveData.setValue(null);
            return;
        }
        GankDataRepository.SettingSecurityPwd(pageData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .safeSubscribe(new Observer<BaseResponseData>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onNext(BaseResponseData responseData) {
                        setPwdLiveData.setValue(responseData);
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
                        setPwdLiveData.setValue(responseData);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    public class PageData {

        public PageData() {
            //防止空指针
            phoneNum.set(GlobalUserDataStore.getInstance().getMobile());
            passWord.set("");
            verificationCode.set("");
            confirmPwd.set("");
        }

        //手机号码
        private ObservableField<String> phoneNum = new ObservableField<>();
        //密码
        private ObservableField<String> passWord = new ObservableField<>();
        //密码
        private ObservableField<String> confirmPwd = new ObservableField<>();
        //验证码
        private ObservableField<String> verificationCode = new ObservableField<>();

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

        public ObservableField<String> getConfirmPwd() {
            return confirmPwd;
        }

        public void setConfirmPwd(ObservableField<String> confirmPwd) {
            this.confirmPwd = confirmPwd;
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mDisposable.clear();
    }

}
