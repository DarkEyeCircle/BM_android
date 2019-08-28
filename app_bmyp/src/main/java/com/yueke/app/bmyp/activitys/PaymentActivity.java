package com.yueke.app.bmyp.activitys;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.apkfuns.logutils.LogUtils;
import com.askia.common.base.ARouterPath;
import com.askia.common.base.BaseActivity;
import com.askia.common.util.InputTools;
import com.askia.common.util.ToastUtils;
import com.askia.common.util.Utils;
import com.askia.coremodel.GlobalUserDataStore;
import com.askia.coremodel.datamodel.http.entities.BaseResponseData;
import com.askia.coremodel.datamodel.http.entities.PaymentData;
import com.askia.coremodel.datamodel.http.entities.PaymentScaleData;
import com.askia.coremodel.datamodel.http.entities.QRUrlData;
import com.askia.coremodel.datamodel.http.entities.ScanfPaymentData;
import com.askia.coremodel.datamodel.http.entities.WalletBalanceData;
import com.askia.coremodel.util.DecimalUtil;
import com.askia.coremodel.util.RxUtils;
import com.askia.coremodel.viewmodel.PaymentViewModel;
import com.askia.keyboard.OnPasswordInputFinish;
import com.askia.keyboard.widget.PopEnterPassword;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.yueke.app.bmyp.R;
import com.yueke.app.bmyp.databinding.PaymentActivityBinding;
import com.yueke.app.bmyp.widgets.PayAmountEdittext;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;

@Route(path = ARouterPath.ActivityPayment)
public class PaymentActivity extends BaseActivity {

    private PaymentActivityBinding mPaymentActivityBinding;
    private PaymentViewModel mPaymentViewModel;
    private final CompositeDisposable mDisposable = new CompositeDisposable();
    private PopEnterPassword popEnterPassword = null;
    private boolean isGenerateQRCode = false;

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
        mPaymentActivityBinding = DataBindingUtil.setContentView(this, R.layout.payment_activity);
    }

    private void initTopBar() {
        mPaymentActivityBinding.topBar.setTitle(R.string.payment).getPaint().setFakeBoldText(true);
        mPaymentActivityBinding.topBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //解决返回键不居中问题
        mPaymentActivityBinding.topBar.setGravity(Gravity.CENTER_VERTICAL);
    }

    private void initViewModel() {
        mPaymentViewModel = ViewModelProviders.of(this).get(PaymentViewModel.class);
        subscribeToModel(mPaymentViewModel);
    }

    private void initData() {
        mPaymentActivityBinding.setHandlers(this);
        mPaymentActivityBinding.setPageData(mPaymentViewModel.getPageData());
        mPaymentViewModel.getWalletBalance();
    }

    private void initViews() {
        mPaymentActivityBinding.mainLayout.setVisibility(View.GONE);
        mPaymentActivityBinding.btModify.setVisibility(View.GONE);
        mPaymentActivityBinding.etPayAmount.setButton(mPaymentActivityBinding.btGradient);
        mPaymentActivityBinding.rbtMemberBear.setChecked(true);
        mPaymentViewModel.getPageData().setMarginType(PaymentViewModel.MarginType.MembersBear);
        mPaymentActivityBinding.etPayAmount.setInputAccountListener(new PayAmountEdittext.InputAccountListener() {
            @Override
            public void inputAccount(float value) {
                mPaymentViewModel.calculatePoundage(value);
            }
        });
    }

    //会员承担保证金
    public void membersBear(View view) {
        if (!mPaymentActivityBinding.etPayAmount.isEnabled()) { //二维码已生成
            isGenerateQRCode = false;
            mPaymentViewModel.getPageData().setMarginType(PaymentViewModel.MarginType.MembersBear);
            mPaymentActivityBinding.availableBalanceLaytou.setVisibility(View.VISIBLE);
            mPaymentActivityBinding.btModify.setVisibility(View.GONE);
            mPaymentActivityBinding.etPayAmount.setText("");
            mPaymentActivityBinding.imgvQRCode.setImageBitmap(null);
            mPaymentActivityBinding.btGradient.setEnabled(false);
            mPaymentActivityBinding.etPayAmount.setEnabled(true);
            mPaymentActivityBinding.etPayAmount.setFocusable(true);
            mPaymentActivityBinding.etPayAmount.setFocusableInTouchMode(true);
            mPaymentActivityBinding.etPayAmount.requestFocus();
            return;
        }
        mPaymentViewModel.getPageData().setMarginType(PaymentViewModel.MarginType.MembersBear);
        mPaymentActivityBinding.availableBalanceLaytou.setVisibility(View.VISIBLE);
    }

    //商户承担保证金
    public void merchantsBear(View view) {
        if (!mPaymentActivityBinding.etPayAmount.isEnabled()) { //二维码已生成
            isGenerateQRCode = false;
            mPaymentViewModel.getPageData().setMarginType(PaymentViewModel.MarginType.MerchantsBear);
            mPaymentActivityBinding.availableBalanceLaytou.setVisibility(View.GONE);
            mPaymentActivityBinding.btModify.setVisibility(View.GONE);
            mPaymentActivityBinding.etPayAmount.setText("");
            mPaymentActivityBinding.imgvQRCode.setImageBitmap(null);
            mPaymentActivityBinding.btGradient.setEnabled(false);
            mPaymentActivityBinding.etPayAmount.setEnabled(true);
            mPaymentActivityBinding.etPayAmount.setFocusable(true);
            mPaymentActivityBinding.etPayAmount.setFocusableInTouchMode(true);
            mPaymentActivityBinding.etPayAmount.requestFocus();
            return;
        }
        mPaymentViewModel.getPageData().setMarginType(PaymentViewModel.MarginType.MerchantsBear);
        mPaymentActivityBinding.availableBalanceLaytou.setVisibility(View.GONE);
    }

    //修改金额
    public void modifyMoney(View view) {
        isGenerateQRCode = false;
        mPaymentActivityBinding.btModify.setVisibility(View.GONE);
        mPaymentActivityBinding.etPayAmount.setText("");
        mPaymentActivityBinding.imgvQRCode.setImageBitmap(null);
        mPaymentActivityBinding.btGradient.setEnabled(false);
        mPaymentActivityBinding.etPayAmount.setEnabled(true);
        mPaymentActivityBinding.etPayAmount.setFocusable(true);
        mPaymentActivityBinding.etPayAmount.setFocusableInTouchMode(true);
        mPaymentActivityBinding.etPayAmount.requestFocus();

    }

    //生成二维码
    public void generateOR(View view) {
        if (!GlobalUserDataStore.getInstance().isIdentityApprove()) {
            ARouter.getInstance().build(ARouterPath.ActivityIdentityApprove).navigation();
            return;
        }
        if (!GlobalUserDataStore.getInstance().isHaveSecurityPwd()) {
            ARouter.getInstance().build(ARouterPath.ActivitySettingSecurityPwd).navigation();
            return;
        }
        try {
            String money = mPaymentViewModel.getPageData().getPaymentMoney().get();
            String poundage = mPaymentViewModel.getPageData().getPoundage().get();
            if (TextUtils.isEmpty(money)) {
                ToastUtils.info("请设置金额");
                return;
            }
            if (Float.valueOf(money) <= 0f) {
                ToastUtils.info("请输入正确金额");
                return;
            }
            String totalMoney = DecimalUtil.add(money, poundage);
            if (Float.valueOf(totalMoney) > Float.valueOf(mPaymentViewModel.getPageData().getBalance().get())) {
                ToastUtils.info("付款金额不可大于可用余额");
                return;
            }
            showPayKeyBoard();
        } catch (Throwable e) {
            ToastUtils.info("请输入正确金额");
        }
    }

    //显示支付键盘
    public void showPayKeyBoard() {
        InputTools.HideKeyboard(mPaymentActivityBinding.layoutContent);
        if (null == popEnterPassword) {
            popEnterPassword = new PopEnterPassword(this, new OnPasswordInputFinish() {
                @Override
                public void inputFinish(String password) {
                    popEnterPassword.close();
                    showLogadingDialog();
                    mPaymentViewModel.checkSecurityPwd(password);
                }
            });
        }
        if (mPaymentViewModel.getPageData().getMarginType() == PaymentViewModel.MarginType.MembersBear) {
            String poundage = String.valueOf(Float.valueOf(mPaymentViewModel.getPageData().getPoundage().get()));
            String payMoney = String.valueOf(Float.valueOf(mPaymentViewModel.getPageData().getPaymentMoney().get()) + Float.valueOf(poundage));
            popEnterPassword.show(payMoney, poundage + "质保金", findViewById(R.id.layoutContent));
        } else {
            String payMoney = mPaymentViewModel.getPageData().getPaymentMoney().get();
            popEnterPassword.show(payMoney, null, findViewById(R.id.layoutContent));
        }

    }

    private void subscribeToModel(final PaymentViewModel model) {
        //获取余额
        model.getWalletBalanceLiveData().observe(this, new Observer<WalletBalanceData>() {
            @Override
            public void onChanged(@Nullable WalletBalanceData responseData) {
                if (responseData == null) {
                    mPaymentActivityBinding.emptyView.show(false, getResources().getString(R.string.emptyView_mode_desc_fail_desc), getResources().getString(R.string.emptyView_mode_desc_fail_desc), getResources().getString(R.string.emptyView_mode_desc_retry), retryAgainListener);
                    return;
                }
                if (responseData.isError() || null == responseData.getObj()) {
                    mPaymentActivityBinding.emptyView.show(false, getResources().getString(R.string.emptyView_mode_desc_fail_title), null, getResources().getString(R.string.emptyView_mode_desc_retry), retryAgainListener);
                    resolveResponseErrorCode(responseData.getCode(), responseData.getMsg());
                    return;
                }
                mPaymentViewModel.getPageData().getBalance().set(responseData.getObj().getBalance());
                mPaymentViewModel.getScale();
            }
        });
        //获取比例
        model.getScaleLiveData().observe(this, new Observer<PaymentScaleData>() {
            @Override
            public void onChanged(@Nullable PaymentScaleData responseData) {
                if (responseData == null) {
                    mPaymentActivityBinding.emptyView.show(false, getResources().getString(R.string.emptyView_mode_desc_fail_desc), getResources().getString(R.string.emptyView_mode_desc_fail_desc), getResources().getString(R.string.emptyView_mode_desc_retry), retryAgainListener);
                    return;
                }
                if (responseData.isError()) {
                    mPaymentActivityBinding.emptyView.show(false, getResources().getString(R.string.emptyView_mode_desc_fail_title), null, getResources().getString(R.string.emptyView_mode_desc_retry), retryAgainListener);
                    resolveResponseErrorCode(responseData.getCode(), responseData.getMsg());
                    return;
                }

                mPaymentActivityBinding.emptyView.show(false);
                mPaymentActivityBinding.mainLayout.setVisibility(View.VISIBLE);
                mPaymentViewModel.getPageData().setScale(responseData.getObj());
            }
        });
        //获取二维码
        model.getORLiveData().observe(this, new Observer<QRUrlData>() {
            @Override
            public void onChanged(@Nullable QRUrlData responseData) {
                closeLogadingDialog();
                if (responseData == null) {
                    ToastUtils.showNetNoConnected();
                    return;
                }
                if (responseData.isError()) {
                    resolveResponseErrorCode(responseData.getCode(), responseData.getMsg());
                    return;
                }
                mPaymentViewModel.getPageData().setCodeId(responseData.parsing().getCodeId());
                Utils.getLogoForQR(GlobalUserDataStore.getInstance().getAvatar(), new BaseBitmapDataSubscriber() {
                    @Override
                    protected void onNewResultImpl(Bitmap bitmap) {

                        Utils.generateQRForPayment(responseData.getObj(), bitmap, new io.reactivex.Observer<Bitmap>() {

                            private Disposable disposable;

                            @Override
                            public void onSubscribe(Disposable d) {
                                disposable = d;
                            }

                            @Override
                            public void onNext(Bitmap bitmap) {
                                mPaymentActivityBinding.imgvQRCode.setImageBitmap(bitmap);
                                mPaymentActivityBinding.etPayAmount.setEnabled(false);
                                mPaymentActivityBinding.btModify.setVisibility(View.VISIBLE);
                                mPaymentActivityBinding.btGradient.setEnabled(false);
                                isGenerateQRCode = true;
                                //开始轮训
                                mDisposable.add(RxUtils.CountDown(1, null, new Action() {
                                    @Override
                                    public void run() throws Exception {
                                        mPaymentViewModel.getPayState();
                                    }
                                }));
                            }

                            @Override
                            public void onError(Throwable e) {
                                disposable.dispose();
                                ToastUtils.info("生成二维码失败，请重试");
                            }

                            @Override
                            public void onComplete() {
                                disposable.dispose();
                            }
                        });
                    }

                    @Override
                    protected void onFailureImpl(DataSource<CloseableReference<CloseableImage>> dataSource) {
                        ToastUtils.info("生成二维码失败，请重试");
                    }
                });

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
                mPaymentViewModel.getQRcodeUrl();
            }
        });
        //检查付款状态
        model.getPayStateLiveData().observe(this, new Observer<PaymentData>() {
            @Override
            public void onChanged(@Nullable PaymentData responseData) {
                if (responseData == null || responseData.isError()) {
                    if (!isGenerateQRCode) {
                        return;
                    }
                    mDisposable.add(RxUtils.CountDown(1, null, new Action() {
                        @Override
                        public void run() throws Exception {
                            mPaymentViewModel.getPayState();
                        }
                    }));
                    return;
                }
                ARouter.getInstance().build(ARouterPath.ActivityTradingState).
                        withString(TradingStateActivity.Intent_TradingType, TradingStateActivity.Intent_TradingType_Payment)
                        .withBoolean(TradingStateActivity.Intent_TradingState, true)
                        .withString(TradingStateActivity.Intent_TradingMoney, responseData.getObj())
                        .navigation();
                finish();
            }
        });


    }

    private View.OnClickListener retryAgainListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mPaymentActivityBinding.mainLayout.setVisibility(View.GONE);
            mPaymentActivityBinding.emptyView.show(true);
            mPaymentActivityBinding.emptyView.setTitleText("正在加载");
            mPaymentViewModel.getWalletBalance();
        }
    };
}
