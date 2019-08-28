package com.askia.coremodel.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.askia.coremodel.datamodel.http.entities.IntegralMallDetailsData;
import com.askia.coremodel.datamodel.http.entities.ResponseCode;
import com.askia.coremodel.datamodel.http.repository.GankDataRepository;
import com.askia.coremodel.util.NetUtils;

import java.net.SocketTimeoutException;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class IntegralMallDetilsViewModel extends ViewModel {

    private MutableLiveData<IntegralMallDetailsData> billsListLiveData = new MutableLiveData<>();
    private final CompositeDisposable mDisposable = new CompositeDisposable();
    private int page = 1;

    public MutableLiveData<IntegralMallDetailsData> getBillsListLiveData() {
        return billsListLiveData;
    }

    public IntegralMallDetilsViewModel() {
        getBills();
    }

    public void updatePage(int page) {
        this.page = page + 1;
    }

    public void getBills() {
        if (!NetUtils.isNetConnected()) {
            billsListLiveData.setValue(null);
            return;
        }
        GankDataRepository.GetIntegralMallDetails(this.page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .safeSubscribe(new Observer<IntegralMallDetailsData>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onNext(IntegralMallDetailsData responseData) {
                        billsListLiveData.setValue(responseData);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        IntegralMallDetailsData responseData = new IntegralMallDetailsData();
                        if (e instanceof SocketTimeoutException) {
                            responseData.setCode(ResponseCode.ConnectTimeOut);
                            responseData.setMsg("连接超时，请重试");
                        } else {
                            responseData.setCode(ResponseCode.ServerNotResponding);
                            responseData.setMsg("服务器无响应，请重试");
                        }
                        billsListLiveData.setValue(responseData);
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
