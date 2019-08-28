package com.yueke.app.bmyp.activitys;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.askia.coremodel.datamodel.http.entities.BuyCardData;
import com.askia.coremodel.viewmodel.BuyCardViewModel;
import com.askia.coremodel.viewmodel.PaymentViewModel;
import com.askia.keyboard.OnPasswordInputFinish;
import com.askia.keyboard.widget.PopEnterPassword;
import com.yueke.app.bmyp.R;
import com.yueke.app.bmyp.databinding.BuyCardActivityBinding;

@Route(path = ARouterPath.ActivityBuyCard)
public class BuyCardActivity extends BaseActivity {

    public static final String INTENT_DATA_VALUE = "INTENT_DATA_VALUE";
    public static final String INTENT_DATA_URL = "INTENT_DATA_URL";
    public static final String INTENT_DATA_ID = "INTENT_DATA_ID";
    public static final String INTENT_DATA_INTEGRAL_MULRIPLE = "INTENT_DATA_INTEGRAL_MULRIPLE";

    private BuyCardActivityBinding mBuyCardActivityBinding;
    private BuyCardViewModel mBuyCardViewModel;
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
        mBuyCardActivityBinding.imgvCardCover.setImageURI(mBuyCardViewModel.getPageData().getCardCoverUrl().get());
    }

    private void initDataBinding() {
        mBuyCardActivityBinding = DataBindingUtil.setContentView(this, R.layout.buy_card_activity);
    }

    //初始化TopBar
    private void initTopBar() {
        //标题栏返回键监听
        mBuyCardActivityBinding.topBar.setTitle(R.string.buy_card).getPaint().setFakeBoldText(true);
        mBuyCardActivityBinding.topBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //解决返回键不居中问题
        mBuyCardActivityBinding.topBar.setGravity(Gravity.CENTER_VERTICAL);
    }

    //初始化ViewModle
    private void initViewModel() {
        mBuyCardViewModel = ViewModelProviders.of(BuyCardActivity.this).get(BuyCardViewModel.class);
        subscribeToModel(mBuyCardViewModel);
    }

    //初始化页面数据
    private void initData() {
        mBuyCardActivityBinding.setHandlers(this);
        mBuyCardActivityBinding.setPageData(mBuyCardViewModel.getPageData());
        String value = getIntent().getStringExtra(INTENT_DATA_VALUE);
        String url = getIntent().getStringExtra(INTENT_DATA_URL);
        String ID = getIntent().getStringExtra(INTENT_DATA_ID);
        String mulriple = getIntent().getStringExtra(INTENT_DATA_INTEGRAL_MULRIPLE);
        mBuyCardViewModel.setData(ID, value, url, mulriple);
    }

    //立即购买
    public void doBuyNow(View view) {
        if (!GlobalUserDataStore.getInstance().isIdentityApprove()) {
            ARouter.getInstance().build(ARouterPath.ActivityIdentityApprove).navigation();
            return;
        }
        if (!GlobalUserDataStore.getInstance().isHaveSecurityPwd()) {
            ARouter.getInstance().build(ARouterPath.ActivitySettingSecurityPwd).navigation();
            return;
        }
        showPayKeyBoard();
    }

    private void subscribeToModel(final BuyCardViewModel model) {

        model.getBuyCardLiveData().observe(this, new Observer<BuyCardData>() {
            @Override
            public void onChanged(@Nullable BuyCardData responseData) {
                closeLogadingDialog();
                if (responseData == null) {
                    ToastUtils.showNetNoConnected();
                    return;
                }
                if (responseData.isError()) {
                    resolveResponseErrorCode(responseData.getCode(), responseData.getMsg());
                    return;
                }
                ARouter.getInstance().build(ARouterPath.ActivityPurchaseStates)
                        .withBoolean(PurchaseStatesActivity.IntentPurchaseState, true)
                        .withString(PurchaseStatesActivity.INTENT_DATA_ID, responseData.getObj().getId())
                        .withString(PurchaseStatesActivity.INTENT_DATA_MONEY, mBuyCardViewModel.getPageData().getFaceValue().get())
                        .withString(PurchaseStatesActivity.INTENT_DATA_INTEGRAL_MULRIPLE, mBuyCardViewModel.getPageData().getIntegralMulriple())
                        .navigation();
                finish();
            }
        });
    }

    //显示支付键盘
    public void showPayKeyBoard() {
        InputTools.HideKeyboard(mBuyCardActivityBinding.layoutContent);
        if (null == popEnterPassword) {
            popEnterPassword = new PopEnterPassword(this, new OnPasswordInputFinish() {
                @Override
                public void inputFinish(String password) {
                    mBuyCardViewModel.getPageData().setSecurityPassword(password);
                    popEnterPassword.close();
                    showLogadingDialog();
                    mBuyCardViewModel.buyCard();
                }
            });
        }
        String payMoney = mBuyCardViewModel.getPageData().getFaceValue().get();
        popEnterPassword.show(payMoney, null, findViewById(R.id.layoutContent));
    }

}
