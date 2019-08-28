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
import com.askia.coremodel.GlobalUserDataStore;
import com.askia.coremodel.datamodel.http.entities.BaseResponseData;
import com.askia.coremodel.datamodel.http.entities.ScanfQRCodeResult;
import com.askia.coremodel.viewmodel.ScanfBecomeFansViewModel;
import com.yueke.app.bmyp.R;
import com.yueke.app.bmyp.databinding.ScanfBecomeFansActivityBinding;

@Route(path = ARouterPath.ActivityScanfBecomeFans)
public class ScanfBecomeFansActivity extends BaseActivity {

    public static final String INTENT_DATA_RESULT = "INTENT_DATA_RESULT";
    private ScanfBecomeFansActivityBinding mScanfBecomeFansActivityBinding;
    private ScanfBecomeFansViewModel mScanfBecomeFansViewModel;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDataBinding();
        initViewModel();
        initViews();
        initData();
    }

    private void initDataBinding() {
        mScanfBecomeFansActivityBinding = DataBindingUtil.setContentView(this, R.layout.scanf_become_fans_activity);
    }

    private void initViewModel() {
        mScanfBecomeFansViewModel = ViewModelProviders.of(this).get(ScanfBecomeFansViewModel.class);
        subscribeToModel(mScanfBecomeFansViewModel);
    }

    private void initData() {
        mScanfBecomeFansActivityBinding.setHandlers(this);
        mScanfBecomeFansActivityBinding.setPageData(mScanfBecomeFansViewModel.getPageData());
        ScanfQRCodeResult qrCodeResult = getIntent().getParcelableExtra(INTENT_DATA_RESULT);
        mScanfBecomeFansViewModel.getPageData().setCodeId(qrCodeResult.getIntroduceCode());
        mScanfBecomeFansViewModel.becomeFans();
    }

    private void initViews() {
        mScanfBecomeFansActivityBinding.mainLayout.setVisibility(View.GONE);
    }

    public void goBack(View view) {
        finish();
    }


    private void subscribeToModel(final ScanfBecomeFansViewModel model) {

        model.getBecomeFansLiveData().observe(this, new Observer<BaseResponseData>() {
            @Override
            public void onChanged(@Nullable BaseResponseData responseData) {
                if (responseData == null) {
                    mScanfBecomeFansActivityBinding.emptyView.show(false, getResources().getString(R.string.emptyView_mode_desc_fail_desc), getResources().getString(R.string.emptyView_mode_desc_fail_desc), getResources().getString(R.string.emptyView_mode_desc_retry), retryAgainListener);
                    return;
                }
                if (responseData.isError()) {
                    mScanfBecomeFansViewModel.getPageData().getIsSuccess().set(false);
                    mScanfBecomeFansViewModel.getPageData().getErrorMes().set(responseData.getMsg());
                    mScanfBecomeFansActivityBinding.emptyView.setLoadingShowing(false);
                    mScanfBecomeFansActivityBinding.mainLayout.setVisibility(View.VISIBLE);
                    return;
                }
                //更新推荐人
                GlobalUserDataStore.getInstance().setIntroducerName(responseData.getObj().toString());
                mScanfBecomeFansViewModel.getPageData().getIsSuccess().set(true);
                mScanfBecomeFansActivityBinding.emptyView.setLoadingShowing(false);
                mScanfBecomeFansActivityBinding.mainLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    private View.OnClickListener retryAgainListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mScanfBecomeFansActivityBinding.mainLayout.setVisibility(View.GONE);
            mScanfBecomeFansActivityBinding.emptyView.show(true);
            mScanfBecomeFansActivityBinding.emptyView.setTitleText("正在加载");
            mScanfBecomeFansViewModel.becomeFans();
        }
    };

}
