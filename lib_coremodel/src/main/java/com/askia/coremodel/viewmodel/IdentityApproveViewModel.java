package com.askia.coremodel.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableField;

import com.askia.coremodel.GlobalUserDataStore;
import com.askia.coremodel.datamodel.http.entities.BaseResponseData;
import com.askia.coremodel.datamodel.http.entities.ResponseCode;
import com.askia.coremodel.datamodel.http.entities.User;
import com.askia.coremodel.datamodel.http.repository.GankDataRepository;
import com.askia.coremodel.util.NetUtils;
import com.askia.coremodel.util.RegexUtils;

import java.net.SocketTimeoutException;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class IdentityApproveViewModel extends ViewModel {

    private MutableLiveData<BaseResponseData> smsLiveData = new MutableLiveData<>();
    private MutableLiveData<User> bindingLiveData = new MutableLiveData<>();
    private final CompositeDisposable mDisposable = new CompositeDisposable();
    private PageData pageData;

    public IdentityApproveViewModel() {
        this.pageData = new PageData();
    }

    public MutableLiveData<BaseResponseData> getSmsLiveData() {
        return smsLiveData;
    }

    public MutableLiveData<User> getBindingLiveData() {
        return bindingLiveData;
    }

    public PageData getPageData() {
        return pageData;
    }


    //获取验证码
    public void senSmsCode() {
        if (!NetUtils.isNetConnected()) {
            smsLiveData.setValue(null);
            return;
        }
        GankDataRepository.SendSmsCodeForBinding(pageData)
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

    //身份绑定
    public void bindUserInfo() {
        if (!NetUtils.isNetConnected()) {
            bindingLiveData.setValue(null);
            return;
        }
        GankDataRepository.BindUserInfo(pageData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .safeSubscribe(new Observer<User>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onNext(User responseData) {
                        bindingLiveData.setValue(responseData);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        User responseData = new User();
                        if (e instanceof SocketTimeoutException) {
                            responseData.setCode(ResponseCode.ConnectTimeOut);
                            responseData.setMsg("连接超时，请重试");
                        } else {
                            responseData.setCode(ResponseCode.ServerNotResponding);
                            responseData.setMsg("服务器无响应，请重试");
                        }
                        bindingLiveData.setValue(responseData);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    public class PageData {

        public PageData() {
            realPhoneNum = GlobalUserDataStore.getInstance().getMobile();
            phoneNum.set(RegexUtils.formatPhoneNumToHide(GlobalUserDataStore.getInstance().getMobile()));
            verificationCode.set("");
            curCity.set("");
            detailLocation.set("");
            realName.set("");
            IDCardNum.set("");

        }

        private String realPhoneNum = null;

        private String idCardImg_behind = null;

        private String idCardImg_front = null;

        private String idCardImg_behind_url = null;

        private String idCardImg_front_url = null;


        private ObservableField<String> phoneNum = new ObservableField<>();

        private ObservableField<String> verificationCode = new ObservableField<>();

        private ObservableField<String> curCity = new ObservableField<>();

        private ObservableField<String> detailLocation = new ObservableField<>();

        private ObservableField<String> realName = new ObservableField<>();

        private ObservableField<String> IDCardNum = new ObservableField<>();

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

        public ObservableField<String> getCurCity() {
            return curCity;
        }

        public void setCurCity(ObservableField<String> curCity) {
            this.curCity = curCity;
        }

        public ObservableField<String> getDetailLocation() {
            return detailLocation;
        }

        public void setDetailLocation(ObservableField<String> detailLocation) {
            this.detailLocation = detailLocation;
        }

        public ObservableField<String> getRealName() {
            return realName;
        }

        public void setRealName(ObservableField<String> realName) {
            this.realName = realName;
        }

        public ObservableField<String> getIDCardNum() {
            return IDCardNum;
        }

        public void setIDCardNum(ObservableField<String> IDCardNum) {
            this.IDCardNum = IDCardNum;
        }

        public String getRealPhoneNum() {
            return realPhoneNum;
        }

        public void setRealPhoneNum(String realPhoneNum) {
            this.realPhoneNum = realPhoneNum;
        }

        public String getIdCardImg_behind() {
            return idCardImg_behind;
        }

        public void setIdCardImg_behind(String idCardImg_behind) {
            this.idCardImg_behind = idCardImg_behind;
        }

        public String getIdCardImg_front() {
            return idCardImg_front;
        }

        public void setIdCardImg_front(String idCardImg_front) {
            this.idCardImg_front = idCardImg_front;
        }

        public String getIdCardImg_behind_url() {
            return idCardImg_behind_url;
        }

        public void setIdCardImg_behind_url(String idCardImg_behind_url) {
            this.idCardImg_behind_url = idCardImg_behind_url;
        }

        public String getIdCardImg_front_url() {
            return idCardImg_front_url;
        }

        public void setIdCardImg_front_url(String idCardImg_front_url) {
            this.idCardImg_front_url = idCardImg_front_url;
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mDisposable.clear();
    }
}
