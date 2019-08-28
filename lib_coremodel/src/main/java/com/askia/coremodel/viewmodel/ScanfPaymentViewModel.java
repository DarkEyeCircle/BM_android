package com.askia.coremodel.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableField;

import com.askia.coremodel.datamodel.http.entities.BaseResponseData;
import com.askia.coremodel.datamodel.http.entities.ResponseCode;
import com.askia.coremodel.datamodel.http.entities.ScanfPaymentData;
import com.askia.coremodel.datamodel.http.entities.UserInfoByQRCode;
import com.askia.coremodel.datamodel.http.repository.GankDataRepository;
import com.askia.coremodel.util.NetUtils;

import java.net.SocketTimeoutException;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ScanfPaymentViewModel extends ViewModel {

    private MutableLiveData<BaseResponseData> checkSecurityPwdLiveData = new MutableLiveData<>();
    private MutableLiveData<ScanfPaymentData> payLiveData = new MutableLiveData<>();
    private MutableLiveData<UserInfoByQRCode> userInfoLiveData = new MutableLiveData<>();
    private PageData pageData = new PageData();
    private final CompositeDisposable mDisposable = new CompositeDisposable();

    public MutableLiveData<BaseResponseData> getCheckSecurityPwdLiveData() {
        return checkSecurityPwdLiveData;
    }

    public MutableLiveData<UserInfoByQRCode> getUserInfoLiveData() {
        return userInfoLiveData;
    }

    public MutableLiveData<ScanfPaymentData> getPayLiveData() {
        return payLiveData;
    }

    public PageData getPageData() {
        return pageData;
    }

    //验证安全支付密码
    public void checkSecurityPwd(String pwd) {
        if (!NetUtils.isNetConnected()) {
            checkSecurityPwdLiveData.setValue(null);
            return;
        }
        GankDataRepository.VerifySecurityPassword(pwd)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .safeSubscribe(new Observer<BaseResponseData>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onNext(BaseResponseData responseData) {
                        checkSecurityPwdLiveData.setValue(responseData);
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
                        checkSecurityPwdLiveData.setValue(responseData);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    //获取收款方用户信息（头像，昵称）
    public void getUserInfoByCode() {
        if (!NetUtils.isNetConnected()) {
            userInfoLiveData.setValue(null);
            return;
        }
        GankDataRepository.GetUserInfoByQRCode(pageData.getCodeId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .safeSubscribe(new Observer<UserInfoByQRCode>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onNext(UserInfoByQRCode responseData) {
                        userInfoLiveData.setValue(responseData);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        UserInfoByQRCode responseData = new UserInfoByQRCode();
                        if (e instanceof SocketTimeoutException) {
                            responseData.setCode(ResponseCode.ConnectTimeOut);
                            responseData.setMsg("连接超时，请重试");
                        } else {
                            responseData.setCode(ResponseCode.ServerNotResponding);
                            responseData.setMsg("服务器无响应，请重试");
                        }
                        userInfoLiveData.setValue(responseData);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    //付款
    public void doPay() {
        if (!NetUtils.isNetConnected()) {
            payLiveData.setValue(null);
            return;
        }
        GankDataRepository.PaymentForQR(pageData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .safeSubscribe(new Observer<ScanfPaymentData>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onNext(ScanfPaymentData responseData) {
                        payLiveData.setValue(responseData);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        ScanfPaymentData responseData = new ScanfPaymentData();
                        if (e instanceof SocketTimeoutException) {
                            responseData.setCode(ResponseCode.ConnectTimeOut);
                            responseData.setMsg("连接超时，请重试");
                        } else {
                            responseData.setCode(ResponseCode.ServerNotResponding);
                            responseData.setMsg("服务器无响应，请重试");
                        }
                        payLiveData.setValue(responseData);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    public class PageData {

        public PageData() {
        }

        private ObservableField<String> money = new ObservableField<>();
        private ObservableField<String> nickName = new ObservableField<>();
        private String codeId;
        private String imgHead;
        private String receiveUserId;

        public ObservableField<String> getMoney() {
            return money;
        }

        public void setMoney(ObservableField<String> money) {
            this.money = money;
        }

        public ObservableField<String> getNickName() {
            return nickName;
        }

        public void setNickName(ObservableField<String> nickName) {
            this.nickName = nickName;
        }

        public String getCodeId() {
            return codeId;
        }

        public void setCodeId(String codeId) {
            this.codeId = codeId;
        }

        public String getImgHead() {
            return imgHead;
        }

        public void setImgHead(String imgHead) {
            this.imgHead = imgHead;
        }

        public String getReceiveUserId() {
            return receiveUserId;
        }

        public void setReceiveUserId(String receiveUserId) {
            this.receiveUserId = receiveUserId;
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mDisposable.clear();
    }
}
