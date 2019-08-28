package com.askia.coremodel.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.askia.coremodel.datamodel.http.entities.BandCardData;
import com.askia.coremodel.datamodel.http.entities.FansData;
import com.askia.coremodel.datamodel.http.entities.ResponseCode;
import com.askia.coremodel.datamodel.http.repository.GankDataRepository;
import com.askia.coremodel.util.NetUtils;

import java.net.SocketTimeoutException;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RecommendedViewModel extends ViewModel {
    private MutableLiveData<FansData> FansDataListLiveData = new MutableLiveData<>();
    private PageData pageData = new PageData();
    private final CompositeDisposable mDisposable = new CompositeDisposable();
    private int page = 1;

    public PageData getPageData() {
        return pageData;
    }

    public MutableLiveData<FansData> getFansDataListLiveData() {
        return FansDataListLiveData;
    }

    public void updatePage(int page) {
        this.page = page + 1;
    }

    public void getFansListData() {
        if (!NetUtils.isNetConnected()) {
            FansDataListLiveData.setValue(null);
            return;
        }
        GankDataRepository.GetFansData(this.page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .safeSubscribe(new Observer<FansData>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onNext(FansData responseData) {
                        FansDataListLiveData.setValue(responseData);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        FansData responseData = new FansData();
                        if (e instanceof SocketTimeoutException) {
                            responseData.setCode(ResponseCode.ConnectTimeOut);
                            responseData.setMsg("连接超时，请重试");
                        } else {
                            responseData.setCode(ResponseCode.ServerNotResponding);
                            responseData.setMsg("服务器无响应，请重试");
                        }
                        FansDataListLiveData.setValue(responseData);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    public class PageData {

        public PageData() {
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mDisposable.clear();
    }
}
