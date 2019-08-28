package com.askia.coremodel.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableField;

import com.askia.coremodel.datamodel.http.entities.MoneyStatistics;
import com.askia.coremodel.datamodel.http.entities.ResponseCode;
import com.askia.coremodel.datamodel.http.repository.GankDataRepository;
import com.askia.coremodel.util.NetUtils;

import java.net.SocketTimeoutException;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class BillViewModel extends ViewModel {

    private MutableLiveData<MoneyStatistics> moneyBalance = new MutableLiveData<>();
    private PageData pageData = new PageData();
    private final CompositeDisposable mDisposable = new CompositeDisposable();

    public MutableLiveData<MoneyStatistics> getMoneyBalance() {
        return moneyBalance;
    }

    public PageData getPageData() {
        return pageData;
    }

    public BillViewModel() {
        getMoneyStatistics();
    }

    public void update(MoneyStatistics.ObjBean objBean) {
        pageData.getDrawMoneyTotal().set(String.valueOf(objBean.getTotalWithdrawAmount()));
        pageData.getRechargeMoneyTotal().set(String.valueOf(objBean.getTotalRechargeAmount()));
    }

    //获取资金统计（提现总额和充值总额）
    public void getMoneyStatistics() {
        if (!NetUtils.isNetConnected()) {
            moneyBalance.setValue(null);
            return;
        }
        GankDataRepository.GetMoneyStatistics()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .safeSubscribe(new Observer<MoneyStatistics>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onNext(MoneyStatistics responseData) {
                        moneyBalance.setValue(responseData);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        MoneyStatistics moneyStatistics = new MoneyStatistics();
                        if (e instanceof SocketTimeoutException) {
                            moneyStatistics.setCode(ResponseCode.ConnectTimeOut);
                            moneyStatistics.setMsg("连接超时，请重试");
                        } else {
                            moneyStatistics.setCode(ResponseCode.ServerNotResponding);
                            moneyStatistics.setMsg("服务器无响应，请重试");
                        }
                        moneyBalance.setValue(moneyStatistics);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }


    public class PageData {

        //提现总额
        private ObservableField<String> drawMoneyTotal = new ObservableField<>();
        //充值总额
        private ObservableField<String> rechargeMoneyTotal = new ObservableField<>();

        public ObservableField<String> getDrawMoneyTotal() {
            return drawMoneyTotal;
        }

        public void setDrawMoneyTotal(ObservableField<String> drawMoneyTotal) {
            this.drawMoneyTotal = drawMoneyTotal;
        }

        public ObservableField<String> getRechargeMoneyTotal() {
            return rechargeMoneyTotal;
        }

        public void setRechargeMoneyTotal(ObservableField<String> rechargeMoneyTotal) {
            this.rechargeMoneyTotal = rechargeMoneyTotal;
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mDisposable.clear();
    }
}
