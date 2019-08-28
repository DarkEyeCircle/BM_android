package com.yueke.app.bmyp.activitys;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.askia.common.base.ARouterPath;
import com.askia.common.base.BaseActivity;
import com.yueke.app.bmyp.R;
import com.yueke.app.bmyp.databinding.TradingStateActivityBinding;

@Route(path = ARouterPath.ActivityTradingState)
public class TradingStateActivity extends BaseActivity {

    public static final String Intent_TradingType = "Intent_TradingType"; //交易类型
    public static final String Intent_TradingType_Payment = "Intent_TradingType_Payment"; //付款
    public static final String Intent_TradingType_Proceeds = "Intent_TradingType_Proceeds"; //收款
    public static final String Intent_TradingType_WithDrawal = "Intent_TradingType_WithDrawal"; //提现
    public static final String Intent_TradingType_Transfer = "Intent_TradingType_Transfer"; //转账
    public static final String Intent_TradingType_Operation = "Intent_TradingType_Operation"; //操作

    public static final String Intent_TradingState = "Intent_TradingState"; //交易状态

    public static final String Intent_TradingMoney = "Intent_TradingMoney"; //交易金额

    public static final String Intent_Trading_Error_Msg = "Intent_Trading_Error_Msg"; //交易失败原因

    private TradingStateActivityBinding mTradingStateActivityBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTradingStateActivityBinding = DataBindingUtil.setContentView(this, R.layout.trading_state_activity);
        String title = "";
        String money = "";
        String errorMsg = "";
        String tradingType = getIntent().getStringExtra(Intent_TradingType);
        if (TextUtils.equals(Intent_TradingType_Payment, tradingType)) {
            title = "付款";
        } else if (TextUtils.equals(Intent_TradingType_Proceeds, tradingType)) {
            title = "收款";
        } else if (TextUtils.equals(Intent_TradingType_WithDrawal, tradingType)) {
            title = "提现";
        } else if (TextUtils.equals(Intent_TradingType_Transfer, tradingType)) {
            title = "转账";
        } else if (TextUtils.equals(Intent_TradingType_Operation, tradingType)) {
            title = "操作";
        }

        boolean tradingState = getIntent().getBooleanExtra(Intent_TradingState, false);
        if (tradingState) {
            title = title + "成功";
            money = getIntent().getStringExtra(Intent_TradingMoney);
            mTradingStateActivityBinding.imgvState.setBackgroundResource(R.drawable.icon_success);
            if (TextUtils.isEmpty(money)) {
                mTradingStateActivityBinding.moneyLayout.setVisibility(View.GONE);
            } else {
                mTradingStateActivityBinding.tvMoney.setText(money);
                mTradingStateActivityBinding.moneyLayout.setVisibility(View.VISIBLE);
            }
            mTradingStateActivityBinding.tvErrorMsg.setVisibility(View.GONE);
        } else {
            title = title + "失败";
            errorMsg = getIntent().getStringExtra(Intent_Trading_Error_Msg).replaceAll("！", "");
            mTradingStateActivityBinding.imgvState.setBackgroundResource(R.drawable.icon_failer);
            mTradingStateActivityBinding.moneyLayout.setVisibility(View.GONE);
            mTradingStateActivityBinding.tvErrorMsg.setVisibility(View.VISIBLE);
            mTradingStateActivityBinding.tvErrorMsg.setText(errorMsg);
        }
        mTradingStateActivityBinding.tvState.setText(title);
    }

    public void goBack(View view) {
        finish();
    }

}
