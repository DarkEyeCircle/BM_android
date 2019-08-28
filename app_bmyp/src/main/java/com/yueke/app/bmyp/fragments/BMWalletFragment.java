package com.yueke.app.bmyp.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.askia.common.base.ARouterPath;
import com.askia.common.base.BaseFragment;
import com.askia.coremodel.GlobalUserDataStore;
import com.askia.coremodel.datamodel.http.entities.WalletBalanceData;
import com.askia.coremodel.viewmodel.BillsDetilsViewModel;
import com.askia.coremodel.viewmodel.WalletBalanceViewModel;
import com.askia.coremodel.viewmodel.WithDrawalViewModel;
import com.yueke.app.bmyp.R;
import com.yueke.app.bmyp.activitys.WithDrawalActivity;
import com.yueke.app.bmyp.databinding.WalletBaimengFragmentBinding;

@Route(path = ARouterPath.FragmentBMWallet)
public class BMWalletFragment extends BaseFragment {

    private WalletBaimengFragmentBinding mWalletBaimengFragmentBinding;
    private WalletBalanceViewModel mWalletBalanceViewModel;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mWalletBaimengFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.wallet_baimeng_fragment, container, false);
        initViewModel();
        initData();
        initViews();
        return mWalletBaimengFragmentBinding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        mWalletBalanceViewModel.getWalletBalance(WalletBalanceViewModel.BalanceType.BMWallet);
    }

    private void initViewModel() {
        mWalletBalanceViewModel = ViewModelProviders.of(this).get(WalletBalanceViewModel.class);
        subscribeToModel(mWalletBalanceViewModel);
    }

    private void initData() {
        mWalletBaimengFragmentBinding.setHandlers(this);
        mWalletBaimengFragmentBinding.setPageData(mWalletBalanceViewModel.getPageData());
    }

    private void initViews() {
        mWalletBaimengFragmentBinding.mainLayout.setVisibility(View.GONE);
    }

    public void itemOnclick(View view) {
        switch (view.getId()) {
            //充值
            case R.id.bt_recharge:
                ARouter.getInstance().build(ARouterPath.ActivityRecharge).navigation();
                break;
            //提现
            case R.id.bt_withdrawal:
                ARouter.getInstance().build(ARouterPath.ActivityWithDrawal)
                        .withInt(WithDrawalActivity.WithDrawalType, WithDrawalViewModel.WithDrawalType.BMWithDrawa.getType())
                        .withString("title", "百盟钱包提现")
                        .navigation();
                break;
            //转账
            case R.id.bt_transfer:
                ARouter.getInstance().build(ARouterPath.ActivityTransfer).navigation();
                break;
            //资金明细
            case R.id.bt_financial_details:
                ARouter.getInstance().build(ARouterPath.ActivityBillsDetails)
                        .withInt("BillType", BillsDetilsViewModel.BillType.BmWalletDetails.getType())
                        .withString("title", "资金明细")
                        .navigation();
                break;
            //兑换卡券
            case R.id.bt_exchange_card:
                ARouter.getInstance().build(ARouterPath.ActivityCardBag).navigation();
                break;
        }
    }

    private void subscribeToModel(final WalletBalanceViewModel model) {
        //获取余额
        model.getWalletBalanceLiveData().observe(this, new Observer<WalletBalanceData>() {
            @Override
            public void onChanged(@Nullable WalletBalanceData responseData) {
                if (responseData == null) {
                    mWalletBaimengFragmentBinding.emptyView.show(false, getResources().getString(R.string.emptyView_mode_desc_fail_desc), getResources().getString(R.string.emptyView_mode_desc_fail_desc), getResources().getString(R.string.emptyView_mode_desc_retry), retryAgainListener);
                    return;
                }
                if (responseData.isError() || null == responseData.getObj()) {
                    mWalletBaimengFragmentBinding.emptyView.show(false, getResources().getString(R.string.emptyView_mode_desc_fail_title), null, getResources().getString(R.string.emptyView_mode_desc_retry), retryAgainListener);
                    resolveResponseErrorCode(responseData.getCode(), responseData.getMsg());
                    return;
                }
                mWalletBaimengFragmentBinding.emptyView.show(false);
                mWalletBalanceViewModel.update(responseData.getObj());
                mWalletBaimengFragmentBinding.mainLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    private View.OnClickListener retryAgainListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mWalletBaimengFragmentBinding.mainLayout.setVisibility(View.GONE);
            mWalletBaimengFragmentBinding.emptyView.show(true);
            mWalletBaimengFragmentBinding.emptyView.setTitleText("正在加载");
            mWalletBalanceViewModel.getWalletBalance(WalletBalanceViewModel.BalanceType.BMWallet);
        }
    };

}
