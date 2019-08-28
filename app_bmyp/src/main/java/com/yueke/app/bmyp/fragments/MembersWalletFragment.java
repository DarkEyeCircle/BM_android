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
import com.yueke.app.bmyp.databinding.WalletVipFragmentBinding;

@Route(path = ARouterPath.FragmentMembersWallet)
public class MembersWalletFragment extends BaseFragment {

    private WalletVipFragmentBinding mWalletVipFragmentBinding;
    private WalletBalanceViewModel mWalletBalanceViewModel;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mWalletVipFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.wallet_vip_fragment, container, false);
        initViewModel();
        initData();
        initViews();
        return mWalletVipFragmentBinding.getRoot();
    }


    private void initViewModel() {
        mWalletBalanceViewModel = ViewModelProviders.of(this).get(WalletBalanceViewModel.class);
        subscribeToModel(mWalletBalanceViewModel);
    }

    private void initData() {
        mWalletVipFragmentBinding.setHandlers(this);
        mWalletVipFragmentBinding.setPageData(mWalletBalanceViewModel.getPageData());
    }

    private void initViews() {
        mWalletVipFragmentBinding.mainLayout.setVisibility(View.GONE);
    }

    @Override
    public void onStart() {
        super.onStart();
        mWalletBalanceViewModel.getWalletBalance(WalletBalanceViewModel.BalanceType.MemberWallet);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void itemOnclick(View view) {
        switch (view.getId()) {
            //提现
            case R.id.bt_withdrawal:
                ARouter.getInstance().build(ARouterPath.ActivityWithDrawal)
                        .withInt(WithDrawalActivity.WithDrawalType, WithDrawalViewModel.WithDrawalType.MembersWithDrawa.getType())
                        .withString("title", "会员钱包提现")
                        .navigation();
                break;
            //资金明细
            case R.id.bt_financial_details:
                ARouter.getInstance().build(ARouterPath.ActivityBillsDetails)
                        .withInt("BillType", BillsDetilsViewModel.BillType.MembersWalletDetails.getType())
                        .withString("title", "资金明细")
                        .navigation();
                break;
        }
    }

    private void subscribeToModel(final WalletBalanceViewModel model) {
        //获取银行卡列表
        model.getWalletBalanceLiveData().observe(this, new Observer<WalletBalanceData>() {
            @Override
            public void onChanged(@Nullable WalletBalanceData responseData) {
                if (responseData == null) {
                    mWalletVipFragmentBinding.emptyView.show(false, getResources().getString(R.string.emptyView_mode_desc_fail_desc), getResources().getString(R.string.emptyView_mode_desc_fail_desc), getResources().getString(R.string.emptyView_mode_desc_retry), retryAgainListener);
                    return;
                }
                if (responseData.isError() || null == responseData.getObj()) {
                    mWalletVipFragmentBinding.emptyView.show(false, getResources().getString(R.string.emptyView_mode_desc_fail_title), null, getResources().getString(R.string.emptyView_mode_desc_retry), retryAgainListener);
                    resolveResponseErrorCode(responseData.getCode(), responseData.getMsg());
                    return;
                }
                mWalletVipFragmentBinding.emptyView.show(false);
                mWalletBalanceViewModel.update(responseData.getObj());
                mWalletVipFragmentBinding.mainLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    private View.OnClickListener retryAgainListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mWalletVipFragmentBinding.mainLayout.setVisibility(View.GONE);
            mWalletVipFragmentBinding.emptyView.show(true);
            mWalletVipFragmentBinding.emptyView.setTitleText("正在加载");
            mWalletBalanceViewModel.getWalletBalance(WalletBalanceViewModel.BalanceType.MemberWallet);
        }
    };
}
