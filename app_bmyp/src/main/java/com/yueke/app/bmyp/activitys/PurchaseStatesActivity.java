package com.yueke.app.bmyp.activitys;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.askia.common.base.ARouterPath;
import com.askia.common.base.BaseActivity;
import com.askia.common.util.ToastUtils;
import com.askia.coremodel.datamodel.http.entities.BaseResponseData;
import com.askia.coremodel.viewmodel.PurchaseStatesViewModel;
import com.clock.scratch.ScratchView;
import com.yueke.app.bmyp.R;
import com.yueke.app.bmyp.databinding.PurchaseStatesActivityBinding;

@Route(path = ARouterPath.ActivityPurchaseStates)
public class PurchaseStatesActivity extends BaseActivity {

    public static final String IntentPurchaseState = "IntentPurchaseState";
    public static final String INTENT_DATA_ID = "INTENT_DATA_ID";
    public static final String INTENT_DATA_MONEY = "INTENT_DATA_MONEY";
    public static final String INTENT_DATA_INTEGRAL_MULRIPLE = "INTENT_DATA_INTEGRAL_MULRIPLE";
    private PurchaseStatesActivityBinding mPurchaseStatesActivityBinding;
    private PurchaseStatesViewModel mPurchaseStatesViewModel;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDataBinding();
        initViewModel();
        initTopBar();
        initData();
        initViews();
    }

    private void initDataBinding() {
        mPurchaseStatesActivityBinding = DataBindingUtil.setContentView(this, R.layout.purchase_states_activity);
    }

    private void initTopBar() {
        boolean purchaseSuccess = getIntent().getBooleanExtra(IntentPurchaseState, true);
        mPurchaseStatesViewModel.getPageData().setPurchaseState(purchaseSuccess);
        if (purchaseSuccess) {
            mPurchaseStatesActivityBinding.topBar.setTitle(R.string.buy_success).getPaint().setFakeBoldText(true);

        } else {
            mPurchaseStatesActivityBinding.topBar.setTitle(R.string.buy_failure).getPaint().setFakeBoldText(true);
        }
        //解决返回键不居中问题
        mPurchaseStatesActivityBinding.topBar.setGravity(Gravity.CENTER_VERTICAL);
    }

    private void initViewModel() {
        mPurchaseStatesViewModel = ViewModelProviders.of(this).get(PurchaseStatesViewModel.class);
        subscribeToModel(mPurchaseStatesViewModel);
    }

    private void initData() {
        mPurchaseStatesActivityBinding.setHandlers(this);
        mPurchaseStatesActivityBinding.setPageData(mPurchaseStatesViewModel.getPageData());
        mPurchaseStatesViewModel.getPageData().setCardId(getIntent().getStringExtra(INTENT_DATA_ID));
        mPurchaseStatesViewModel.getPageData().getFaceValue().set(getIntent().getStringExtra(INTENT_DATA_MONEY));
        mPurchaseStatesViewModel.getPageData().getIntegralMulriple().set(getIntent().getStringExtra(INTENT_DATA_INTEGRAL_MULRIPLE));
    }

    private void initViews() {
        mPurchaseStatesActivityBinding.scratchView.setMaxPercent(35);
        mPurchaseStatesActivityBinding.scratchView.setWatermark(R.drawable.scratch_top_bg);
        mPurchaseStatesActivityBinding.scratchView.setEraseStatusListener(new ScratchView.EraseStatusListener() {
            @Override
            public void onProgress(int percent) {
                if (percent <= 3) {
                    mPurchaseStatesActivityBinding.scratchHint.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCompleted(View view) {
                mPurchaseStatesActivityBinding.scratchView.clear();
                showLogadingDialog();
                mPurchaseStatesViewModel.exchangeIntegral();
            }
        });
    }

    public void goBack(View view) {
        finish();
    }


    private void subscribeToModel(final PurchaseStatesViewModel model) {

        model.getExchangeLiveData().observe(this, new Observer<BaseResponseData>() {
            @Override
            public void onChanged(@Nullable BaseResponseData responseData) {
                closeLogadingDialog();
                mPurchaseStatesActivityBinding.btSave.setEnabled(true);
                if (responseData == null) {
                    ToastUtils.showNetNoConnected();
                    return;
                }
                if (responseData.isError()) {
                    resolveResponseErrorCode(responseData.getCode(), responseData.getMsg());
                    return;
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return true;
    }
}
