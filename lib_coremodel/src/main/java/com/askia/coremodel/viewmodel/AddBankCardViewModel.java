package com.askia.coremodel.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableField;

import com.askia.coremodel.GlobalUserDataStore;
import com.askia.coremodel.datamodel.http.entities.BankCardInfoData;
import com.askia.coremodel.datamodel.http.entities.BaseResponseData;
import com.askia.coremodel.datamodel.http.entities.ResponseCode;
import com.askia.coremodel.datamodel.http.repository.GankDataRepository;
import com.askia.coremodel.util.NetUtils;
import com.askia.coremodel.util.RegexUtils;

import java.net.SocketTimeoutException;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AddBankCardViewModel extends ViewModel {

    private MutableLiveData<BaseResponseData> addBankCardLiveData = new MutableLiveData<>();
    private MutableLiveData<BaseResponseData> smsLiveData = new MutableLiveData<>();
    private MutableLiveData<BankCardInfoData> cardInfoLiveData = new MutableLiveData<>();
    private PageData pageData = null;
    private final CompositeDisposable mDisposable = new CompositeDisposable();

    public MutableLiveData<BaseResponseData> getAddBankCardLiveData() {
        return addBankCardLiveData;
    }

    public MutableLiveData<BaseResponseData> getSmsLiveData() {
        return smsLiveData;
    }

    public MutableLiveData<BankCardInfoData> getCardInfoLiveData() {
        return cardInfoLiveData;
    }

    public PageData getPageData() {
        return pageData;
    }

    public void setPageData(PageData pageData) {
        this.pageData = pageData;
    }

    public AddBankCardViewModel() {
        pageData = new PageData();
    }

    //获取验证码
    public void senSmsCode() {
        if (!NetUtils.isNetConnected()) {
            smsLiveData.setValue(null);
            return;
        }
        GankDataRepository.SendSmsForBindCard(pageData)
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

    //绑定银行卡
    public void addBankCard() {
        if (!NetUtils.isNetConnected()) {
            addBankCardLiveData.setValue(null);
            return;
        }
        GankDataRepository.BindUserCard(pageData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .safeSubscribe(new Observer<BaseResponseData>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onNext(BaseResponseData responseData) {
                        addBankCardLiveData.setValue(responseData);
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
                        addBankCardLiveData.setValue(responseData);
                    }

                    @Override
                    public void onComplete() {
                    }
                });

    }

    // 根据银行卡号获取银行卡信息
    public void getBankCardInfo() {
        if (!NetUtils.isNetConnected()) {
            cardInfoLiveData.setValue(null);
            return;
        }
        GankDataRepository.GetBankCardInfo(pageData.getBankCardNum().get().replaceAll(" ", ""))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .safeSubscribe(new Observer<BankCardInfoData>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onNext(BankCardInfoData responseData) {
                        cardInfoLiveData.setValue(responseData);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        BankCardInfoData responseData = new BankCardInfoData();
                        if (e instanceof SocketTimeoutException) {
                            responseData.setCode(ResponseCode.ConnectTimeOut);
                            responseData.setMsg("连接超时，请重试");
                        } else {
                            responseData.setCode(ResponseCode.ServerNotResponding);
                            responseData.setMsg("服务器无响应，请重试");
                        }
                        cardInfoLiveData.setValue(responseData);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    public class PageData {

        public PageData() {
            phoneNum.set("");
            realName.set(GlobalUserDataStore.getInstance().getRealName());
            bankCardNum.set("");
            bankName.set("");
            verifiCode.set("");
        }

        private String realPhoneNum;
        private ObservableField<String> realName = new ObservableField<>();
        private ObservableField<String> bankCardNum = new ObservableField<>();
        private ObservableField<String> bankName = new ObservableField<>();
        private ObservableField<String> phoneNum = new ObservableField<>();
        private ObservableField<String> verifiCode = new ObservableField<>();
        private ObservableField<String> bankCity = new ObservableField<>();


        public String getRealPhoneNum() {
            return realPhoneNum;
        }

        public void setRealPhoneNum(String realPhoneNum) {
            this.realPhoneNum = realPhoneNum;
        }

        public ObservableField<String> getRealName() {
            return realName;
        }

        public void setRealName(ObservableField<String> name) {
            this.realName = name;
        }

        public ObservableField<String> getBankCardNum() {
            return bankCardNum;
        }

        public void setBankCardNum(ObservableField<String> bankCardNum) {
            this.bankCardNum = bankCardNum;
        }

        public ObservableField<String> getBankName() {
            return bankName;
        }

        public void setBankName(ObservableField<String> bankName) {
            this.bankName = bankName;
        }

        public ObservableField<String> getPhoneNum() {
            return phoneNum;
        }

        public void setPhoneNum(ObservableField<String> phoneNum) {
            this.phoneNum = phoneNum;
        }

        public ObservableField<String> getVerifiCode() {
            return verifiCode;
        }

        public void setVerifiCode(ObservableField<String> verifiCode) {
            this.verifiCode = verifiCode;
        }

        public ObservableField<String> getBankCity() {
            return bankCity;
        }

        public void setBankCity(ObservableField<String> bankCity) {
            this.bankCity = bankCity;
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mDisposable.clear();
    }
}
