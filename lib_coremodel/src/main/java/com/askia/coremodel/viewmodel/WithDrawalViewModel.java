package com.askia.coremodel.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

import com.askia.coremodel.datamodel.http.entities.BaseResponseData;
import com.askia.coremodel.datamodel.http.entities.RechargeOrderData;
import com.askia.coremodel.datamodel.http.entities.ResponseCode;
import com.askia.coremodel.datamodel.http.entities.WithDrawalBalanceAndScaleData;
import com.askia.coremodel.datamodel.http.repository.GankDataRepository;
import com.askia.coremodel.util.DecimalUtil;
import com.askia.coremodel.util.NetUtils;

import java.net.SocketTimeoutException;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class WithDrawalViewModel extends ViewModel {

    //到账时间
    public static final int AccountingDateType_7Days = 1;

    public static final int AccountingDateType_1Monthes = 2;

    //提现类型
    public enum WithDrawalType {
        //百盟钱包
        BMWithDrawa(1),
        //商铺钱包
        MerchantsWithDrawa(2),
        //会员钱包
        MembersWithDrawa(3);

        private final int type;

        WithDrawalType(int type) {
            this.type = type;
        }

        public static WithDrawalType getWithDrawalType(int type) {
            switch (type) {
                case 1:
                    return BMWithDrawa;
                case 2:
                    return MerchantsWithDrawa;
                case 3:
                    return MembersWithDrawa;
                default:
                    return null;
            }
        }

        public int getType() {
            return type;
        }
    }

    //提现账户
    public enum WithDrawalAccount {
        //微信
        Wechat(1),
        //支付宝
        AliPay(2),
        //银行卡
        Unionpay(3),
        //百盟钱包
        BMWallet(4);


        private final int type;

        WithDrawalAccount(int type) {
            this.type = type;
        }

        public static WithDrawalAccount getWithDrawalAccount(int type) {
            switch (type) {
                case 1:
                    return Wechat;
                case 2:
                    return AliPay;
                case 3:
                    return Unionpay;
                case 4:
                    return BMWallet;

                default:
                    return null;
            }
        }

        public int getType() {
            return type;
        }
    }

    private MutableLiveData<WithDrawalBalanceAndScaleData> poundageScaleLiveData = new MutableLiveData<>();
    private MutableLiveData<BaseResponseData> withDrawalLiveData = new MutableLiveData<>();
    private MutableLiveData<BaseResponseData> checkSecurityPwdLiveData = new MutableLiveData<>();
    private final CompositeDisposable mDisposable = new CompositeDisposable();
    private PageData pageData = new PageData();

    public MutableLiveData<WithDrawalBalanceAndScaleData> getPoundageScaleLiveData() {
        return poundageScaleLiveData;
    }

    public MutableLiveData<BaseResponseData> getWithDrawalLiveData() {
        return withDrawalLiveData;
    }

    public MutableLiveData<BaseResponseData> getCheckSecurityPwdLiveData() {
        return checkSecurityPwdLiveData;
    }

    public PageData getPageData() {
        return pageData;
    }

    //提现
    public void withDrawal() {
        if (!NetUtils.isNetConnected()) {
            withDrawalLiveData.setValue(null);
            return;
        }
        GankDataRepository.WithDrawal(pageData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .safeSubscribe(new Observer<BaseResponseData>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onNext(BaseResponseData responseData) {
                        withDrawalLiveData.setValue(responseData);
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
                        withDrawalLiveData.setValue(responseData);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    //获取手续费比例
    public void getBalanceAndScale() {
        if (!NetUtils.isNetConnected()) {
            poundageScaleLiveData.setValue(null);
            return;
        }
        GankDataRepository.GetWithDrawalBalanceAndScale()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .safeSubscribe(new Observer<WithDrawalBalanceAndScaleData>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onNext(WithDrawalBalanceAndScaleData responseData) {
                        poundageScaleLiveData.setValue(responseData);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        WithDrawalBalanceAndScaleData responseData = new WithDrawalBalanceAndScaleData();
                        if (e instanceof SocketTimeoutException) {
                            responseData.setCode(ResponseCode.ConnectTimeOut);
                            responseData.setMsg("连接超时，请重试");
                        } else {
                            responseData.setCode(ResponseCode.ServerNotResponding);
                            responseData.setMsg("服务器无响应，请重试");
                        }
                        poundageScaleLiveData.setValue(responseData);
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

    //计算手续费
    public void calculatePoundage(float money) {
        String poundage;
        switch (pageData.getWithDrawalType()) {
            case BMWithDrawa:
                //poundage = (pageData.getPoundageScaleToOhters() * 100) * money / 100;
                poundage = DecimalUtil.multiplyWithScale(pageData.getPoundageScaleToOhters() + "", money + "", 2);
                pageData.getPoundage().set(poundage);
                break;
            case MembersWithDrawa:
                if (pageData.getWithDrawalAccount() == WithDrawalAccount.BMWallet) {
                    //poundage = (pageData.getPoundageScaleToBM() * 100) * money / 100;
                    poundage = DecimalUtil.multiplyWithScale(pageData.getPoundageScaleToBM() + "", money + "", 2);
                } else {
                    //poundage = (pageData.getPoundageScaleToOhters() * 100) * money / 100;
                    poundage = DecimalUtil.multiplyWithScale(pageData.getPoundageScaleToOhters() + "", money + "", 2);
                }
                pageData.getPoundage().set(String.valueOf(poundage));
                break;
            case MerchantsWithDrawa:
                if (pageData.getWithDrawalAccount() == WithDrawalAccount.BMWallet) {
                    //poundage = (pageData.getPoundageScaleToBM() * 100) * money / 100;
                    poundage = DecimalUtil.multiplyWithScale(pageData.getPoundageScaleToBM() + "", money + "", 2);
                } else {
                    //poundage = (pageData.getPoundageScaleToOhters() * 100) * money / 100;
                    poundage = DecimalUtil.multiplyWithScale(pageData.getPoundageScaleToOhters() + "", money + "", 2);
                }
                pageData.getPoundage().set(String.valueOf(poundage));
                break;
        }

    }

    public void update(WithDrawalBalanceAndScaleData data) {
        switch (pageData.getWithDrawalType()) {
            case BMWithDrawa:
                pageData.getBalance().set(data.getObj().getBmBalance());
                pageData.setPoundageScaleToOhters(percentToFloat(data.getObj().getBmWithdrawThird()));
                break;
            case MembersWithDrawa:
                pageData.getBalance().set(data.getObj().getMemberBalance());
                pageData.setPoundageScaleToBM(percentToFloat(data.getObj().getMemberWithdrawBm()));
                pageData.setPoundageScaleToOhters(percentToFloat(data.getObj().getMemberWithdrawThird()));
                break;
            case MerchantsWithDrawa:
                pageData.getBalance().set(data.getObj().getMerchantBalance());
                pageData.setPoundageScaleToBM(0f);
                pageData.setPoundageScaleToOhters(percentToFloat(data.getObj().getShopWithdrawThird()));
                break;
        }
    }

    public void updateScale() {
        switch (pageData.getWithDrawalType()) {
            case BMWithDrawa:
                if (pageData.getPoundageScaleToOhters() == 0f) {
                    pageData.getHidePoundageLayout().set(true);
                } else {
                    pageData.getHidePoundageLayout().set(false);
                }
                pageData.getScale().set(pageData.getPoundageScaleToOhters() * 100 + "");
                break;
            case MembersWithDrawa:
                if (pageData.getWithDrawalAccount() == WithDrawalAccount.BMWallet) {
                    if (pageData.getPoundageScaleToBM() == 0f) {
                        pageData.getHidePoundageLayout().set(true);
                    } else {
                        pageData.getHidePoundageLayout().set(false);
                    }
                    pageData.getScale().set(pageData.getPoundageScaleToBM() * 100 + "");
                } else {
                    if (pageData.getPoundageScaleToOhters() == 0f) {
                        pageData.getHidePoundageLayout().set(true);
                    } else {
                        pageData.getHidePoundageLayout().set(false);
                    }
                    pageData.getScale().set(pageData.getPoundageScaleToOhters() * 100 + "");
                }
                break;
            case MerchantsWithDrawa:
                if (pageData.getWithDrawalAccount() == WithDrawalAccount.BMWallet) {
                    if (pageData.getPoundageScaleToBM() == 0f) {
                        pageData.getHidePoundageLayout().set(true);
                    } else {
                        pageData.getHidePoundageLayout().set(false);
                    }
                    pageData.getScale().set(pageData.getPoundageScaleToBM() * 100 + "");
                } else {
                    if (pageData.getPoundageScaleToOhters() == 0f) {
                        pageData.getHidePoundageLayout().set(true);
                    } else {
                        pageData.getHidePoundageLayout().set(false);
                    }
                    pageData.getScale().set(pageData.getPoundageScaleToOhters() * 100 + "");
                }
                break;
        }
    }


    //百分比转Float
    private float percentToFloat(String percentStr) {
        if (percentStr.endsWith("%")) {
            String floatStr = percentStr.substring(0, percentStr.indexOf("%"));
            return Float.valueOf(floatStr) / 100;
        }
        return Float.valueOf(percentStr);
    }

    public class PageData {

        public PageData() {
            poundage.set("0");
        }

        //余额
        private ObservableField<String> balance = new ObservableField<>();
        //提现类型
        private WithDrawalType withDrawalType;
        private WithDrawalAccount withDrawalAccount;
        //提现金额
        private ObservableField<String> payAmount = new ObservableField<>();
        //手续费
        private ObservableField<String> poundage = new ObservableField<>();
        //提现比例
        private ObservableField<String> scale = new ObservableField<>();
        //提现到百盟手续费比例
        private float poundageScaleToBM;
        //提现到第三方手续费比例-支付宝等
        private float poundageScaleToOhters;
        //到账时间
        private int accountingDateType = -1;
        //是否隐藏手续费
        private ObservableBoolean hidePoundageLayout = new ObservableBoolean();

        public WithDrawalType getWithDrawalType() {
            return withDrawalType;
        }

        public void setWithDrawalType(WithDrawalType withDrawalType) {
            this.withDrawalType = withDrawalType;
        }

        public WithDrawalAccount getWithDrawalAccount() {
            return withDrawalAccount;
        }

        public void setWithDrawalAccount(WithDrawalAccount withDrawalAccount) {
            this.withDrawalAccount = withDrawalAccount;
        }

        public ObservableField<String> getPayAmount() {
            return payAmount;
        }

        public void setPayAmount(ObservableField<String> payAmount) {
            this.payAmount = payAmount;
        }

        public int getAccountingDateType() {
            return accountingDateType;
        }

        public void setAccountingDateType(int accountingDateType) {
            this.accountingDateType = accountingDateType;
        }

        public ObservableField<String> getPoundage() {
            return poundage;
        }

        public void setPoundage(ObservableField<String> poundage) {
            this.poundage = poundage;
        }

        public ObservableField<String> getBalance() {
            return balance;
        }

        public void setBalance(ObservableField<String> balance) {
            this.balance = balance;
        }

        public float getPoundageScaleToBM() {
            return poundageScaleToBM;
        }

        public void setPoundageScaleToBM(float poundageScaleToBM) {
            this.poundageScaleToBM = poundageScaleToBM;
        }

        public float getPoundageScaleToOhters() {
            return poundageScaleToOhters;
        }

        public void setPoundageScaleToOhters(float poundageScaleToOhters) {
            this.poundageScaleToOhters = poundageScaleToOhters;
        }

        public ObservableField<String> getScale() {
            return scale;
        }

        public void setScale(ObservableField<String> scale) {
            this.scale = scale;
        }

        public ObservableBoolean getHidePoundageLayout() {
            return hidePoundageLayout;
        }

        public void setHidePoundageLayout(ObservableBoolean hidePoundageLayout) {
            this.hidePoundageLayout = hidePoundageLayout;
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mDisposable.clear();
    }
}
