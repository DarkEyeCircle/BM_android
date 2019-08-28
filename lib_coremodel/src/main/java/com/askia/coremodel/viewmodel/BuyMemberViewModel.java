package com.askia.coremodel.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableField;

import com.askia.coremodel.datamodel.http.entities.BaseResponseData;
import com.askia.coremodel.datamodel.http.entities.BuyMemberData;
import com.askia.coremodel.datamodel.http.entities.ResponseCode;
import com.askia.coremodel.datamodel.http.parm.UserDictionary;
import com.askia.coremodel.datamodel.http.repository.GankDataRepository;
import com.askia.coremodel.util.NetUtils;

import java.net.SocketTimeoutException;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class BuyMemberViewModel extends ViewModel {
    private MutableLiveData<BuyMemberData> buyMemberLiveData = new MutableLiveData<>();
    private MutableLiveData<BaseResponseData> checkSecurityPwdLiveData = new MutableLiveData<>();
    private final CompositeDisposable mDisposable = new CompositeDisposable();
    private PageData pageData = new PageData();

    public MutableLiveData<BuyMemberData> getBuyMemberLiveData() {
        return buyMemberLiveData;
    }

    public MutableLiveData<BaseResponseData> getCheckSecurityPwdLiveData() {
        return checkSecurityPwdLiveData;
    }

    public PageData getPageData() {
        return pageData;
    }

    public void buyMember() {
        if (!NetUtils.isNetConnected()) {
            buyMemberLiveData.setValue(null);
            return;
        }
        GankDataRepository.BuyMember(this.pageData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .safeSubscribe(new Observer<BuyMemberData>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onNext(BuyMemberData responseData) {
                        buyMemberLiveData.setValue(responseData);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        BuyMemberData responseData = new BuyMemberData();
                        if (e instanceof SocketTimeoutException) {
                            responseData.setCode(ResponseCode.ConnectTimeOut);
                            responseData.setMsg("连接超时，请重试");
                        } else {
                            responseData.setCode(ResponseCode.ServerNotResponding);
                            responseData.setMsg("服务器无响应，请重试");
                        }
                        buyMemberLiveData.setValue(responseData);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
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

    public void setData(String cardId, String value, String url) {
        pageData.getFaceValue().set(value);
        pageData.getCardCoverUrl().set(url);
        pageData.setCardId(cardId);
    }

    public class PageData {

        private String cardId;
        private ObservableField<String> faceValue = new ObservableField<>();
        private ObservableField<String> cardCoverUrl = new ObservableField<>();
        private int payType = UserDictionary.CHANNEL_ALIPAY;
        //订单URL
        private String orderUrl;

        public String getCardId() {
            return cardId;
        }

        public void setCardId(String cardId) {
            this.cardId = cardId;
        }

        public ObservableField<String> getFaceValue() {
            return faceValue;
        }

        public void setFaceValue(ObservableField<String> faceValue) {
            this.faceValue = faceValue;
        }

        public ObservableField<String> getCardCoverUrl() {
            return cardCoverUrl;
        }

        public void setCardCoverUrl(ObservableField<String> cardCoverUrl) {
            this.cardCoverUrl = cardCoverUrl;
        }

        public int getPayType() {
            return payType;
        }

        public void setPayType(int payType) {
            this.payType = payType;
        }

        public String getOrderUrl() {
            return orderUrl;
        }

        public void setOrderUrl(String orderUrl) {
            this.orderUrl = orderUrl;
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mDisposable.clear();
    }
}
