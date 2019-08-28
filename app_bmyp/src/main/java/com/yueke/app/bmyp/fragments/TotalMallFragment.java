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
import com.askia.coremodel.datamodel.http.entities.WalletBalanceData;
import com.askia.coremodel.viewmodel.BillsDetilsViewModel;
import com.askia.coremodel.viewmodel.WalletBalanceViewModel;
import com.yueke.app.bmyp.R;
import com.yueke.app.bmyp.databinding.TotalMallFrgmentBinding;

@Route(path = ARouterPath.FragmentTotalMall)
public class TotalMallFragment extends BaseFragment {

    private TotalMallFrgmentBinding mTotalMallFrgmentBinding;
    private WalletBalanceViewModel mWalletBalanceViewModel;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mTotalMallFrgmentBinding = DataBindingUtil.inflate(inflater, R.layout.total_mall_frgment, container, false);
        initViewModel();
        initData();
        initViews();
        return mTotalMallFrgmentBinding.getRoot();
    }


    private void initViewModel() {
        mWalletBalanceViewModel = ViewModelProviders.of(this).get(WalletBalanceViewModel.class);
        subscribeToModel(mWalletBalanceViewModel);
    }

    private void initData() {
        mTotalMallFrgmentBinding.setHandlers(this);
        mTotalMallFrgmentBinding.setPageData(mWalletBalanceViewModel.getPageData());
        mWalletBalanceViewModel.getWalletBalance(WalletBalanceViewModel.BalanceType.MallPoints);
    }

    private void initViews() {
        mTotalMallFrgmentBinding.mainLayout.setVisibility(View.GONE);
    }

    public void itemOnclick(View view) {
        switch (view.getId()) {
            //积分明细
            case R.id.bt_integral_details:
                ARouter.getInstance().build(ARouterPath.ActivityBillsDetails)
                        .withInt("BillType", BillsDetilsViewModel.BillType.PointsMallDetails.getType())
                        .withString("title", "积分商城明细")
                        .navigation();
                break;
        }
    }

    private void subscribeToModel(final WalletBalanceViewModel model) {
        model.getWalletBalanceLiveData().observe(this, new Observer<WalletBalanceData>() {
            @Override
            public void onChanged(@Nullable WalletBalanceData responseData) {
                if (responseData == null) {
                    mTotalMallFrgmentBinding.emptyView.show(false, getResources().getString(R.string.emptyView_mode_desc_fail_desc), getResources().getString(R.string.emptyView_mode_desc_fail_desc), getResources().getString(R.string.emptyView_mode_desc_retry), retryAgainListener);
                    return;
                }
                if (responseData.isError() || null == responseData.getObj()) {
                    mTotalMallFrgmentBinding.emptyView.show(false, getResources().getString(R.string.emptyView_mode_desc_fail_title), null, getResources().getString(R.string.emptyView_mode_desc_retry), retryAgainListener);
                    resolveResponseErrorCode(responseData.getCode(), responseData.getMsg());
                    return;
                }
                mTotalMallFrgmentBinding.emptyView.show(false);
                mWalletBalanceViewModel.update(responseData.getObj());
                mTotalMallFrgmentBinding.mainLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    private View.OnClickListener retryAgainListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mTotalMallFrgmentBinding.mainLayout.setVisibility(View.GONE);
            mTotalMallFrgmentBinding.emptyView.show(true);
            mTotalMallFrgmentBinding.emptyView.setTitleText("正在加载");
            mWalletBalanceViewModel.getWalletBalance(WalletBalanceViewModel.BalanceType.MallPoints);
        }
    };

    public void updateData() {
        mTotalMallFrgmentBinding.mainLayout.setVisibility(View.GONE);
        mTotalMallFrgmentBinding.emptyView.show(true);
        mTotalMallFrgmentBinding.emptyView.setTitleText("正在加载");
        mWalletBalanceViewModel.getWalletBalance(WalletBalanceViewModel.BalanceType.MallPoints);
    }

}
