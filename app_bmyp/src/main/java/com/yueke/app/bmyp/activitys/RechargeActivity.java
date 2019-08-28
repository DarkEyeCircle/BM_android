package com.yueke.app.bmyp.activitys;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.askia.common.base.ARouterPath;
import com.askia.common.base.BaseActivity;
import com.askia.common.util.ToastUtils;
import com.askia.common.util.Utils;
import com.askia.coremodel.GlobalUserDataStore;
import com.askia.coremodel.datamodel.http.entities.BaseResponseData;
import com.askia.coremodel.datamodel.http.entities.RechargeOrderData;
import com.askia.coremodel.viewmodel.RechargeViewModel;
import com.askia.keyboard.OnPasswordInputFinish;
import com.askia.keyboard.widget.PopEnterPassword;
import com.yueke.app.bmyp.R;
import com.yueke.app.bmyp.databinding.RechargeActivityBinding;

@Route(path = ARouterPath.ActivityRecharge)
public class RechargeActivity extends BaseActivity {

    private RechargeActivityBinding mRechargeActivityBinding;
    private RechargeViewModel mRechargeViewModel;

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
        mRechargeActivityBinding.etPayAmount.setButton(mRechargeActivityBinding.btPay);
    }

    private void initDataBinding() {
        mRechargeActivityBinding = DataBindingUtil.setContentView(this, R.layout.recharge_activity);
    }

    //初始化TopBar
    private void initTopBar() {
        //标题栏返回键监听
        mRechargeActivityBinding.topBar.setTitle(R.string.recharge).getPaint().setFakeBoldText(true);
        mRechargeActivityBinding.topBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //解决返回键不居中问题
        mRechargeActivityBinding.topBar.setGravity(Gravity.CENTER_VERTICAL);
    }

    //初始化ViewModle
    private void initViewModel() {
        mRechargeViewModel = ViewModelProviders.of(RechargeActivity.this).get(RechargeViewModel.class);
        subscribeToModel(mRechargeViewModel);
    }

    //初始化页面数据
    private void initData() {
        mRechargeActivityBinding.setHandlers(this);
        mRechargeActivityBinding.setPageData(mRechargeViewModel.getPageData());
    }

    //支付宝账户支付
    public void alipayRecharge(View view) {
        mRechargeActivityBinding.btAlipayRecharge.setChecked(true);
        mRechargeViewModel.getPageData().setRechargeType(RechargeViewModel.RechargeType.AliPay);
    }

    //微信账户支付
    public void wechatRecharge(View view) {
        mRechargeActivityBinding.btWechatRecharge.setChecked(true);
        mRechargeViewModel.getPageData().setRechargeType(RechargeViewModel.RechargeType.Wechat);
    }

    //银行卡账户支付
    public void bankCardRecharge(View view) {
        mRechargeActivityBinding.btBankCardRecharge.setChecked(true);
        mRechargeViewModel.getPageData().setRechargeType(RechargeViewModel.RechargeType.Unionpay);
    }

    //支付
    public void doPayment(View view) {
        if (!GlobalUserDataStore.getInstance().isIdentityApprove()) {
            ARouter.getInstance().build(ARouterPath.ActivityIdentityApprove).navigation();
            return;
        }
        if (!GlobalUserDataStore.getInstance().isHaveSecurityPwd()) {
            ARouter.getInstance().build(ARouterPath.ActivitySettingSecurityPwd).navigation();
            return;
        }
        if (Float.valueOf(mRechargeViewModel.getPageData().getPayAmount().get()) <= 0f) {
            ToastUtils.info("请输入正确金额");
            return;
        }
        if (mRechargeViewModel.getPageData().getRechargeType() == null) {
            ToastUtils.info("请选择支付方式");
            return;
        }
        showLogadingDialog();
        mRechargeViewModel.creatRechargeOrder();
    }


    private void subscribeToModel(final RechargeViewModel model) {
        //订单
        model.getRechangeOrderLiveData().observe(this, new Observer<RechargeOrderData>() {
            @Override
            public void onChanged(@Nullable RechargeOrderData responseData) {
                closeLogadingDialog();
                if (responseData == null) {
                    ToastUtils.showNetNoConnected();
                    return;
                }
                if (responseData.isError() || responseData.getObj() == null) {
                    ToastUtils.info("生成订单失败，请重试");
                    return;
                }
                mRechargeViewModel.getPageData().setOrderUrl(responseData.getObj().getRqcode_url());
                jumpToPay();
            }
        });

    }

    private void jumpToPay() {
        if (!Utils.checkAliPayInstalled()) {
            ToastUtils.info("请确定是否已安装支付宝");
            return;
        }
        try {
            String newUrl = "";
            switch (mRechargeViewModel.getPageData().getRechargeType()) {
                case Wechat:
                    newUrl = "alipays://platformapi/startapp?appId=20000067&url=" + mRechargeViewModel.getPageData().getOrderUrl();
                    break;
                case AliPay:
                    newUrl = "alipays://platformapi/startapp?appId=20000067&url=" + mRechargeViewModel.getPageData().getOrderUrl();
                    break;
                case Unionpay:
                    newUrl = "alipays://platformapi/startapp?appId=20000067&url=" + mRechargeViewModel.getPageData().getOrderUrl();
                    break;
            }
            Uri uri = Uri.parse(newUrl);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivityForResult(intent, 0x12);
        } catch (Exception e) {
            ToastUtils.info("打开失败，请确定是否已安装支付宝");
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ARouter.getInstance().build(ARouterPath.ActivityTradingState).
                withString(TradingStateActivity.Intent_TradingType, TradingStateActivity.Intent_TradingType_Operation)
                .withBoolean(TradingStateActivity.Intent_TradingState, true)
                .withString(TradingStateActivity.Intent_TradingMoney, "")
                .navigation();
        finish();
    }
}
