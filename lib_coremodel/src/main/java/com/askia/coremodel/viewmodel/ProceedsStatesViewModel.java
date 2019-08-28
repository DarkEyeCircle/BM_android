package com.askia.coremodel.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

import com.askia.coremodel.datamodel.http.entities.ScanfProceedsData;
import com.askia.coremodel.datamodel.http.entities.ResponseCode;
import com.askia.coremodel.datamodel.http.repository.GankDataRepository;
import com.askia.coremodel.util.NetUtils;

import java.net.SocketTimeoutException;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ProceedsStatesViewModel extends ViewModel {
    private MutableLiveData<ScanfProceedsData> proceedsStatesLiveData = new MutableLiveData<>();
    private final CompositeDisposable mDisposable = new CompositeDisposable();
    private PageData pageData = new PageData();

    public MutableLiveData<ScanfProceedsData> getProceedsStatesLiveData() {
        return proceedsStatesLiveData;
    }

    public PageData getPageData() {
        return pageData;
    }

    //收款
    public void doProceeds() {
        if (!NetUtils.isNetConnected()) {
            proceedsStatesLiveData.setValue(null);
            return;
        }
        GankDataRepository.ProceedsForQRCode(pageData.getCodeId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .safeSubscribe(new Observer<ScanfProceedsData>() {

                    Disposable disposable;

                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(ScanfProceedsData responseData) {
                        proceedsStatesLiveData.setValue(responseData);
                    }

                    @Override
                    public void onError(Throwable e) {
                        disposable.dispose();
                        e.printStackTrace();
                        ScanfProceedsData responseData = new ScanfProceedsData();
                        if (e instanceof SocketTimeoutException) {
                            responseData.setCode(ResponseCode.ConnectTimeOut);
                            responseData.setMsg("连接超时，请重试");
                        } else {
                            responseData.setCode(ResponseCode.ServerNotResponding);
                            responseData.setMsg("服务器无响应，请重试");
                        }
                        proceedsStatesLiveData.setValue(responseData);
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
        private ObservableField<String> money = new ObservableField<>();
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

        public ObservableField<String> getMoney() {
            return money;
        }

        public void setMoney(ObservableField<String> money) {
            this.money = money;
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
