package com.askia.coremodel.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.askia.coremodel.datamodel.http.entities.BillDetailsData;
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

public class BillsDetilsViewModel extends ViewModel {

    public enum BillType {
        //积分商城明细
        PointsMallDetails(1),
        //钱包总明细
        WalletDetails(2),
        //提现明细
        DrawMoneyDetails(3),
        //商户明细
        MerchantsDetails(4),
        //收款明细
        CollectionDetails(5),
        //充值明细
        RechargeDetails(6),
        //百盟钱包明细
        BmWalletDetails(7),
        //会员钱包明细
        MembersWalletDetails(8),;


        private final int type;

        BillType(int type) {
            this.type = type;
        }

        public static BillType getBillType(int type) {
            switch (type) {
                case 1:
                    return PointsMallDetails;
                case 2:
                    return WalletDetails;
                case 3:
                    return DrawMoneyDetails;
                case 4:
                    return MerchantsDetails;
                case 5:
                    return CollectionDetails;
                case 6:
                    return RechargeDetails;
                case 7:
                    return BmWalletDetails;
                case 8:
                    return MembersWalletDetails;
                default:
                    return null;
            }
        }

        public int getType() {
            return type;
        }
    }

    private MutableLiveData<BillDetailsData> billsListLiveData = new MutableLiveData<>();
    private final CompositeDisposable mDisposable = new CompositeDisposable();
    private BillType billType;
    private int page = 1;

    public MutableLiveData<BillDetailsData> getBillsListLiveData() {
        return billsListLiveData;
    }

    public void init(BillType billType) {
        this.billType = billType;
        getBills();
    }

    public void updatePage(int page) {
        this.page = page + 1;
    }

    public void getBills() {
        if (!NetUtils.isNetConnected()) {
            billsListLiveData.setValue(null);
            return;
        }
        GankDataRepository.GetBills(billType, this.page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .safeSubscribe(new Observer<BillDetailsData>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onNext(BillDetailsData responseData) {
                        billsListLiveData.setValue(responseData);
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
                        billsListLiveData.setValue(billDetailsData);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mDisposable.clear();
    }
}
