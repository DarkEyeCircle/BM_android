package com.askia.coremodel.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableField;

import com.askia.coremodel.datamodel.http.entities.BankCardInfoData;
import com.askia.coremodel.datamodel.http.entities.BaseResponseData;
import com.askia.coremodel.datamodel.http.entities.BillDetailsData;
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

public class TransferViewModel extends ViewModel {

    //转账类型
    public enum TransferType {
        TransferToBM(1),
        TransferToAlipay(2),
        TransferToBankCard(3);

        private final int type;

        TransferType(int type) {
            this.type = type;
        }

        public static TransferType getTransferType(int type) {
            switch (type) {
                case 1:
                    return TransferToBM;
                case 2:
                    return TransferToAlipay;
                case 3:
                    return TransferToBankCard;
                default:
                    return null;
            }
        }

        public int getType() {
            return type;
        }
    }

    private MutableLiveData<WithDrawalBalanceAndScaleData> poundageScaleLiveData = new MutableLiveData<>();
    private MutableLiveData<BaseResponseData> checkSecurityPwdLiveData = new MutableLiveData<>();
    private MutableLiveData<BaseResponseData> transferLiveData = new MutableLiveData<>();
    private MutableLiveData<BillDetailsData> transferHistoryLiveData = new MutableLiveData<>();
    private MutableLiveData<BankCardInfoData> cardInfoLiveData = new MutableLiveData<>();
    private final CompositeDisposable mDisposable = new CompositeDisposable();
    private PageData pageData = new PageData();
    private int page = 1;

    public MutableLiveData<WithDrawalBalanceAndScaleData> getPoundageScaleLiveData() {
        return poundageScaleLiveData;
    }

    public MutableLiveData<BaseResponseData> getCheckSecurityPwdLiveData() {
        return checkSecurityPwdLiveData;
    }

    public MutableLiveData<BaseResponseData> getTransferLiveData() {
        return transferLiveData;
    }


    public MutableLiveData<BillDetailsData> getTransferHistoryLiveData() {
        return transferHistoryLiveData;
    }

    public MutableLiveData<BankCardInfoData> getCardInfoLiveData() {
        return cardInfoLiveData;
    }

    public PageData getPageData() {
        return pageData;
    }

    public void updatePage(int page) {
        this.page = page + 1;
    }

    //转账历史
    public void getTransferHistory() {
        if (!NetUtils.isNetConnected()) {
            transferHistoryLiveData.setValue(null);
            return;
        }
        GankDataRepository.GetTransferHistory(this.page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .safeSubscribe(new Observer<BillDetailsData>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onNext(BillDetailsData responseData) {
                        transferHistoryLiveData.setValue(responseData);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        BillDetailsData billDetailsData = new BillDetailsData();
                        if (e instanceof SocketTimeoutException) {
                            billDetailsData.setCode(ResponseCode.ConnectTimeOut);
                            billDetailsData.setMsg("连接超时，请重试");
                        } else {
                            billDetailsData.setCode(ResponseCode.ServerNotResponding);
                            billDetailsData.setMsg("服务器无响应，请重试");
                        }
                        transferHistoryLiveData.setValue(billDetailsData);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    //转账
    public void transfer() {
        if (!NetUtils.isNetConnected()) {
            transferLiveData.setValue(null);
            return;
        }
        GankDataRepository.DoTransfer(pageData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .safeSubscribe(new Observer<BaseResponseData>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onNext(BaseResponseData responseData) {
                        transferLiveData.setValue(responseData);
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
                        transferLiveData.setValue(responseData);
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

    // 根据银行卡号获取银行卡信息
    public void getBankCardInfo() {
        if (!NetUtils.isNetConnected()) {
            cardInfoLiveData.setValue(null);
            return;
        }
        GankDataRepository.GetBankCardInfo(pageData.getBankAccount().get().replaceAll(" ", ""))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .safeSubscribe(new Observer<BankCardInfoData>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onNext(BankCardInfoData responseData) {
                        cardInfoLiveData.setValue(responseData);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        BankCardInfoData responseData = new BankCardInfoData();
                        if (e instanceof SocketTimeoutException) {
                            responseData.setCode(ResponseCode.ConnectTimeOut);
                            responseData.setMsg("连接超时，请重试");
                        } else {
                            responseData.setCode(ResponseCode.ServerNotResponding);
                            responseData.setMsg("服务器无响应，请重试");
                        }
                        cardInfoLiveData.setValue(responseData);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    //计算手续费
    public void calculatePoundage(float money) {
        String poundage = DecimalUtil.multiplyWithScale(pageData.getScale() + "", money + "", 2);
        pageData.getPoundage().set(poundage);
    }

    public void update(WithDrawalBalanceAndScaleData data) {
        pageData.getBalance().set(data.getObj().getBmBalance());
        switch (pageData.getTransferType()) {
            case TransferToBM:
                pageData.setScale(percentToFloat(data.getObj().getBmTransferBm()));
                break;
            case TransferToAlipay:
            case TransferToBankCard:
                pageData.setScale(percentToFloat(data.getObj().getBmTransferThird()));
                break;
        }
        pageData.getPoundageScale().set(pageData.getScale() * 100 + "");
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
            poundage.set("0.0");
        }

        //账户
        private ObservableField<String> BMaccount = new ObservableField<>();
        //余额
        private ObservableField<String> balance = new ObservableField<>();
        //提现类型
        private TransferType transferType;
        //转账金额
        private ObservableField<String> payAmount = new ObservableField<>();
        //手续费
        private ObservableField<String> poundage = new ObservableField<>();
        //提现比例
        private ObservableField<String> poundageScale = new ObservableField<>();
        //提现到百盟手续费比例
        private float scale;
        private ObservableField<String> bankAccount = new ObservableField<>();
        private ObservableField<String> bankName = new ObservableField<>();
        private ObservableField<String> openingBank = new ObservableField<>();
        private ObservableField<String> bankCity = new ObservableField<>();
        private ObservableField<String> alipayAccount = new ObservableField<>();
        private ObservableField<String> alipayName = new ObservableField<>();

        public ObservableField<String> getBMaccount() {
            return BMaccount;
        }

        public void setBMaccount(ObservableField<String> BMaccount) {
            this.BMaccount = BMaccount;
        }

        public ObservableField<String> getBalance() {
            return balance;
        }

        public void setBalance(ObservableField<String> balance) {
            this.balance = balance;
        }

        public TransferType getTransferType() {
            return transferType;
        }

        public void setTransferType(TransferType transferType) {
            this.transferType = transferType;
        }

        public ObservableField<String> getPayAmount() {
            return payAmount;
        }

        public void setPayAmount(ObservableField<String> payAmount) {
            this.payAmount = payAmount;
        }

        public ObservableField<String> getPoundage() {
            return poundage;
        }

        public void setPoundage(ObservableField<String> poundage) {
            this.poundage = poundage;
        }

        public ObservableField<String> getPoundageScale() {
            return poundageScale;
        }

        public void setPoundageScale(ObservableField<String> poundageScale) {
            this.poundageScale = poundageScale;
        }

        public float getScale() {
            return scale;
        }

        public void setScale(float scale) {
            this.scale = scale;
        }

        public ObservableField<String> getBankAccount() {
            return bankAccount;
        }

        public void setBankAccount(ObservableField<String> bankAccount) {
            this.bankAccount = bankAccount;
        }

        public ObservableField<String> getBankName() {
            return bankName;
        }

        public void setBankName(ObservableField<String> bankName) {
            this.bankName = bankName;
        }

        public ObservableField<String> getOpeningBank() {
            return openingBank;
        }

        public void setOpeningBank(ObservableField<String> openingBank) {
            this.openingBank = openingBank;
        }

        public ObservableField<String> getBankCity() {
            return bankCity;
        }

        public void setBankCity(ObservableField<String> bankCity) {
            this.bankCity = bankCity;
        }

        public ObservableField<String> getAlipayAccount() {
            return alipayAccount;
        }

        public void setAlipayAccount(ObservableField<String> alipayAccount) {
            this.alipayAccount = alipayAccount;
        }

        public ObservableField<String> getAlipayName() {
            return alipayName;
        }

        public void setAlipayName(ObservableField<String> alipayName) {
            this.alipayName = alipayName;
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mDisposable.clear();
    }
}
