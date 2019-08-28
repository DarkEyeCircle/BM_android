package com.askia.coremodel.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.askia.coremodel.datamodel.http.entities.BaseResponseData;
import com.askia.coremodel.datamodel.http.entities.BaseResponseData;
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

public class CardBagViewModel extends ViewModel {

    private MutableLiveData<CardsData> cardListLiveData = new MutableLiveData<>();
    private final CompositeDisposable mDisposable = new CompositeDisposable();
    private int page = 1;

    public CardBagViewModel() {
        getCardList();
    }

    public MutableLiveData<CardsData> getCardListLiveData() {
        return cardListLiveData;
    }

    public void updatePage(int page) {
        this.page = page + 1;
    }

    public void getCardList() {
        if (!NetUtils.isNetConnected()) {
            cardListLiveData.setValue(null);
            return;
        }
        GankDataRepository.GetCardVouchersData(this.page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .safeSubscribe(new Observer<CardsData>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onNext(CardsData responseData) {
                        cardListLiveData.setValue(responseData);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        CardsData responseData = new CardsData();
                        if (e instanceof SocketTimeoutException) {
                            responseData.setCode(ResponseCode.ConnectTimeOut);
                            responseData.setMsg("连接超时，请重试");
                        } else {
                            responseData.setCode(ResponseCode.ServerNotResponding);
                            responseData.setMsg("服务器无响应，请重试");
                        }
                        cardListLiveData.setValue(responseData);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mDisposable.clear();
    }
}
