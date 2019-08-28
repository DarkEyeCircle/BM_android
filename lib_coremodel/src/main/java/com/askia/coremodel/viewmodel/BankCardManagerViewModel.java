package com.askia.coremodel.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.askia.coremodel.datamodel.http.entities.BandCardData;
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

public class BankCardManagerViewModel extends ViewModel {

    private MutableLiveData<BandCardData> bankCardListLiveData = new MutableLiveData<>();
    private MutableLiveData<BaseResponseData> unbindCardLiveData = new MutableLiveData<>();
    private final CompositeDisposable mDisposable = new CompositeDisposable();

    public MutableLiveData<BandCardData> getBankCardListLiveData() {
        return bankCardListLiveData;
    }

    public MutableLiveData<BaseResponseData> getUnbindCardLiveData() {
        return unbindCardLiveData;
    }

    public BankCardManagerViewModel() {
        getBankCardListData();
    }


    public void unbindBankCard(BandCardData.ListBean listBean) {
        if (!NetUtils.isNetConnected()) {
            unbindCardLiveData.setValue(null);
            return;
        }
        GankDataRepository.UnbindUserBankCard(listBean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .safeSubscribe(new Observer<BaseResponseData>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onNext(BaseResponseData responseData) {
                        unbindCardLiveData.setValue(responseData);
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
                        unbindCardLiveData.setValue(responseData);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    public void getBankCardListData() {
        if (!NetUtils.isNetConnected()) {
            bankCardListLiveData.setValue(null);
            return;
        }
        GankDataRepository.GetUserBankCardData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .safeSubscribe(new Observer<BandCardData>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onNext(BandCardData responseData) {
                        bankCardListLiveData.setValue(responseData);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        BandCardData bandCardData = new BandCardData();
                        if (e instanceof SocketTimeoutException) {
                            bandCardData.setCode(ResponseCode.ConnectTimeOut);
                            bandCardData.setMsg("连接超时，请重试");
                        } else {
                            bandCardData.setCode(ResponseCode.ServerNotResponding);
                            bandCardData.setMsg("服务器无响应，请重试");
                        }
                        bankCardListLiveData.setValue(bandCardData);
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
