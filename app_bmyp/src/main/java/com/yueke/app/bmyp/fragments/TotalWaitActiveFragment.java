package com.yueke.app.bmyp.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.askia.common.base.ARouterPath;
import com.askia.common.base.BaseFragment;
import com.askia.common.util.InputTools;
import com.askia.common.util.ToastUtils;
import com.askia.coremodel.GlobalUserDataStore;
import com.askia.coremodel.datamodel.http.entities.BaseResponseData;
import com.askia.coremodel.datamodel.http.entities.WalletBalanceData;
import com.askia.coremodel.viewmodel.WalletBalanceViewModel;
import com.askia.keyboard.OnPasswordInputFinish;
import com.askia.keyboard.widget.PopEnterPassword;
import com.yueke.app.bmyp.R;
import com.yueke.app.bmyp.activitys.IntegralAccountActivity;
import com.yueke.app.bmyp.databinding.TotalWaitActiveFrgmentBinding;
import com.yueke.app.bmyp.widgets.PayAmountEdittext;

@Route(path = ARouterPath.FragmentTotalWaitActive)
public class TotalWaitActiveFragment extends BaseFragment {

    private TotalWaitActiveFrgmentBinding mTotalWaitActiveFrgmentBinding;
    private WalletBalanceViewModel mWalletBalanceViewModel;
    private PopEnterPassword popEnterPassword = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mTotalWaitActiveFrgmentBinding = DataBindingUtil.inflate(inflater, R.layout.total_wait_active_frgment, container, false);
        initViewModel();
        initData();
        initViews();
        return mTotalWaitActiveFrgmentBinding.getRoot();
    }


    private void initViewModel() {
        mWalletBalanceViewModel = ViewModelProviders.of(this).get(WalletBalanceViewModel.class);
        subscribeToModel(mWalletBalanceViewModel);
    }

    private void initData() {
        mTotalWaitActiveFrgmentBinding.setHandlers(this);
        mTotalWaitActiveFrgmentBinding.setPageData(mWalletBalanceViewModel.getPageData());
        mWalletBalanceViewModel.getWalletBalance(WalletBalanceViewModel.BalanceType.WaitActiviePoints);
    }

    private void initViews() {
        mTotalWaitActiveFrgmentBinding.mainLayout.setVisibility(View.GONE);
        mTotalWaitActiveFrgmentBinding.etPayAmount.setButton(mTotalWaitActiveFrgmentBinding.btActive);
        mTotalWaitActiveFrgmentBinding.etPayAmount.hideDecimal();
        mTotalWaitActiveFrgmentBinding.etPayAmount.setInputAccountListener(new PayAmountEdittext.InputAccountListener() {
            @Override
            public void inputAccount(float value) {
                mWalletBalanceViewModel.calculatePoundage(value);
            }
        });
    }

    //激活积分
    public void jumpToActivate(View view) {
        if (!GlobalUserDataStore.getInstance().isHaveSecurityPwd()) {
            ARouter.getInstance().build(ARouterPath.ActivitySettingSecurityPwd).navigation();
            return;
        }
        try {
            String money = mWalletBalanceViewModel.getPageData().getNeedActiveIntegral().get();
            if (TextUtils.isEmpty(money)) {
                ToastUtils.info("请设置需要激活的积分");
                return;
            }
            if (Float.valueOf(money) <= 0f) {
                ToastUtils.info("请输入正确积分金额");
                return;
            }
            showPayKeyBoard();
        } catch (Throwable e) {
            ToastUtils.info("请输入正确积分金额");
        }
    }

    //显示支付键盘
    public void showPayKeyBoard() {
        InputTools.HideKeyboard(mTotalWaitActiveFrgmentBinding.mainLayout);
        if (null == popEnterPassword) {
            popEnterPassword = new PopEnterPassword(getActivity(), new OnPasswordInputFinish() {
                @Override
                public void inputFinish(String password) {
                    popEnterPassword.close();
                    showLogadingDialog();
                    mWalletBalanceViewModel.checkSecurityPwd(password);
                }
            });
        }
        String payMoney = mWalletBalanceViewModel.getPageData().getPoundage().get();
        popEnterPassword.show(payMoney, null, getActivity().findViewById(R.id.layoutContent));
    }

    //积分数据输入框内容变化
    public void moneyEdittextChanged(CharSequence s, int start, int before, int count) {
        if (TextUtils.isEmpty(s.toString())) {
            mWalletBalanceViewModel.getPageData().getPoundage().set("0");
            mTotalWaitActiveFrgmentBinding.btActive.setEnabled(false);
            return;
        }
        mWalletBalanceViewModel.calculatePoundage(Long.valueOf(s.toString()));
        if (s.length() > 0) {
            float total = Float.valueOf(mWalletBalanceViewModel.getPageData().getBalance().get());
            if (Long.valueOf(s.toString()) > total) {
                mTotalWaitActiveFrgmentBinding.btActive.setEnabled(false);
                return;
            }
            mTotalWaitActiveFrgmentBinding.btActive.setEnabled(true);
        } else {
            mTotalWaitActiveFrgmentBinding.btActive.setEnabled(false);
        }
    }


    private void subscribeToModel(final WalletBalanceViewModel model) {
        model.getWalletBalanceLiveData().observe(this, new Observer<WalletBalanceData>() {
            @Override
            public void onChanged(@Nullable WalletBalanceData responseData) {
                if (responseData == null) {
                    mTotalWaitActiveFrgmentBinding.emptyView.show(false, getResources().getString(R.string.emptyView_mode_desc_fail_desc), getResources().getString(R.string.emptyView_mode_desc_fail_desc), getResources().getString(R.string.emptyView_mode_desc_retry), retryAgainListener);
                    return;
                }
                if (responseData.isError() || null == responseData.getObj()) {
                    mTotalWaitActiveFrgmentBinding.emptyView.show(false, getResources().getString(R.string.emptyView_mode_desc_fail_title), null, getResources().getString(R.string.emptyView_mode_desc_retry), retryAgainListener);
                    resolveResponseErrorCode(responseData.getCode(), responseData.getMsg());
                    return;
                }
                if (Float.valueOf(responseData.getObj().getBalance()) <= 0) {
                    mTotalWaitActiveFrgmentBinding.emptyView.show(false);
                    mTotalWaitActiveFrgmentBinding.emptyView.setTitleText("你目前暂无待激活积分，无法激活");
                    return;
                }
                mTotalWaitActiveFrgmentBinding.emptyView.show(false);
                mWalletBalanceViewModel.update(responseData.getObj());
                mTotalWaitActiveFrgmentBinding.mainLayout.setVisibility(View.VISIBLE);
            }
        });

        //激活积分
        model.getActtiveIntegralLiveData().observe(this, new Observer<WalletBalanceData>() {
            @Override
            public void onChanged(@Nullable WalletBalanceData responseData) {
                closeLogadingDialog();
                if (responseData == null) {
                    ToastUtils.showNetNoConnected();
                    return;
                }
                if (responseData.isError() || null == responseData.getObj()) {
                    resolveResponseErrorCode(responseData.getCode(), responseData.getMsg());
                    return;
                }
                mWalletBalanceViewModel.update(responseData.getObj());
                mTotalWaitActiveFrgmentBinding.btActive.setEnabled(false);
                ToastUtils.info("激活成功", Toast.LENGTH_SHORT);
                mTotalWaitActiveFrgmentBinding.mainLayout.setVisibility(View.GONE);
                mTotalWaitActiveFrgmentBinding.emptyView.show(true);
                mTotalWaitActiveFrgmentBinding.emptyView.setTitleText("正在加载");
                mWalletBalanceViewModel.getWalletBalance(WalletBalanceViewModel.BalanceType.WaitActiviePoints);
                //更新商城积分
                ((IntegralAccountActivity) getActivity()).updateMallTotal();
            }
        });
        //验证安全密码
        model.getCheckSecurityPwdLiveData().observe(this, new Observer<BaseResponseData>() {
            @Override
            public void onChanged(@Nullable BaseResponseData responseData) {
                if (responseData == null) {
                    closeLogadingDialog();
                    ToastUtils.showNetNoConnected();
                    return;
                }
                if (responseData.isError()) {
                    closeLogadingDialog();
                    resolveResponseErrorCode(responseData.getCode(), responseData.getMsg());
                    return;
                }
                mWalletBalanceViewModel.activieIntegral();
            }
        });

    }

    private View.OnClickListener retryAgainListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mTotalWaitActiveFrgmentBinding.mainLayout.setVisibility(View.GONE);
            mTotalWaitActiveFrgmentBinding.emptyView.show(true);
            mTotalWaitActiveFrgmentBinding.emptyView.setTitleText("正在加载");
            mWalletBalanceViewModel.getWalletBalance(WalletBalanceViewModel.BalanceType.WaitActiviePoints);
        }
    };
}
