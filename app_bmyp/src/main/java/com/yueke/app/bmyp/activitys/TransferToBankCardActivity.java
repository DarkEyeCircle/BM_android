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
import com.askia.coremodel.datamodel.http.entities.BankCardInfoData;
import com.askia.coremodel.datamodel.http.entities.BaseResponseData;
import com.askia.coremodel.datamodel.http.entities.WithDrawalBalanceAndScaleData;
import com.askia.coremodel.util.DecimalUtil;
import com.askia.coremodel.util.RegexUtils;
import com.askia.coremodel.viewmodel.TransferViewModel;
import com.askia.keyboard.OnPasswordInputFinish;
import com.askia.keyboard.widget.PopEnterPassword;
import com.yueke.app.bmyp.R;
import com.yueke.app.bmyp.databinding.TransferToBankcardActivityBinding;
import com.yueke.app.bmyp.widgets.PayAmountEdittext;

@Route(path = ARouterPath.ActivityTransferToBankCard)
public class TransferToBankCardActivity extends BaseActivity {
    public static final String WithDrawalType = "TransferType";
    private TransferToBankcardActivityBinding mTransferToBankcardActivityBinding;
    private TransferViewModel mTransferViewModel;
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

    private void initDataBinding() {
        mTransferToBankcardActivityBinding = DataBindingUtil.setContentView(this, R.layout.transfer_to_bankcard_activity);
    }

    private void initTopBar() {
        mTransferToBankcardActivityBinding.topBar.setTitle("转账到银行卡").getPaint().setFakeBoldText(true);
        mTransferToBankcardActivityBinding.topBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //解决返回键不居中问题
        mTransferToBankcardActivityBinding.topBar.setGravity(Gravity.CENTER_VERTICAL);
    }

    private void initViewModel() {
        mTransferViewModel = ViewModelProviders.of(this).get(TransferViewModel.class);
        subscribeToModel(mTransferViewModel);
    }

    private void initData() {
        mTransferToBankcardActivityBinding.setHandlers(this);
        mTransferToBankcardActivityBinding.setPageData(mTransferViewModel.getPageData());
        int type = getIntent().getIntExtra(WithDrawalType, 0);
        mTransferViewModel.getPageData().setTransferType(TransferViewModel.TransferType.getTransferType(type));
        mTransferViewModel.getBalanceAndScale();
    }

    private void initViews() {
        mTransferToBankcardActivityBinding.mainLayout.setVisibility(View.GONE);
        mTransferToBankcardActivityBinding.etPayAmount.setButton(mTransferToBankcardActivityBinding.btTransfer);
        mTransferToBankcardActivityBinding.etPayAmount.setInputAccountListener(new PayAmountEdittext.InputAccountListener() {
            @Override
            public void inputAccount(float value) {
                mTransferViewModel.calculatePoundage(value);
            }
        });
        mTransferToBankcardActivityBinding.etBankNum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                String bankCardNum = mTransferViewModel.getPageData().getBankAccount().get();
                if (!b) {
                    if (!TextUtils.isEmpty(bankCardNum)) {
                        mTransferViewModel.getBankCardInfo();
                    }
                }
            }
        });
    }

    public void doTransfer(View view) {
        if (!GlobalUserDataStore.getInstance().isIdentityApprove()) {
            ARouter.getInstance().build(ARouterPath.ActivityIdentityApprove).navigation();
            return;
        }
        if (!GlobalUserDataStore.getInstance().isHaveSecurityPwd()) {
            ARouter.getInstance().build(ARouterPath.ActivitySettingSecurityPwd).navigation();
            return;
        }
        if (TextUtils.isEmpty(mTransferViewModel.getPageData().getBankName().get()) ||
                TextUtils.isEmpty(mTransferViewModel.getPageData().getBankAccount().get()) ||
                TextUtils.isEmpty(mTransferViewModel.getPageData().getOpeningBank().get()) ||
                TextUtils.isEmpty(mTransferViewModel.getPageData().getBankCity().get())) {
            ToastUtils.info("请完善账号信息");
            return;
        }
        try {
            String money = mTransferViewModel.getPageData().getPayAmount().get();
            String poundage = mTransferViewModel.getPageData().getPoundage().get();
            if (TextUtils.isEmpty(money)) {
                ToastUtils.info("请设置金额");
                return;
            }
            if (Float.valueOf(money) <= 0f) {
                ToastUtils.info("请输入正确金额");
                return;
            }
            String totalMoney = DecimalUtil.add(money, poundage);
            if (Float.valueOf(totalMoney) > Float.valueOf(mTransferViewModel.getPageData().getBalance().get())) {
                ToastUtils.info("转账金额不可大于可用余额");
                return;
            }
            showPayKeyBoard();
        } catch (Throwable e) {
            ToastUtils.info("请输入正确金额");
        }
    }

    //银行卡号输入框内容变化
    public void userCardNumEdittextChanged(CharSequence s, int start, int before, int count) {
        mTransferViewModel.getPageData().getOpeningBank().set("");
    }

    private void subscribeToModel(final TransferViewModel model) {
        //手续费比例
        model.getPoundageScaleLiveData().observe(this, new Observer<WithDrawalBalanceAndScaleData>() {
            @Override
            public void onChanged(@Nullable WithDrawalBalanceAndScaleData responseData) {
                if (responseData == null) {
                    mTransferToBankcardActivityBinding.emptyView.show(false, getResources().getString(R.string.emptyView_mode_desc_fail_desc), getResources().getString(R.string.emptyView_mode_desc_fail_desc), getResources().getString(R.string.emptyView_mode_desc_retry), retryAgainListener);
                    return;
                }
                if (responseData.isError()) {
                    mTransferToBankcardActivityBinding.emptyView.show(false, getResources().getString(R.string.emptyView_mode_desc_fail_title), null, getResources().getString(R.string.emptyView_mode_desc_retry), retryAgainListener);
                    resolveResponseErrorCode(responseData.getCode(), responseData.getMsg());
                    return;
                }

                mTransferToBankcardActivityBinding.emptyView.show(false);
                mTransferToBankcardActivityBinding.mainLayout.setVisibility(View.VISIBLE);
                mTransferViewModel.update(responseData);
            }
        });
        //转账
        model.getTransferLiveData().observe(this, new Observer<BaseResponseData>() {
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
                        .withString(TradingStateActivity.Intent_TradingMoney, mTransferViewModel.getPageData().getPayAmount().get())
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
                mTransferViewModel.transfer();
            }
        });
        //银行卡信息
        model.getCardInfoLiveData().observe(this, new Observer<BankCardInfoData>() {
            @Override
            public void onChanged(@Nullable BankCardInfoData responseData) {
                closeLogadingDialog();
                if (responseData == null) {
                    ToastUtils.showNetNoConnected();
                    return;
                }
                if (responseData.isError()) {
                    resolveResponseErrorCode(responseData.getCode(), responseData.getMsg());
                    return;
                }
                mTransferViewModel.getPageData().getOpeningBank().set(responseData.getObj().getOpeningBank());
            }
        });
    }

    private View.OnClickListener retryAgainListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mTransferToBankcardActivityBinding.mainLayout.setVisibility(View.GONE);
            mTransferToBankcardActivityBinding.emptyView.show(true);
            mTransferToBankcardActivityBinding.emptyView.setTitleText("正在加载");
            mTransferViewModel.getBalanceAndScale();
        }
    };

    //显示支付键盘
    public void showPayKeyBoard() {
        InputTools.HideKeyboard(mTransferToBankcardActivityBinding.layoutContent);
        if (null == popEnterPassword) {
            popEnterPassword = new PopEnterPassword(this, new OnPasswordInputFinish() {
                @Override
                public void inputFinish(String password) {
                    popEnterPassword.close();
                    showLogadingDialog();
                    mTransferViewModel.checkSecurityPwd(password);
                }
            });
        }
        String poundage = String.valueOf(Float.valueOf(mTransferViewModel.getPageData().getPoundage().get()));
        String payMoney = String.valueOf(Float.valueOf(mTransferViewModel.getPageData().getPayAmount().get()) + Float.valueOf(poundage));
        popEnterPassword.show(payMoney, poundage + "手续费", findViewById(R.id.layoutContent));
    }
}
