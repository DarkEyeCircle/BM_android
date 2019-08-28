package com.askia.coremodel.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableField;

import com.askia.coremodel.datamodel.http.entities.BaseResponseData;
import com.askia.coremodel.datamodel.http.entities.BuyCardData;
import com.askia.coremodel.datamodel.http.entities.CardsData;
import com.askia.coremodel.datamodel.http.entities.ResponseCode;
import com.askia.coremodel.datamodel.http.repository.GankDataRepository;
import com.askia.coremodel.util.NetUtils;

import java.net.SocketTimeoutException;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class BuyCardViewModel extends ViewModel {
    private MutableLiveData<BuyCardData> buyCardLiveData = new MutableLiveData<>();
    private final CompositeDisposable mDisposable = new CompositeDisposable();
    private PageData pageData = new PageData();

    public MutableLiveData<BuyCardData> getBuyCardLiveData() {
        return buyCardLiveData;
    }

    public PageData getPageData() {
        return pageData;
    }

    public void buyCard() {
        if (!NetUtils.isNetConnected()) {
            buyCardLiveData.setValue(null);
            return;
        }
        GankDataRepository.BuyCard(this.pageData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .safeSubscribe(new Observer<BuyCardData>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onNext(BuyCardData responseData) {
                        buyCardLiveData.setValue(responseData);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        BuyCardData responseData = new BuyCardData();
                        if (e instanceof SocketTimeoutException) {
                            responseData.setCode(ResponseCode.ConnectTimeOut);
                            responseData.setMsg("连接超时，请重试");
                        } else {
                            responseData.setCode(ResponseCode.ServerNotResponding);
                            responseData.setMsg("服务器无响应，请重试");
                        }
                        buyCardLiveData.setValue(responseData);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    public void setData(String cardId, String value, String url, String integralMulriple) {
        pageData.getFaceValue().set(value);
        pageData.getCardCoverUrl().set(url);
        pageData.setCardId(cardId);
        pageData.setIntegralMulriple(integralMulriple);
    }

    public class PageData {

        private String cardId;
        private ObservableField<String> faceValue = new ObservableField<>();
        private ObservableField<String> cardCoverUrl = new ObservableField<>();
        private String integralMulriple;
        private String securityPassword;

        public String getSecurityPassword() {
            return securityPassword;
        }

        public void setSecurityPassword(String securityPassword) {
            this.securityPassword = securityPassword;
        }

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

        public String getIntegralMulriple() {
            return integralMulriple;
        }

        public void setIntegralMulriple(String integralMulriple) {
            this.integralMulriple = integralMulriple;
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mDisposable.clear();
    }
}
