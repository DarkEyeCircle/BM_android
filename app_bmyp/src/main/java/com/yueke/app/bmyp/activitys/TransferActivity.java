package com.yueke.app.bmyp.activitys;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.apkfuns.logutils.LogUtils;
import com.askia.common.base.ARouterPath;
import com.askia.common.base.BaseActivity;
import com.askia.common.util.ToastUtils;
import com.askia.common.widget.SpacesItemDecoration;
import com.askia.coremodel.GlobalUserDataStore;
import com.askia.coremodel.datamodel.http.entities.BillDetailsData;
import com.askia.coremodel.viewmodel.TransferViewModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yueke.app.bmyp.R;
import com.yueke.app.bmyp.adapters.TransferHistoryListAdapter;
import com.yueke.app.bmyp.databinding.TransferActivityBinding;

@Route(path = ARouterPath.ActivityTransfer)
public class TransferActivity extends BaseActivity {

    private TransferActivityBinding mTransferActivityBinding;
    private TransferViewModel mTransferViewModel;
    private TransferHistoryListAdapter mTransferHistoryListAdapter = null;

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
        mTransferActivityBinding = DataBindingUtil.setContentView(this, R.layout.transfer_activity);
    }

    private void initTopBar() {
        mTransferActivityBinding.topBar.setTitle(R.string.transfer).getPaint().setFakeBoldText(true);
        mTransferActivityBinding.topBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //解决返回键不居中问题
        mTransferActivityBinding.topBar.setGravity(Gravity.CENTER_VERTICAL);
    }

    private void initViewModel() {
        mTransferViewModel = ViewModelProviders.of(this).get(TransferViewModel.class);
        subscribeToModel(mTransferViewModel);
    }

    private void initData() {
        mTransferActivityBinding.setHandlers(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mTransferActivityBinding.mainLayout.setVisibility(View.GONE);
        mTransferActivityBinding.emptyView.show(true);
        mTransferActivityBinding.emptyView.setTitleText("正在加载");
        //重新获取数据
        if (mTransferHistoryListAdapter != null && mTransferHistoryListAdapter.getData() != null) {
            mTransferHistoryListAdapter.getData().clear();
        }
        mTransferViewModel.updatePage(0);
        mTransferViewModel.getTransferHistory();
    }

    private void initViews() {
        mTransferHistoryListAdapter = new TransferHistoryListAdapter(R.layout.item_transfer_history_layout);
        //开启动画(默认为渐显效果)
        mTransferHistoryListAdapter.openLoadAnimation();
        //设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(getBaseContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mTransferActivityBinding.rlvHistory.setLayoutManager(layoutManager);
        //设置Item间距
        mTransferActivityBinding.rlvHistory.addItemDecoration(new SpacesItemDecoration(1));
        // 设置适配器
        mTransferActivityBinding.rlvHistory.setAdapter(mTransferHistoryListAdapter);
        //Empty View
        TextView textView = new TextView(getBaseContext());
        textView.setText("暂无数据");
        textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        textView.setPadding(0, 40, 0, 0);
        mTransferHistoryListAdapter.setEmptyView(textView);
        mTransferHistoryListAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mTransferViewModel.getTransferHistory();
            }
        }, mTransferActivityBinding.rlvHistory);
        LogUtils.d("count:" + mTransferHistoryListAdapter.getItemCount());
    }

    public void transferToBM(View view) {
        ARouter.getInstance().build(ARouterPath.ActivityTransferToBMWallet)
                .withInt(TransferToBMWalletActivity.TransferlType, TransferViewModel.TransferType.TransferToBM.getType())
                .navigation();
    }

    public void transferToAlipay(View view) {
        ARouter.getInstance().build(ARouterPath.ActivityTransferToAlipay)
                .withInt(TransferToBMWalletActivity.TransferlType, TransferViewModel.TransferType.TransferToAlipay.getType())
                .navigation();
    }

    public void transferToBankCard(View view) {
        ARouter.getInstance().build(ARouterPath.ActivityTransferToBankCard)
                .withInt(TransferToBMWalletActivity.TransferlType, TransferViewModel.TransferType.TransferToBankCard.getType())
                .navigation();
    }

    private void subscribeToModel(final TransferViewModel model) {
        //获取账单详细
        model.getTransferHistoryLiveData().observe(this, new Observer<BillDetailsData>() {
            @Override
            public void onChanged(@Nullable BillDetailsData responseData) {
                if (mTransferHistoryListAdapter.getData().size() > 0) { //页面已显示数据
                    if (responseData == null) {
                        ToastUtils.showNetNoConnected();
                        mTransferHistoryListAdapter.loadMoreFail();
                        return;
                    }
                    if (responseData.isError()) {
                        resolveResponseErrorCode(responseData.getCode(), responseData.getMsg());
                        return;
                    }


                    if (responseData.getList().size() <= 0) {
                        mTransferHistoryListAdapter.loadMoreEnd();
                        return;
                    }
                    mTransferHistoryListAdapter.addData(responseData.getList());
                    mTransferHistoryListAdapter.loadMoreComplete();
                    mTransferViewModel.updatePage(responseData.getCurrent());
                } else {
                    if (responseData == null) {
                        mTransferActivityBinding.emptyView.show(false, getResources().getString(R.string.emptyView_mode_desc_fail_desc), getResources().getString(R.string.emptyView_mode_desc_fail_desc), getResources().getString(R.string.emptyView_mode_desc_retry), retryAgainListener);
                        return;
                    }
                    if (responseData.isError()) {
                        mTransferActivityBinding.emptyView.show(false, getResources().getString(R.string.emptyView_mode_desc_fail_title), null, getResources().getString(R.string.emptyView_mode_desc_retry), retryAgainListener);
                        resolveResponseErrorCode(responseData.getCode(), responseData.getMsg());
                        return;
                    }
                    mTransferActivityBinding.emptyView.show(false);
                    mTransferActivityBinding.mainLayout.setVisibility(View.VISIBLE);
                    if (responseData.getList() == null || responseData.getList().size() <= 0) {
                        return;
                    }
                    mTransferHistoryListAdapter.setNewData(responseData.getList());
                    mTransferHistoryListAdapter.disableLoadMoreIfNotFullPage(mTransferActivityBinding.rlvHistory);
                    mTransferViewModel.updatePage(responseData.getCurrent());
                }
            }
        });
    }


    private View.OnClickListener retryAgainListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mTransferActivityBinding.emptyView.show(true);
            mTransferActivityBinding.emptyView.setTitleText("正在加载");
            mTransferViewModel.getTransferHistory();
        }
    };
}
