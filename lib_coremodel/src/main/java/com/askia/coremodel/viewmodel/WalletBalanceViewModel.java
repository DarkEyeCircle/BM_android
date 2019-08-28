package com.askia.coremodel.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableField;

import com.askia.coremodel.datamodel.http.entities.BaseResponseData;
import com.askia.coremodel.datamodel.http.entities.ResponseCode;
import com.askia.coremodel.datamodel.http.entities.WalletBalanceData;
import com.askia.coremodel.datamodel.http.repository.GankDataRepository;
import com.askia.coremodel.util.DecimalUtil;
import com.askia.coremodel.util.NetUtils;

import java.math.RoundingMode;
import java.net.SocketTimeoutException;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class WalletBalanceViewModel extends ViewModel {


    public enum BalanceType {
        //百盟钱包余额
        BMWallet(1),
        //商户钱包余额
        MerchantsWallet(2),
        //会员钱包余额
        MemberWallet(3),
        //商城积分余额
        MallPoints(4),
        //待激活积分余额
        WaitActiviePoints(5);


        private final int type;

        BalanceType(int type) {
            this.type = type;
        }

        public static BalanceType getBillType(int type) {
            switch (type) {
                case 1:
                    return BMWallet;
                case 2:
                    return MerchantsWallet;
                case 3:
                    return MemberWallet;
                case 4:
                    return MallPoints;
                case 5:
                    return WaitActiviePoints;
                default:
                    return null;
            }
        }

        public int getType() {
            return type;
        }
    }

    private MutableLiveData<WalletBalanceData> walletBalanceLiveData = new MutableLiveData<>();
    private MutableLiveData<WalletBalanceData> acttiveIntegralLiveData = new MutableLiveData<>();
    private MutableLiveData<BaseResponseData> checkSecurityPwdLiveData = new MutableLiveData<>();
    private PageData pageData = new PageData();
    private final CompositeDisposable mDisposable = new CompositeDisposable();
    private float proportion;

    public MutableLiveData<WalletBalanceData> getWalletBalanceLiveData() {
        return walletBalanceLiveData;
    }

    public MutableLiveData<WalletBalanceData> getActtiveIntegralLiveData() {
        return acttiveIntegralLiveData;
    }

    public MutableLiveData<BaseResponseData> getCheckSecurityPwdLiveData() {
        return checkSecurityPwdLiveData;
    }

    public PageData getPageData() {
        return pageData;
    }

    //计算手续费
    public void calculatePoundage(float money) {
        String poundage = DecimalUtil.multiplyWithScale(proportion + "", money + "", 2);
        poundage = DecimalUtil.divideWithRoundingModeAndScale(poundage, 100 + "", RoundingMode.DOWN, 2);
        pageData.getPoundage().set(poundage);
    }

    public void update(WalletBalanceData.ObjBean objBean) {
        pageData.getBalance().set(String.valueOf(objBean.getBalance()));
        pageData.getPoundage().set("0.00");
        pageData.getNeedActiveIntegral().set("");
        proportion = objBean.getProportion();
    }

    public void activieIntegral() {
        if (!NetUtils.isNetConnected()) {
            acttiveIntegralLiveData.setValue(null);
            return;
        }
        GankDataRepository.ActivationWaitIntegral(Long.valueOf(pageData.getNeedActiveIntegral().get()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .safeSubscribe(new Observer<WalletBalanceData>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onNext(WalletBalanceData responseData) {
                        acttiveIntegralLiveData.setValue(responseData);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        WalletBalanceData responseData = new WalletBalanceData();
                        if (e instanceof SocketTimeoutException) {
                            responseData.setCode(ResponseCode.ConnectTimeOut);
                            responseData.setMsg("连接超时，请重试");
                        } else {
                            responseData.setCode(ResponseCode.ServerNotResponding);
                            responseData.setMsg("服务器无响应，请重试");
                        }
                        acttiveIntegralLiveData.setValue(responseData);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    //获取余额
    public void getWalletBalance(BalanceType balanceType) {
        if (!NetUtils.isNetConnected()) {
            walletBalanceLiveData.setValue(null);
            return;
        }
        GankDataRepository.getWalletBalance(balanceType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .safeSubscribe(new Observer<WalletBalanceData>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onNext(WalletBalanceData responseData) {
                        walletBalanceLiveData.setValue(responseData);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        WalletBalanceData responseData = new WalletBalanceData();
                        if (e instanceof SocketTimeoutException) {
                            responseData.setCode(ResponseCode.ConnectTimeOut);
                            responseData.setMsg("连接超时，请重试");
                        } else {
                            responseData.setCode(ResponseCode.ServerNotResponding);
                            responseData.setMsg("服务器无响应，请重试");
                        }
                        walletBalanceLiveData.setValue(responseData);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    //验证安全支付密码
    public void checkSecurityPwd(String pwd) {
        if (!NetUtils.isNetConnected()) {
            checkSecurityPwdLiveData.setValue(null);
            return;
        }
        GankDataRepository.VerifySecurityPassword(pwd)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .safeSubscribe(new Observer<BaseResponseData>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onNext(BaseResponseData responseData) {
                        checkSecurityPwdLiveData.setValue(responseData);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        BaseResponseData responseData = new BaseResponseData();
                        responseData.setCode(ResponseCode.ServerNotResponding);
                        responseData.setMsg("服务器无响应");
                        checkSecurityPwdLiveData.setValue(responseData);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    public class PageData {

        //余额
        private ObservableField<String> balance = new ObservableField<>();
        //需要激活积分
        private ObservableField<String> needActiveIntegral = new ObservableField<>();
        //手续费
        private ObservableField<String> poundage = new ObservableField<>();

        public ObservableField<String> getBalance() {
            return balance;
        }

        public void setBalance(ObservableField<String> balance) {
            this.balance = balance;
        }

        public ObservableField<String> getNeedActiveIntegral() {
            return needActiveIntegral;
        }

        public void setNeedActiveIntegral(ObservableField<String> needActiveIntegral) {
            this.needActiveIntegral = needActiveIntegral;
        }

        public ObservableField<String> getPoundage() {
            return poundage;
        }

        public void setPoundage(ObservableField<String> poundage) {
            this.poundage = poundage;
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mDisposable.clear();
    }
}
