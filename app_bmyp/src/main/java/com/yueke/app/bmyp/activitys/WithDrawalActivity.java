package com.yueke.app.bmyp.activitys;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.askia.common.base.ARouterPath;
import com.askia.common.base.BaseActivity;
import com.askia.common.util.InputTools;
import com.askia.common.util.ToastUtils;
import com.askia.coremodel.GlobalUserDataStore;
import com.askia.coremodel.datamodel.http.entities.BaseResponseData;
import com.askia.coremodel.datamodel.http.entities.WithDrawalBalanceAndScaleData;
import com.askia.coremodel.util.DecimalUtil;
import com.askia.coremodel.viewmodel.WithDrawalViewModel;
import com.askia.keyboard.OnPasswordInputFinish;
import com.askia.keyboard.widget.PopEnterPassword;
import com.yueke.app.bmyp.R;
import com.yueke.app.bmyp.databinding.WithdrawalActivityBinding;
import com.yueke.app.bmyp.widgets.PayAmountEdittext;

@Route(path = ARouterPath.ActivityWithDrawal)
public class WithDrawalActivity extends BaseActivity {

    public static final String WithDrawalType = "WithDrawalType";
    private WithdrawalActivityBinding mWithdrawalActivityBinding;
    private WithDrawalViewModel mWithDrawalViewModel;
    private PopEnterPassword popEnterPassword = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDataBinding();
        initTopBar();
        initViewModel();
        initData();
        initViews();
    }

    private void initViews() {
        mWithdrawalActivityBinding.mainLayout.setVisibility(View.GONE);
        mWithdrawalActivityBinding.etPayAmount.setButton(mWithdrawalActivityBinding.btPay);
        mWithdrawalActivityBinding.etPayAmount.setInputAccountListener(new PayAmountEdittext.InputAccountListener() {
            @Override
            public void inputAccount(float value) {
                mWithDrawalViewModel.calculatePoundage(value);
            }
        });
        switch (mWithDrawalViewModel.getPageData().getWithDrawalType()) {
            case BMWithDrawa:
                mWithdrawalActivityBinding.bmLayout.setVisibility(View.GONE);
                mWithdrawalActivityBinding.btBM.setVisibility(View.GONE);
                mWithdrawalActivityBinding.btAli.setChecked(true);
                mWithDrawalViewModel.getPageData().setWithDrawalAccount(WithDrawalViewModel.WithDrawalAccount.AliPay);
                break;
            case MerchantsWithDrawa:
                mWithdrawalActivityBinding.bmLayout.setVisibility(View.VISIBLE);
                mWithdrawalActivityBinding.btBM.setVisibility(View.VISIBLE);
                mWithdrawalActivityBinding.btBM.setChecked(true);
                mWithDrawalViewModel.getPageData().setWithDrawalAccount(WithDrawalViewModel.WithDrawalAccount.BMWallet);
                break;
            case MembersWithDrawa:
                mWithdrawalActivityBinding.bmLayout.setVisibility(View.VISIBLE);
                mWithdrawalActivityBinding.btBM.setVisibility(View.VISIBLE);
                mWithdrawalActivityBinding.btBM.setChecked(true);
                mWithDrawalViewModel.getPageData().setWithDrawalAccount(WithDrawalViewModel.WithDrawalAccount.BMWallet);
                break;
        }

    }

    private void initDataBinding() {
        mWithdrawalActivityBinding = DataBindingUtil.setContentView(this, R.layout.withdrawal_activity);
    }

    //初始化TopBar
    private void initTopBar() {
        //标题栏返回键监听
        mWithdrawalActivityBinding.topBar.setTitle(getIntent().getStringExtra("title")).getPaint().setFakeBoldText(true);
        mWithdrawalActivityBinding.topBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //解决返回键不居中问题
        mWithdrawalActivityBinding.topBar.setGravity(Gravity.CENTER_VERTICAL);
    }

    //初始化ViewModle
    private void initViewModel() {
        mWithDrawalViewModel = ViewModelProviders.of(WithDrawalActivity.this).get(WithDrawalViewModel.class);
        subscribeToModel(mWithDrawalViewModel);
    }

    //初始化页面数据
    private void initData() {
        mWithdrawalActivityBinding.setHandlers(this);
        mWithdrawalActivityBinding.setPageData(mWithDrawalViewModel.getPageData());
        int type = getIntent().getIntExtra(WithDrawalType, 0);
        mWithDrawalViewModel.getPageData().setWithDrawalType(WithDrawalViewModel.WithDrawalType.getWithDrawalType(type));
        mWithDrawalViewModel.getBalanceAndScale();
    }

    //提现到百盟钱包
    public void withDrawalToBM(View view) {
        mWithdrawalActivityBinding.btBM.setChecked(true);
        mWithDrawalViewModel.getPageData().setWithDrawalAccount(WithDrawalViewModel.WithDrawalAccount.BMWallet);
        if (!TextUtils.isEmpty(mWithDrawalViewModel.getPageData().getPayAmount().get())) {
            mWithDrawalViewModel.calculatePoundage(Float.valueOf(mWithDrawalViewModel.getPageData().getPayAmount().get()));
        }
        mWithDrawalViewModel.updateScale();
    }

    //提现到支付宝
    public void withDrawalToAlipay(View view) {
        mWithdrawalActivityBinding.btAli.setChecked(true);
        mWithDrawalViewModel.getPageData().setWithDrawalAccount(WithDrawalViewModel.WithDrawalAccount.AliPay);
        if (!TextUtils.isEmpty(mWithDrawalViewModel.getPageData().getPayAmount().get())) {
            mWithDrawalViewModel.calculatePoundage(Float.valueOf(mWithDrawalViewModel.getPageData().getPayAmount().get()));
        }
        mWithDrawalViewModel.updateScale();
    }

    //提现到微信
//    public void withDrawalToWechat(View view) {
//        mWithdrawalActivityBinding.btWechat.setChecked(true);
//        mWithDrawalViewModel.getPageData().setWithDrawalAccount(WithDrawalViewModel.WithDrawalAccount.Wechat);
//        mWithDrawalViewModel.calculatePoundage(Float.valueOf(mWithDrawalViewModel.getPageData().getPayAmount().get()));
//    }

    //提现到银行卡
    public void withDrawalToBankCard(View view) {
        mWithdrawalActivityBinding.btBankCard.setChecked(true);
        mWithDrawalViewModel.getPageData().setWithDrawalAccount(WithDrawalViewModel.WithDrawalAccount.Unionpay);
        if (!TextUtils.isEmpty(mWithDrawalViewModel.getPageData().getPayAmount().get())) {
            mWithDrawalViewModel.calculatePoundage(Float.valueOf(mWithDrawalViewModel.getPageData().getPayAmount().get()));
        }
        mWithDrawalViewModel.updateScale();
    }

    //提现
    public void doWithDrawal(View view) {
        //未设置支付宝账户
        if (mWithDrawalViewModel.getPageData().getWithDrawalAccount() == WithDrawalViewModel.WithDrawalAccount.AliPay) {
            if (!GlobalUserDataStore.getInstance().isBindAlippayAccount()) {
                ARouter.getInstance().build(ARouterPath.ActivityBindingAlipayAccount).navigation();
                return;
            }
        }
        //未设置银行卡
        if (mWithDrawalViewModel.getPageData().getWithDrawalAccount() == WithDrawalViewModel.WithDrawalAccount.Unionpay) {
            if (!GlobalUserDataStore.getInstance().isBindedBankCard()) {
                ARouter.getInstance().build(ARouterPath.ActivityAddBankCard).navigation();
                return;
            }
        }
        //未设置安全密码
        if (!GlobalUserDataStore.getInstance().isHaveSecurityPwd()) {
            ARouter.getInstance().build(ARouterPath.ActivitySettingSecurityPwd).navigation();
            return;
        }
        if (mWithDrawalViewModel.getPageData().getWithDrawalAccount() == null) {
            ToastUtils.info("请选择账户类型");
            return;
        }
        if (mWithDrawalViewModel.getPageData().getAccountingDateType() <= 0) {
            ToastUtils.info("请选择到账时间");
            return;
        }
        try {
            String money = mWithDrawalViewModel.getPageData().getPayAmount().get();
            String poundage = mWithDrawalViewModel.getPageData().getPoundage().get();
            if (TextUtils.isEmpty(money)) {
                ToastUtils.info("请设置金额");
                return;
            }
            if (Float.valueOf(money) <= 0f) {
                ToastUtils.info("请输入正确金额");
                return;
            }
            String totalMoney = DecimalUtil.add(money, poundage);
            if (Float.valueOf(totalMoney) > Float.valueOf(mWithDrawalViewModel.getPageData().getBalance().get())) {
                ToastUtils.info("提现金额不可大于可用余额");
                return;
            }
            showPayKeyBoard();
        } catch (Throwable e) {
            ToastUtils.info("请输入正确金额");
        }

        showPayKeyBoard();
    }

    //显示支付键盘
    public void showPayKeyBoard() {
        InputTools.HideKeyboard(mWithdrawalActivityBinding.layoutContent);
        if (null == popEnterPassword) {
            popEnterPassword = new PopEnterPassword(this, new OnPasswordInputFinish() {
                @Override
                public void inputFinish(String password) {
                    popEnterPassword.close();
                    showLogadingDialog();
                    mWithDrawalViewModel.checkSecurityPwd(password);
                }
            });
        }
        if (!mWithDrawalViewModel.getPageData().getHidePoundageLayout().get()) {
            String poundage = String.valueOf(Float.valueOf(mWithDrawalViewModel.getPageData().getPoundage().get()));
            String payMoney = String.valueOf(Float.valueOf(mWithDrawalViewModel.getPageData().getPayAmount().get()) + Float.valueOf(poundage));
            popEnterPassword.show(payMoney, poundage + "手续费", findViewById(R.id.layoutContent));
        } else {
            String payMoney = mWithDrawalViewModel.getPageData().getPayAmount().get();
            popEnterPassword.show(payMoney, null, findViewById(R.id.layoutContent));
        }
    }

    //到账时间
    public void selectAccountingDate(View view) {
        switch (view.getId()) {
            case R.id.bt7day:
                mWithdrawalActivityBinding.bt7day.setChecked(true);
                mWithDrawalViewModel.getPageData().setAccountingDateType(WithDrawalViewModel.AccountingDateType_7Days);
                break;
            case R.id.bt30day:
                mWithdrawalActivityBinding.bt30day.setChecked(true);
                mWithDrawalViewModel.getPageData().setAccountingDateType(WithDrawalViewModel.AccountingDateType_1Monthes);
                break;
        }
    }

    private void subscribeToModel(final WithDrawalViewModel model) {
        //手续费比例
        model.getPoundageScaleLiveData().observe(this, new Observer<WithDrawalBalanceAndScaleData>() {
            @Override
            public void onChanged(@Nullable WithDrawalBalanceAndScaleData responseData) {
                if (responseData == null) {
                    mWithdrawalActivityBinding.emptyView.show(false, getResources().getString(R.string.emptyView_mode_desc_fail_desc), getResources().getString(R.string.emptyView_mode_desc_fail_desc), getResources().getString(R.string.emptyView_mode_desc_retry), retryAgainListener);
                    return;
                }
                if (responseData.isError()) {
                    mWithdrawalActivityBinding.emptyView.show(false, getResources().getString(R.string.emptyView_mode_desc_fail_title), null, getResources().getString(R.string.emptyView_mode_desc_retry), retryAgainListener);
                    resolveResponseErrorCode(responseData.getCode(), responseData.getMsg());
                    return;
                }

                mWithdrawalActivityBinding.emptyView.show(false);
                mWithdrawalActivityBinding.mainLayout.setVisibility(View.VISIBLE);
                mWithDrawalViewModel.update(responseData);
                mWithDrawalViewModel.updateScale();
            }
        });
        //提现
        model.getWithDrawalLiveData().observe(this, new Observer<BaseResponseData>() {
            @Override
            public void onChanged(@Nullable BaseResponseData responseData) {
                closeLogadingDialog();
                if (responseData == null) {
                    ToastUtils.showNetNoConnected();
                    return;
                }
                if (responseData.isError()) {
                    ARouter.getInstance().build(ARouterPath.ActivityTradingState).
                            withString(TradingStateActivity.Intent_TradingType, TradingStateActivity.Intent_TradingType_Operation)
                            .withBoolean(TradingStateActivity.Intent_TradingState, false)
                            .withString(TradingStateActivity.Intent_Trading_Error_Msg, responseData.getMsg())
                            .navigation();
                    finish();
                    return;
                }
                ARouter.getInstance().build(ARouterPath.ActivityTradingState).
                        withString(TradingStateActivity.Intent_TradingType, TradingStateActivity.Intent_TradingType_Operation)
                        .withBoolean(TradingStateActivity.Intent_TradingState, true)
                        .withString(TradingStateActivity.Intent_TradingMoney, mWithDrawalViewModel.getPageData().getPayAmount().get())
                        .navigation();
                finish();
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
                mWithDrawalViewModel.withDrawal();
            }
        });
    }

    private View.OnClickListener retryAgainListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mWithdrawalActivityBinding.mainLayout.setVisibility(View.GONE);
            mWithdrawalActivityBinding.emptyView.show(true);
            mWithdrawalActivityBinding.emptyView.setTitleText("正在加载");
            mWithDrawalViewModel.getBalanceAndScale();
        }
    };

}
