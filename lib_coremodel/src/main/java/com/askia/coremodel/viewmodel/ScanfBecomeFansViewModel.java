package com.askia.coremodel.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableBoolean;
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

public class ScanfBecomeFansViewModel extends ViewModel {
    private MutableLiveData<BaseResponseData> becomeFansLiveData = new MutableLiveData<>();
    private final CompositeDisposable mDisposable = new CompositeDisposable();
    private PageData pageData = new PageData();

    public MutableLiveData<BaseResponseData> getBecomeFansLiveData() {
        return becomeFansLiveData;
    }

    public PageData getPageData() {
        return pageData;
    }

    //添加粉丝
    public void becomeFans() {
        if (!NetUtils.isNetConnected()) {
            becomeFansLiveData.setValue(null);
            return;
        }
        GankDataRepository.BecomeFans(pageData.getCodeId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .safeSubscribe(new Observer<BaseResponseData>() {

                    Disposable disposable;

                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(BaseResponseData responseData) {
                        becomeFansLiveData.setValue(responseData);
                    }

                    @Override
                    public void onError(Throwable e) {
                        disposable.dispose();
                        e.printStackTrace();
                        BaseResponseData responseData = new BaseResponseData();
                        if (e instanceof SocketTimeoutException) {
                            responseData.setCode(ResponseCode.ConnectTimeOut);
                            responseData.setMsg("连接超时，请重试");
                        } else {
                            responseData.setCode(ResponseCode.ServerNotResponding);
                            responseData.setMsg("服务器无响应，请重试");
                        }
                        becomeFansLiveData.setValue(responseData);
                    }

                    @Override
                    public void onComplete() {
                        disposable.dispose();
                    }
                });
    }

    public class PageData {

        public PageData() {
            isSuccess.set(false);
        }

        private String codeId;
        private ObservableBoolean isSuccess = new ObservableBoolean();
        private ObservableField<String> errorMes = new ObservableField<>();

        public String getCodeId() {
            return codeId;
        }

        public void setCodeId(String codeId) {
            this.codeId = codeId;
        }

        public ObservableBoolean getIsSuccess() {
            return isSuccess;
        }

        public void setIsSuccess(ObservableBoolean isSuccess) {
            this.isSuccess = isSuccess;
        }

        public ObservableField<String> getErrorMes() {
            return errorMes;
        }

        public void setErrorMes(ObservableField<String> errorMes) {
            this.errorMes = errorMes;
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mDisposable.clear();
    }
}
