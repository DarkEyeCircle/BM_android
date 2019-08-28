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

public class PurchaseStatesViewModel extends ViewModel {
    private MutableLiveData<BaseResponseData> exchangeLiveData = new MutableLiveData<>();
    private final CompositeDisposable mDisposable = new CompositeDisposable();
    private PageData pageData = new PageData();

    public MutableLiveData<BaseResponseData> getExchangeLiveData() {
        return exchangeLiveData;
    }

    public PageData getPageData() {
        return pageData;
    }

    public void exchangeIntegral() {
        if (!NetUtils.isNetConnected()) {
            exchangeLiveData.setValue(null);
            return;
        }
        GankDataRepository.ExchangeIntegral(this.pageData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .safeSubscribe(new Observer<BaseResponseData>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onNext(BaseResponseData responseData) {
                        exchangeLiveData.setValue(responseData);
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
                        };
                        exchangeLiveData.setValue(responseData);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    public class PageData {

        private String cardId;
        private boolean purchaseState = true;
        private ObservableField<String> faceValue = new ObservableField<>();
        private ObservableField<String> integralMulriple = new ObservableField<>();


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

        public ObservableField<String> getIntegralMulriple() {
            return integralMulriple;
        }

        public void setIntegralMulriple(ObservableField<String> integralMulriple) {
            this.integralMulriple = integralMulriple;
        }

        public boolean isPurchaseState() {
            return purchaseState;
        }

        public void setPurchaseState(boolean purchaseState) {
            this.purchaseState = purchaseState;
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mDisposable.clear();
    }
}
