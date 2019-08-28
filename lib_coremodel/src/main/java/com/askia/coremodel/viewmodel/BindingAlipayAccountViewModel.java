package com.askia.coremodel.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableField;

import com.apkfuns.logutils.LogUtils;
import com.askia.coremodel.datamodel.database.repository.DBRepository;
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

public class BindingAlipayAccountViewModel extends ViewModel {

    private MutableLiveData<BaseResponseData> bindLiveData = new MutableLiveData<>();
    private final CompositeDisposable mDisposable = new CompositeDisposable();
    private PageData pageData = new PageData();

    public MutableLiveData<BaseResponseData> getBindLiveData() {
        return bindLiveData;
    }

    public PageData getPageData() {
        return pageData;
    }

    //绑定支付宝
    public void bindAlipay() {
        if (!NetUtils.isNetConnected()) {
            bindLiveData.setValue(null);
            return;
        }
        GankDataRepository.BindAlipayAccount(pageData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .safeSubscribe(new Observer<BaseResponseData>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onNext(BaseResponseData value) {
                        bindLiveData.setValue(value);
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
                        bindLiveData.setValue(responseData);
                    }

                    @Override
                    public void onComplete() {
                        LogUtils.d("onComplete");
                    }
                });
    }

    public class PageData {

        public PageData() {
            account.set("");
        }

        private ObservableField<String> account = new ObservableField<>();

        public ObservableField<String> getAccount() {
            return account;
        }

        public void setAccount(ObservableField<String> account) {
            this.account = account;
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mDisposable.clear();
    }
}
