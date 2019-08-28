package com.yueke.app.bmyp.activitys;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.askia.common.base.ARouterPath;
import com.askia.common.base.BaseActivity;
import com.askia.coremodel.datamodel.http.entities.ScanfProceedsData;
import com.askia.coremodel.datamodel.http.entities.ScanfQRCodeResult;
import com.askia.coremodel.viewmodel.ProceedsStatesViewModel;
import com.yueke.app.bmyp.R;
import com.yueke.app.bmyp.databinding.ScanfProceedsActivityBinding;

@Route(path = ARouterPath.ActivityScanfProceeds)
public class ScanfProceedsActivity extends BaseActivity {

    public static final String INTENT_DATA_RESULT = "INTENT_DATA_RESULT";
    private ScanfProceedsActivityBinding mScanfProceedsActivityBinding;
    private ProceedsStatesViewModel mProceedsStatesViewModel;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDataBinding();
        initViewModel();
        initViews();
        initData();
    }

    private void initDataBinding() {
        mScanfProceedsActivityBinding = DataBindingUtil.setContentView(this, R.layout.scanf_proceeds_activity);
    }

    private void initViewModel() {
        mProceedsStatesViewModel = ViewModelProviders.of(this).get(ProceedsStatesViewModel.class);
        subscribeToModel(mProceedsStatesViewModel);
    }

    private void initData() {
        mScanfProceedsActivityBinding.setHandlers(this);
        mScanfProceedsActivityBinding.setPageData(mProceedsStatesViewModel.getPageData());
        ScanfQRCodeResult qrCodeResult = getIntent().getParcelableExtra(INTENT_DATA_RESULT);
        mProceedsStatesViewModel.getPageData().setCodeId(qrCodeResult.getCodeId());
        mProceedsStatesViewModel.doProceeds();
    }

    private void initViews() {
        mScanfProceedsActivityBinding.mainLayout.setVisibility(View.GONE);
    }

    public void goBack(View view) {
        finish();
    }


    private void subscribeToModel(final ProceedsStatesViewModel model) {

        model.getProceedsStatesLiveData().observe(this, new Observer<ScanfProceedsData>() {
            @Override
            public void onChanged(@Nullable ScanfProceedsData responseData) {
                if (responseData == null) {
                    mScanfProceedsActivityBinding.emptyView.show(false, getResources().getString(R.string.emptyView_mode_desc_fail_desc), getResources().getString(R.string.emptyView_mode_desc_fail_desc), getResources().getString(R.string.emptyView_mode_desc_retry), retryAgainListener);
                    return;
                }
                if (responseData.isError()) {
                    mProceedsStatesViewModel.getPageData().getIsSuccess().set(false);
                    mProceedsStatesViewModel.getPageData().getErrorMes().set(responseData.getMsg());
                    mScanfProceedsActivityBinding.emptyView.setLoadingShowing(false);
                    mScanfProceedsActivityBinding.mainLayout.setVisibility(View.VISIBLE);
                    return;
                }
                mProceedsStatesViewModel.getPageData().getIsSuccess().set(true);
                mProceedsStatesViewModel.getPageData().getMoney().set(responseData.getObj());
                mScanfProceedsActivityBinding.emptyView.setLoadingShowing(false);
                mScanfProceedsActivityBinding.mainLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    private View.OnClickListener retryAgainListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mScanfProceedsActivityBinding.mainLayout.setVisibility(View.GONE);
            mScanfProceedsActivityBinding.emptyView.show(true);
            mScanfProceedsActivityBinding.emptyView.setTitleText("正在进行交易");
            mProceedsStatesViewModel.doProceeds();
        }
    };
}
