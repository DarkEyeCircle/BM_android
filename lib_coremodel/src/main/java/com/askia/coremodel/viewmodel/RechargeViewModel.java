package com.askia.coremodel.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableField;

import com.askia.coremodel.datamodel.http.entities.RechargeOrderData;
import com.askia.coremodel.datamodel.http.entities.ResponseCode;
import com.askia.coremodel.datamodel.http.repository.GankDataRepository;
import com.askia.coremodel.util.NetUtils;

import java.net.SocketTimeoutException;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RechargeViewModel extends ViewModel {

    private MutableLiveData<RechargeOrderData> rechangeOrderLiveData = new MutableLiveData<>();
    private final CompositeDisposable mDisposable = new CompositeDisposable();
    private PageData pageData = new PageData();


    public MutableLiveData<RechargeOrderData> getRechangeOrderLiveData() {
        return rechangeOrderLiveData;
    }

    public PageData getPageData() {
        return pageData;
    }

    public enum RechargeType {
        //微信充值
        Wechat(1),
        //支付宝充值
        AliPay(2),
        //银联充值
        Unionpay(3);


        private final int type;

        RechargeType(int type) {
            this.type = type;
        }

        public static RechargeType getRechargeType(int type) {
            switch (type) {
                case 1:
                    return Wechat;
                case 2:
                    return AliPay;
                case 3:
                    return Unionpay;
                default:
                    return null;
            }
        }

        public int getType() {
            return type;
        }
    }

    //生成充值订单
    public void creatRechargeOrder() {
        if (!NetUtils.isNetConnected()) {
            rechangeOrderLiveData.setValue(null);
            return;
        }
        GankDataRepository.CreatRechargeOrder(pageData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .safeSubscribe(new Observer<RechargeOrderData>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onNext(RechargeOrderData responseData) {
                        rechangeOrderLiveData.setValue(responseData);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        RechargeOrderData responseData = new RechargeOrderData();
                        if (e instanceof SocketTimeoutException) {
                            responseData.setCode(ResponseCode.ConnectTimeOut);
                            responseData.setMsg("连接超时，请重试");
                        } else {
                            responseData.setCode(ResponseCode.ServerNotResponding);
                            responseData.setMsg("服务器无响应，请重试");
                        }
                        rechangeOrderLiveData.setValue(responseData);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }


    public class PageData {

        //支付方式
        private RechargeType rechargeType;
        //支付金额
        private ObservableField<String> payAmount = new ObservableField<>();
        //订单URL
        private String orderUrl;

        public RechargeType getRechargeType() {
            return rechargeType;
        }

        public void setRechargeType(RechargeType rechargeType) {
            this.rechargeType = rechargeType;
        }

        public ObservableField<String> getPayAmount() {
            return payAmount;
        }

        public void setPayAmount(ObservableField<String> payAmount) {
            this.payAmount = payAmount;
        }

        public String getOrderUrl() {
            return orderUrl;
        }

        public void setOrderUrl(String orderUrl) {
            this.orderUrl = orderUrl;
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mDisposable.clear();
    }
}
