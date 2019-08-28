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
import com.askia.coremodel.datamodel.http.entities.UserCardsData;
import com.askia.coremodel.viewmodel.BuyCardHistoryViewModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yueke.app.bmyp.R;
import com.yueke.app.bmyp.adapters.BuyCardHistoryHistoryAdapter;
import com.yueke.app.bmyp.databinding.BuyCardHistoryActivityBinding;

@Route(path = ARouterPath.ActivityBuyCardHistory)
public class BuyCardHistoryActivity extends BaseActivity {
    private BuyCardHistoryActivityBinding mBuyCardHistoryActivityBinding = null;
    private BuyCardHistoryViewModel mBuyCardHistoryViewModel = null;
    private BuyCardHistoryHistoryAdapter mBuyCardHistoryHistoryAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDataBinding();
        initViewModel();
        initTopBar();
        initViews();
    }

    private void initDataBinding() {
        mBuyCardHistoryActivityBinding = DataBindingUtil.setContentView(this, R.layout.buy_card_history_activity);
    }

    private void initTopBar() {
        mBuyCardHistoryActivityBinding.topBar.setTitle("购买记录").getPaint().setFakeBoldText(true);
        mBuyCardHistoryActivityBinding.topBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //解决返回键不居中问题
        mBuyCardHistoryActivityBinding.topBar.setGravity(Gravity.CENTER_VERTICAL);
    }

    private void initViewModel() {
        mBuyCardHistoryViewModel = ViewModelProviders.of(this).get(BuyCardHistoryViewModel.class);
        subscribeToModel(mBuyCardHistoryViewModel);
    }

    private void initViews() {
        mBuyCardHistoryActivityBinding.historyList.setVisibility(View.GONE);
        mBuyCardHistoryHistoryAdapter = new BuyCardHistoryHistoryAdapter(R.layout.item_cards_history_layout);
        //开启动画(默认为渐显效果)
        mBuyCardHistoryHistoryAdapter.openLoadAnimation();
        //设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(getBaseContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mBuyCardHistoryActivityBinding.historyList.setLayoutManager(layoutManager);
        //设置Item间距
        mBuyCardHistoryActivityBinding.historyList.addItemDecoration(new SpacesItemDecoration(15));
        // 设置适配器
        mBuyCardHistoryActivityBinding.historyList.setAdapter(mBuyCardHistoryHistoryAdapter);
        mBuyCardHistoryHistoryAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                LogUtils.d("onLoadMoreRequested-----------");
                mBuyCardHistoryViewModel.getCardHistoryList();
            }
        }, mBuyCardHistoryActivityBinding.historyList);
    }

    private void subscribeToModel(final BuyCardHistoryViewModel model) {
        //获取买卡历史
        model.getCardHistoryListLiveData().observe(this, new Observer<UserCardsData>() {
            @Override
            public void onChanged(@Nullable UserCardsData responseData) {
                if (mBuyCardHistoryHistoryAdapter.getItemCount() > 0) { //页面已显示数据
                    if (responseData == null) {
                        ToastUtils.showNetNoConnected();
                        mBuyCardHistoryHistoryAdapter.loadMoreFail();
                        return;
                    }
                    if (responseData.isError()) {
                        mBuyCardHistoryHistoryAdapter.loadMoreFail();
                        resolveResponseErrorCode(responseData.getCode(), responseData.getMsg());
                        return;
                    }
                    if (responseData.getList().size() <= 0) {
                        mBuyCardHistoryHistoryAdapter.loadMoreEnd();
                        return;
                    }
                    mBuyCardHistoryHistoryAdapter.addData(responseData.getList());
                    mBuyCardHistoryHistoryAdapter.loadMoreComplete();
                    int index = responseData.getList().size() - 1;
                    mBuyCardHistoryViewModel.updatePage(responseData.getList().get(index).getCurrent());
                } else {
                    if (responseData == null) {
                        mBuyCardHistoryActivityBinding.emptyView.show(false, getResources().getString(R.string.emptyView_mode_desc_fail_desc), getResources().getString(R.string.emptyView_mode_desc_fail_desc), getResources().getString(R.string.emptyView_mode_desc_retry), retryAgainListener);
                        return;
                    }
                    if (responseData.isError()) {
                        mBuyCardHistoryActivityBinding.emptyView.show(false, getResources().getString(R.string.emptyView_mode_desc_fail_title), null, getResources().getString(R.string.emptyView_mode_desc_retry), retryAgainListener);
                        resolveResponseErrorCode(responseData.getCode(), responseData.getMsg());
                        return;
                    }
                    if (responseData.getList() == null || responseData.getList().size() <= 0) {
                        mBuyCardHistoryActivityBinding.emptyView.show(false);
                        mBuyCardHistoryActivityBinding.emptyView.setTitleText("暂无数据");
                        return;
                    }
                    mBuyCardHistoryActivityBinding.emptyView.show(false);
                    mBuyCardHistoryActivityBinding.historyList.setVisibility(View.VISIBLE);
                    mBuyCardHistoryHistoryAdapter.setNewData(responseData.getList());
                    mBuyCardHistoryHistoryAdapter.disableLoadMoreIfNotFullPage(mBuyCardHistoryActivityBinding.historyList);
                    int index = responseData.getList().size() - 1;
                    mBuyCardHistoryViewModel.updatePage(responseData.getList().get(index).getCurrent());
                }
            }
        });
    }


    private View.OnClickListener retryAgainListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mBuyCardHistoryActivityBinding.emptyView.show(true);
            mBuyCardHistoryActivityBinding.emptyView.setTitleText("正在加载");
            mBuyCardHistoryViewModel.getCardHistoryList();
        }
    };
}
