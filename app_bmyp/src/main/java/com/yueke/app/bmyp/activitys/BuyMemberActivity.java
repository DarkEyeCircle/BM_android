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
import com.askia.coremodel.datamodel.http.entities.BaseResponseData;
import com.askia.coremodel.datamodel.http.entities.BuyMemberData;
import com.askia.coremodel.viewmodel.BuyMemberViewModel;
import com.askia.keyboard.OnPasswordInputFinish;
import com.askia.keyboard.widget.PopEnterPassword;
import com.yueke.app.bmyp.R;
import com.yueke.app.bmyp.databinding.BuyMemberActivityBinding;

@Route(path = ARouterPath.ActivityBuyMember)
public class BuyMemberActivity extends BaseActivity {

    public static final String INTENT_DATA_VALUE = "INTENT_DATA_VALUE";
    public static final String INTENT_DATA_URL = "INTENT_DATA_URL";
    public static final String INTENT_DATA_ID = "INTENT_DATA_ID";

    private BuyMemberActivityBinding mBuyMemberActivityBinding;
    private BuyMemberViewModel mBuyMemberViewModel;

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
        mBuyMemberActivityBinding.imgvMemberCover.setImageURI(mBuyMemberViewModel.getPageData().getCardCoverUrl().get());
    }

    private void initDataBinding() {
        mBuyMemberActivityBinding = DataBindingUtil.setContentView(this, R.layout.buy_member_activity);
    }

    //初始化TopBar
    private void initTopBar() {
        //标题栏返回键监听
        mBuyMemberActivityBinding.topBar.setTitle("升级会员").getPaint().setFakeBoldText(true);
        mBuyMemberActivityBinding.topBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //解决返回键不居中问题
        mBuyMemberActivityBinding.topBar.setGravity(Gravity.CENTER_VERTICAL);
    }

    //初始化ViewModle
    private void initViewModel() {
        mBuyMemberViewModel = ViewModelProviders.of(BuyMemberActivity.this).get(BuyMemberViewModel.class);
        subscribeToModel(mBuyMemberViewModel);
    }

    //初始化页面数据
    private void initData() {
        mBuyMemberActivityBinding.setHandlers(this);
        mBuyMemberActivityBinding.setPageData(mBuyMemberViewModel.getPageData());
        String value = getIntent().getStringExtra(INTENT_DATA_VALUE);
        String url = getIntent().getStringExtra(INTENT_DATA_URL);
        String ID = getIntent().getStringExtra(INTENT_DATA_ID);
        mBuyMemberViewModel.setData(ID, value, url);
    }

    //立即购买
    public void doBuyNow(View view) {
        showLogadingDialog();
        mBuyMemberViewModel.buyMember();
    }

    private void subscribeToModel(final BuyMemberViewModel model) {

        model.getBuyMemberLiveData().observe(this, new Observer<BuyMemberData>() {
            @Override
            public void onChanged(@Nullable BuyMemberData responseData) {
                closeLogadingDialog();
                if (responseData == null) {
                    ToastUtils.showNetNoConnected();
                    return;
                }
                if (responseData.isError() || responseData.getObj() == null) {
                    ToastUtils.info("生成订单失败，请重试");
                    return;
                }
                mBuyMemberViewModel.getPageData().setOrderUrl(responseData.getObj().getRqcode_url());
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
            String newUrl = "alipays://platformapi/startapp?appId=20000067&url=" + mBuyMemberViewModel.getPageData().getOrderUrl();
            Uri uri = Uri.parse(newUrl);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivityForResult(intent, 0x12);
        } catch (Exception e) {
            ToastUtils.info("请确定是否已安装支付宝");
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
