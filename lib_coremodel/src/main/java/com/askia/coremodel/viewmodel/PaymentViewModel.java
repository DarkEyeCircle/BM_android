package com.askia.coremodel.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableField;

import com.askia.coremodel.datamodel.http.entities.BaseResponseData;
import com.askia.coremodel.datamodel.http.entities.PaymentData;
import com.askia.coremodel.datamodel.http.entities.PaymentScaleData;
import com.askia.coremodel.datamodel.http.entities.QRUrlData;
import com.askia.coremodel.datamodel.http.entities.ResponseCode;
import com.askia.coremodel.datamodel.http.entities.WalletBalanceData;
import com.askia.coremodel.datamodel.http.repository.GankDataRepository;
import com.askia.coremodel.util.DecimalUtil;
import com.askia.coremodel.util.NetUtils;

import java.net.SocketTimeoutException;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.askia.coremodel.viewmodel.WalletBalanceViewModel.BalanceType.BMWallet;

public class PaymentViewModel extends ViewModel {

    public enum MarginType {
        //会员承担保证金
        MembersBear(1),
        //商户承担保证金
        MerchantsBear(2);


        private final int type;

        MarginType(int type) {
            this.type = type;
        }

        public static MarginType getMarginType(int type) {
            switch (type) {
                case 1:
                    return MembersBear;
                case 2:
                    return MerchantsBear;
                default:
                    return null;
            }
        }

        public int getType() {
            return type;
        }
    }

    private MutableLiveData<WalletBalanceData> walletBalanceLiveData = new MutableLiveData<>();
    private MutableLiveData<PaymentScaleData> scaleLiveData = new MutableLiveData<>();
    private MutableLiveData<BaseResponseData> checkSecurityPwdLiveData = new MutableLiveData<>();
    private MutableLiveData<QRUrlData> ORLiveData = new MutableLiveData<>();
    private MutableLiveData<PaymentData> payStateLiveData = new MutableLiveData<>();

    private PageData pageData = new PageData();
    private final CompositeDisposable mDisposable = new CompositeDisposable();

    public PaymentViewModel() {
    }

    public MutableLiveData<WalletBalanceData> getWalletBalanceLiveData() {
        return walletBalanceLiveData;
    }

    public MutableLiveData<PaymentScaleData> getScaleLiveData() {
        return scaleLiveData;
    }

    public MutableLiveData<BaseResponseData> getCheckSecurityPwdLiveData() {
        return checkSecurityPwdLiveData;
    }

    public MutableLiveData<QRUrlData> getORLiveData() {
        return ORLiveData;
    }

    public MutableLiveData<PaymentData> getPayStateLiveData() {
        return payStateLiveData;
    }

    public PageData getPageData() {
        return pageData;
    }

    //计算手续费
    public void calculatePoundage(float money) {
        String poundage = DecimalUtil.multiplyWithScale(pageData.getScale() + "", money + "", 2);
        pageData.getPoundage().set(poundage);
    }

    //获取交易状态
    public void getPayState() {
        if (!NetUtils.isNetConnected()) {
            payStateLiveData.setValue(null);
            return;
        }
        GankDataRepository.CheckTradingStateFroProceeds()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .safeSubscribe(new Observer<PaymentData>() {

                    Disposable disposable;

                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(PaymentData responseData) {
                        payStateLiveData.setValue(responseData);
                    }

                    @Override
                    public void onError(Throwable e) {
                        disposable.dispose();
                        e.printStackTrace();
                        PaymentData responseData = new PaymentData();
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
                        if (e instanceof SocketTimeoutException) {
                            responseData.setCode(ResponseCode.ConnectTimeOut);
                            responseData.setMsg("连接超时，请重试");
                        } else {
                            responseData.setCode(ResponseCode.ServerNotResponding);
                            responseData.setMsg("服务器无响应，请重试");
                        }
                        checkSecurityPwdLiveData.setValue(responseData);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    //获取固定金额付款二维码url
    public void getQRcodeUrl() {
        if (!NetUtils.isNetConnected()) {
            ORLiveData.setValue(null);
            return;
        }
        GankDataRepository.GetQRcodeUrl(pageData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .safeSubscribe(new Observer<QRUrlData>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onNext(QRUrlData responseData) {
                        ORLiveData.setValue(responseData);
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
                        ORLiveData.setValue(responseData);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    //获取付款质保金比例
    public void getScale() {
        if (!NetUtils.isNetConnected()) {
            scaleLiveData.setValue(null);
            return;
        }
        GankDataRepository.GetPaymentScale()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .safeSubscribe(new Observer<PaymentScaleData>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onNext(PaymentScaleData responseData) {
                        scaleLiveData.setValue(responseData);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        PaymentScaleData responseData = new PaymentScaleData();
                        if (e instanceof SocketTimeoutException) {
                            responseData.setCode(ResponseCode.ConnectTimeOut);
                            responseData.setMsg("连接超时，请重试");
                        } else {
                            responseData.setCode(ResponseCode.ServerNotResponding);
                            responseData.setMsg("服务器无响应，请重试");
                        }
                        scaleLiveData.setValue(responseData);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    //获取余额
    public void getWalletBalance() {

        if (!NetUtils.isNetConnected()) {
            walletBalanceLiveData.setValue(null);
            return;
        }
        GankDataRepository.getWalletBalance(BMWallet)
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

    public class PageData {

        public PageData() {
            balance.set("");
            paymentMoney.set("");
            poundage.set("0.00");
        }

        //比例
        private float scale;
        //余额
        private ObservableField<String> balance = new ObservableField<>();
        //付款金额
        private ObservableField<String> paymentMoney = new ObservableField<>();
        //手续费
        private ObservableField<String> poundage = new ObservableField<>();
        //承担保证金类型
        private MarginType marginType;
        //二维码ID
        private String codeId;

        public float getScale() {
            return scale;
        }

        public void setScale(float scale) {
            this.scale = scale;
        }

        public ObservableField<String> getBalance() {
            return balance;
        }

        public void setBalance(ObservableField<String> balance) {
            this.balance = balance;
        }

        public ObservableField<String> getPaymentMoney() {
            return paymentMoney;
        }

        public void setPaymentMoney(ObservableField<String> paymentMoney) {
            this.paymentMoney = paymentMoney;
        }

        public ObservableField<String> getPoundage() {
            return poundage;
        }

        public void setPoundage(ObservableField<String> poundage) {
            this.poundage = poundage;
        }

        public MarginType getMarginType() {
            return marginType;
        }

        public void setMarginType(MarginType marginType) {
            this.marginType = marginType;
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
