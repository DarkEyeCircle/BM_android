package com.askia.coremodel.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.askia.coremodel.datamodel.http.entities.ProceedsData;
import com.askia.coremodel.datamodel.http.entities.QRUrlData;
import com.askia.coremodel.datamodel.http.entities.ResponseCode;
import com.askia.coremodel.datamodel.http.entities.ScanfProceedsData;
import com.askia.coremodel.datamodel.http.repository.GankDataRepository;
import com.askia.coremodel.util.NetUtils;

import java.net.SocketTimeoutException;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ProceedsViewModel extends ViewModel {

    private MutableLiveData<QRUrlData> QRLiveDataForSetMoney = new MutableLiveData<>();
    private MutableLiveData<QRUrlData> QRLiveData = new MutableLiveData<>();
    private MutableLiveData<ProceedsData> payStateLiveData = new MutableLiveData<>();

    private PageData pageData = new PageData();
    private final CompositeDisposable mDisposable = new CompositeDisposable();

    public ProceedsViewModel() {
    }

    public MutableLiveData<QRUrlData> getQRLiveData() {
        return QRLiveData;
    }

    public MutableLiveData<ProceedsData> getPayStateLiveData() {
        return payStateLiveData;
    }

    public MutableLiveData<QRUrlData> getQRLiveDataForSetMoney() {
        return QRLiveDataForSetMoney;
    }

    public PageData getPageData() {
        return pageData;
    }


    //获取收款状态
    public void getProceedsState() {
        if (!NetUtils.isNetConnected()) {
            payStateLiveData.setValue(null);
            return;
        }
        GankDataRepository.CheckTradingStateFroPayment()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .safeSubscribe(new Observer<ProceedsData>() {

                    Disposable disposable;

                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(ProceedsData responseData) {
                        payStateLiveData.setValue(responseData);
                    }

                    @Override
                    public void onError(Throwable e) {
                        disposable.dispose();
                        e.printStackTrace();
                        ProceedsData responseData = new ProceedsData();
                        if (e instanceof SocketTimeoutException) {
                            responseData.setCode(ResponseCode.ConnectTimeOut);
                            responseData.setMsg("连接超时，请重试");
                        } else {
                            responseData.setCode(ResponseCode.ServerNotResponding);
                            responseData.setMsg("服务器无响应，请重试");
                        }
                        payStateLiveData.setValue(responseData);
                    }

                    @Override
                    public void onComplete() {
                        disposable.dispose();
                    }
                });
    }

    //获取固定金额收款二维码url
    public void getProceedQRCodeForSetMoney() {
        if (!NetUtils.isNetConnected()) {
            QRLiveDataForSetMoney.setValue(null);
            return;
        }
        GankDataRepository.GetProceedQRCode(pageData).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .safeSubscribe(new Observer<QRUrlData>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onNext(QRUrlData responseData) {
                        QRLiveDataForSetMoney.setValue(responseData);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        QRUrlData responseData = new QRUrlData();
                        if (e instanceof SocketTimeoutException) {
                            responseData.setCode(ResponseCode.ConnectTimeOut);
                            responseData.setMsg("连接超时，请重试");
                        } else {
                            responseData.setCode(ResponseCode.ServerNotResponding);
                            responseData.setMsg("服务器无响应，请重试");
                        }
                        QRLiveDataForSetMoney.setValue(responseData);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    //获取收款二维码 不设置金额
    public void getProceedQRCode() {
        if (!NetUtils.isNetConnected()) {
            QRLiveData.setValue(null);
            return;
        }
        GankDataRepository.GetProceedQRCode().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .safeSubscribe(new Observer<QRUrlData>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onNext(QRUrlData responseData) {
                        QRLiveData.setValue(responseData);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        QRUrlData responseData = new QRUrlData();
                        if (e instanceof SocketTimeoutException) {
                            responseData.setCode(ResponseCode.ConnectTimeOut);
                            responseData.setMsg("连接超时，请重试");
                        } else {
                            responseData.setCode(ResponseCode.ServerNotResponding);
                            responseData.setMsg("服务器无响应，请重试");
                        }
                        QRLiveData.setValue(responseData);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    public class PageData {

        public PageData() {
            money = "";
        }

        //金额
        private String money;
        //二维码ID
        private String codeId;

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getCodeId() {
            return codeId;
        }

        public void setCodeId(String codeId) {
            this.codeId = codeId;
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mDisposable.clear();
    }
}
