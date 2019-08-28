package com.yueke.app.bmyp.activitys;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
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
import com.askia.coremodel.datamodel.http.entities.PaymentData;
import com.askia.coremodel.datamodel.http.entities.ScanfPaymentData;
import com.askia.coremodel.datamodel.http.entities.ScanfQRCodeResult;
import com.askia.coremodel.datamodel.http.entities.UserInfoByQRCode;
import com.askia.coremodel.viewmodel.ScanfPaymentViewModel;
import com.askia.keyboard.OnPasswordInputFinish;
import com.askia.keyboard.widget.PopEnterPassword;
import com.yueke.app.bmyp.R;
import com.yueke.app.bmyp.databinding.ScanfPaymentActivityBinding;

@Route(path = ARouterPath.ActivityScanfPayment)
public class ScanfPaymentActivity extends BaseActivity {

    public static final String INTENT_DATA_RESULT = "INTENT_DATA_RESULT";

    private ScanfPaymentActivityBinding mScanfPaymentActivityBinding;
    private ScanfPaymentViewModel mScanfPaymentViewModel;
    private PopEnterPassword popEnterPassword = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDataBinding();
        initDataBinding();
        initTopBar();
        initViewModel();
        initData();
        initViews();
    }

    private void initDataBinding() {
        mScanfPaymentActivityBinding = DataBindingUtil.setContentView(this, R.layout.scanf_payment_activity);
    }

    private void initTopBar() {
        mScanfPaymentActivityBinding.topBar.setTitle(R.string.payment).getPaint().setFakeBoldText(true);
        mScanfPaymentActivityBinding.topBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //解决返回键不居中问题
        mScanfPaymentActivityBinding.topBar.setGravity(Gravity.CENTER_VERTICAL);
    }

    private void initViewModel() {
        mScanfPaymentViewModel = ViewModelProviders.of(this).get(ScanfPaymentViewModel.class);
        subscribeToModel(mScanfPaymentViewModel);
    }

    private void initData() {
        mScanfPaymentActivityBinding.setHandlers(this);
        mScanfPaymentActivityBinding.setPageData(mScanfPaymentViewModel.getPageData());
        ScanfQRCodeResult qrCodeResult = getIntent().getParcelableExtra(INTENT_DATA_RESULT);
        mScanfPaymentViewModel.getPageData().setCodeId(qrCodeResult.getCodeId());
        mScanfPaymentViewModel.getUserInfoByCode();
    }

    private void initViews() {
        mScanfPaymentActivityBinding.mainLayout.setVisibility(View.GONE);
        mScanfPaymentActivityBinding.etPayAmount.setButton(mScanfPaymentActivityBinding.btPay);
    }

    //立即支付
    public void doPaymentNow(View view) {
        if (!GlobalUserDataStore.getInstance().isIdentityApprove()) {
            ARouter.getInstance().build(ARouterPath.ActivityIdentityApprove).navigation();
            return;
        }
        if (!GlobalUserDataStore.getInstance().isHaveSecurityPwd()) {
            ARouter.getInstance().build(ARouterPath.ActivitySettingSecurityPwd).navigation();
            return;
        }
        if (Float.valueOf(mScanfPaymentViewModel.getPageData().getMoney().get()) <= 0f) {
            ToastUtils.info("请输入正确金额");
            return;
        }
        showPayKeyBoard();
    }


    //显示支付键盘
    public void showPayKeyBoard() {
        InputTools.HideKeyboard(mScanfPaymentActivityBinding.layoutContent);
        if (null == popEnterPassword) {
            popEnterPassword = new PopEnterPassword(this, new OnPasswordInputFinish() {
                @Override
                public void inputFinish(String password) {
                    popEnterPassword.close();
                    showLogadingDialog();
                    mScanfPaymentViewModel.checkSecurityPwd(password);
                }
            });
        }
        String payMoney = mScanfPaymentViewModel.getPageData().getMoney().get();
        popEnterPassword.show(payMoney, null, findViewById(R.id.layoutContent));
    }

    private void subscribeToModel(final ScanfPaymentViewModel model) {
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
                mScanfPaymentViewModel.doPay();
            }
        });
        //获取二维码信息
        model.getUserInfoLiveData().observe(this, new Observer<UserInfoByQRCode>() {
            @Override
            public void onChanged(@Nullable UserInfoByQRCode responseData) {
                if (responseData == null) {
                    mScanfPaymentActivityBinding.emptyView.show(false, getResources().getString(R.string.emptyView_mode_desc_fail_desc), getResources().getString(R.string.emptyView_mode_desc_fail_desc), getResources().getString(R.string.emptyView_mode_desc_retry), retryAgainListener);
                    return;
                }
                if (responseData.isError() || null == responseData.getObj()) {
                    mScanfPaymentActivityBinding.emptyView.show(false, responseData.getMsg(), null, getResources().getString(R.string.emptyView_mode_desc_retry), retryAgainListener);
                    resolveResponseErrorCode(responseData.getCode(), responseData.getMsg());
                    return;
                }
                mScanfPaymentViewModel.getPageData().setCodeId(responseData.getObj().getCodeId());
                mScanfPaymentViewModel.getPageData().setImgHead(responseData.getObj().getAvatar());
                mScanfPaymentViewModel.getPageData().getMoney().set(responseData.getObj().getMoney());
                mScanfPaymentViewModel.getPageData().getNickName().set(responseData.getObj().getNickName());
                mScanfPaymentViewModel.getPageData().setReceiveUserId(responseData.getObj().getReceiveUserId());
                mScanfPaymentActivityBinding.emptyView.setLoadingShowing(false);
                mScanfPaymentActivityBinding.mainLayout.setVisibility(View.VISIBLE);
                mScanfPaymentActivityBinding.imgvHead.setImageURI(responseData.getObj().getAvatar());
                if (!TextUtils.isEmpty(responseData.getObj().getMoney())) {
                    mScanfPaymentActivityBinding.etPayAmount.setEnabled(false);
                    mScanfPaymentActivityBinding.etPayAmount.setTextColor(Color.parseColor("#999999"));
                }
            }
        });
        //付款
        model.getPayLiveData().observe(this, new Observer<ScanfPaymentData>() {
            @Override
            public void onChanged(@Nullable ScanfPaymentData responseData) {
                closeLogadingDialog();
                if (responseData == null) {
                    ToastUtils.showNetNoConnected();
                    return;
                }
                if (responseData.isError()) {
                    ARouter.getInstance().build(ARouterPath.ActivityTradingState).
                            withString(TradingStateActivity.Intent_TradingType, TradingStateActivity.Intent_TradingType_Payment)
                            .withBoolean(TradingStateActivity.Intent_TradingState, false)
                            .withString(TradingStateActivity.Intent_Trading_Error_Msg, responseData.getMsg())
                            .navigation();
                    finish();
                    return;
                }
                ARouter.getInstance().build(ARouterPath.ActivityTradingState).
                        withString(TradingStateActivity.Intent_TradingType, TradingStateActivity.Intent_TradingType_Payment)
                        .withBoolean(TradingStateActivity.Intent_TradingState, true)
                        .withString(TradingStateActivity.Intent_TradingMoney, mScanfPaymentViewModel.getPageData().getMoney().get())
                        .navigation();
                finish();
            }
        });
    }

    private View.OnClickListener retryAgainListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mScanfPaymentActivityBinding.mainLayout.setVisibility(View.GONE);
            mScanfPaymentActivityBinding.emptyView.show(true);
            mScanfPaymentActivityBinding.emptyView.setTitleText("正在加载");
            mScanfPaymentViewModel.getUserInfoByCode();
        }
    };
}
