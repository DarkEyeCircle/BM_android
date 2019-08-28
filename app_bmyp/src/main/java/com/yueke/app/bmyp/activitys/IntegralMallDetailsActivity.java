package com.yueke.app.bmyp.activitys;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.apkfuns.logutils.LogUtils;
import com.askia.common.base.ARouterPath;
import com.askia.common.base.BaseActivity;
import com.askia.common.util.ToastUtils;
import com.askia.common.widget.SpacesItemDecoration;
import com.askia.coremodel.datamodel.http.entities.IntegralMallDetailsData;
import com.askia.coremodel.viewmodel.IntegralMallDetilsViewModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yueke.app.bmyp.R;
import com.yueke.app.bmyp.adapters.IntegralMallDetailsAdapter;
import com.yueke.app.bmyp.databinding.BillsDetailsActivityBinding;

@Route(path = ARouterPath.ActivityIntegralMallDetails)
public class IntegralMallDetailsActivity extends BaseActivity {

    private BillsDetailsActivityBinding mBillsDetailsActivityBinding = null;
    private IntegralMallDetilsViewModel mIntegralMallDetilsViewModel = null;
    private IntegralMallDetailsAdapter mIntegralMallDetailsAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDataBinding();
        initViewModel();
        initTopBar();
        initViews();
    }

    private void initDataBinding() {
        mBillsDetailsActivityBinding = DataBindingUtil.setContentView(this, R.layout.bills_details_activity);
    }

    private void initTopBar() {
        mBillsDetailsActivityBinding.topBar.setTitle("积分商城明细").getPaint().setFakeBoldText(true);
        mBillsDetailsActivityBinding.topBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //解决返回键不居中问题
        mBillsDetailsActivityBinding.topBar.setGravity(Gravity.CENTER_VERTICAL);
    }

    private void initViewModel() {
        mIntegralMallDetilsViewModel = ViewModelProviders.of(this).get(IntegralMallDetilsViewModel.class);
        subscribeToModel(mIntegralMallDetilsViewModel);
    }

    private void initViews() {
        mBillsDetailsActivityBinding.billList.setVisibility(View.GONE);
        mIntegralMallDetailsAdapter = new IntegralMallDetailsAdapter(R.layout.item_bills_layout);
        //开启动画(默认为渐显效果)
        mIntegralMallDetailsAdapter.openLoadAnimation();
        //设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(getBaseContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mBillsDetailsActivityBinding.billList.setLayoutManager(layoutManager);
        //设置Item间距
        mBillsDetailsActivityBinding.billList.addItemDecoration(new SpacesItemDecoration(1));
        // 设置适配器
        mBillsDetailsActivityBinding.billList.setAdapter(mIntegralMallDetailsAdapter);
        mIntegralMallDetailsAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mIntegralMallDetilsViewModel.getBills();
            }
        }, mBillsDetailsActivityBinding.billList);
    }

    private void subscribeToModel(final IntegralMallDetilsViewModel model) {
        //获取账单详细
        model.getBillsListLiveData().observe(this, new Observer<IntegralMallDetailsData>() {
            @Override
            public void onChanged(@Nullable IntegralMallDetailsData responseData) {
                if (mIntegralMallDetailsAdapter.getItemCount() > 0) { //页面已显示数据
                    if (responseData == null) {
                        ToastUtils.showNetNoConnected();
                        mIntegralMallDetailsAdapter.loadMoreFail();
                        return;
                    }
                    if (responseData.isError()) {
                        mIntegralMallDetailsAdapter.loadMoreFail();
                        resolveResponseErrorCode(responseData.getCode(), responseData.getMsg());
                        return;
                    }
                    if (responseData.getList().size() <= 0) {
                        mIntegralMallDetailsAdapter.loadMoreEnd();
                        return;
                    }
                    mIntegralMallDetailsAdapter.addData(responseData.getList());
                    mIntegralMallDetailsAdapter.loadMoreComplete();
                    mIntegralMallDetilsViewModel.updatePage(responseData.getCurrent());
                } else {
                    if (responseData == null) {
                        mBillsDetailsActivityBinding.emptyView.show(false, getResources().getString(R.string.emptyView_mode_desc_fail_desc), getResources().getString(R.string.emptyView_mode_desc_fail_desc), getResources().getString(R.string.emptyView_mode_desc_retry), retryAgainListener);
                        return;
                    }
                    if (responseData.isError()) {
                        mBillsDetailsActivityBinding.emptyView.show(false, getResources().getString(R.string.emptyView_mode_desc_fail_title), null, getResources().getString(R.string.emptyView_mode_desc_retry), retryAgainListener);
                        resolveResponseErrorCode(responseData.getCode(), responseData.getMsg());
                        return;
                    }
                    if (responseData.getList() == null || responseData.getList().size() <= 0) {
                        mBillsDetailsActivityBinding.emptyView.show(false);
                        mBillsDetailsActivityBinding.emptyView.setTitleText("暂无数据");
                        return;
                    }
                    mBillsDetailsActivityBinding.emptyView.show(false);
                    mBillsDetailsActivityBinding.billList.setVisibility(View.VISIBLE);
                    mIntegralMallDetailsAdapter.setNewData(responseData.getList());
                    mIntegralMallDetailsAdapter.disableLoadMoreIfNotFullPage(mBillsDetailsActivityBinding.billList);
                    mIntegralMallDetilsViewModel.updatePage(responseData.getCurrent());
                }
            }
        });
    }


    private View.OnClickListener retryAgainListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mBillsDetailsActivityBinding.emptyView.show(true);
            mBillsDetailsActivityBinding.emptyView.setTitleText("正在加载");
            mIntegralMallDetilsViewModel.getBills();
        }
    };
}
