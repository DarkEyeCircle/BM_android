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
import com.alibaba.android.arouter.launcher.ARouter;
import com.askia.common.base.ARouterPath;
import com.askia.common.base.BaseActivity;
import com.askia.common.util.ToastUtils;
import com.askia.common.widget.SpacesItemDecoration;
import com.askia.coremodel.datamodel.http.entities.NearSotreData;
import com.askia.coremodel.viewmodel.StoreListViewModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yueke.app.bmyp.R;
import com.yueke.app.bmyp.adapters.StoreListAdapter;
import com.yueke.app.bmyp.databinding.StoreListActivityBinding;

@Route(path = ARouterPath.ActivityNearStoreList)
public class NearStoreListActivity extends BaseActivity {

    private StoreListActivityBinding mStoreListActivityBinding = null;
    private StoreListViewModel mStoreListViewModel = null;
    private StoreListAdapter mStoreListAdapter = null;

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
        mStoreListActivityBinding = DataBindingUtil.setContentView(this, R.layout.store_list_activity);
    }

    private void initTopBar() {
        mStoreListActivityBinding.topBar.setTitle("门店列表").getPaint().setFakeBoldText(true);
        mStoreListActivityBinding.topBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //解决返回键不居中问题
        mStoreListActivityBinding.topBar.setGravity(Gravity.CENTER_VERTICAL);
    }

    private void initViewModel() {
        mStoreListViewModel = ViewModelProviders.of(NearStoreListActivity.this).get(StoreListViewModel.class);
        subscribeToModel(mStoreListViewModel);
    }

    private void initData() {
        mStoreListViewModel.getStoreList();
    }

    private void initViews() {
        mStoreListActivityBinding.storeList.setVisibility(View.GONE);
        mStoreListAdapter = new StoreListAdapter(R.layout.item_near_store);
        //开启动画(默认为渐显效果)
        mStoreListAdapter.openLoadAnimation();
        //设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(getBaseContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mStoreListActivityBinding.storeList.setLayoutManager(layoutManager);
        //设置Item间距
        mStoreListActivityBinding.storeList.addItemDecoration(new SpacesItemDecoration(1));
        // 设置适配器
        mStoreListActivityBinding.storeList.setAdapter(mStoreListAdapter);
        mStoreListAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mStoreListViewModel.getStoreList();
            }
        }, mStoreListActivityBinding.storeList);
        mStoreListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                NearSotreData.ListBean listBean = (NearSotreData.ListBean) adapter.getItem(position);
                ARouter.getInstance().build(ARouterPath.ActivityStoreDetails)
                        .withParcelable(StoreDetailsActivity.INTENT_DATA, listBean)
                        .navigation();
            }
        });
    }

    private void subscribeToModel(final StoreListViewModel model) {
        //获取门店列表
        model.getStoreListLiveData().observe(this, new Observer<NearSotreData>() {
            @Override
            public void onChanged(@Nullable NearSotreData responseData) {
                if (mStoreListAdapter.getItemCount() > 0) { //页面已显示数据
                    if (responseData == null) {
                        ToastUtils.showNetNoConnected();
                        mStoreListAdapter.loadMoreFail();
                        return;
                    }
                    if (responseData.isError()) {
                        mStoreListAdapter.loadMoreFail();
                        resolveResponseErrorCode(responseData.getCode(), responseData.getMsg());
                        return;
                    }
                    if (responseData.getList().size() <= 0) {
                        mStoreListAdapter.loadMoreEnd();
                        return;
                    }
                    mStoreListAdapter.addData(responseData.getList());
                    mStoreListAdapter.loadMoreComplete();
                    mStoreListViewModel.updatePage(responseData.getCurrent());
                } else {
                    if (responseData == null) {
                        mStoreListActivityBinding.emptyView.show(false, getResources().getString(R.string.emptyView_mode_desc_fail_desc), getResources().getString(R.string.emptyView_mode_desc_fail_desc), getResources().getString(R.string.emptyView_mode_desc_retry), retryAgainListener);
                        return;
                    }
                    if (responseData.isError()) {
                        mStoreListActivityBinding.emptyView.show(false, getResources().getString(R.string.emptyView_mode_desc_fail_title), null, getResources().getString(R.string.emptyView_mode_desc_retry), retryAgainListener);
                        resolveResponseErrorCode(responseData.getCode(), responseData.getMsg());
                        return;
                    }
                    if (responseData.getList() == null || responseData.getList().size() <= 0) {
                        mStoreListActivityBinding.emptyView.show(false);
                        mStoreListActivityBinding.emptyView.setTitleText("暂无数据");
                        return;
                    }
                    mStoreListActivityBinding.emptyView.show(false);
                    mStoreListActivityBinding.storeList.setVisibility(View.VISIBLE);
                    mStoreListAdapter.setNewData(responseData.getList());
                    mStoreListAdapter.disableLoadMoreIfNotFullPage(mStoreListActivityBinding.storeList);
                    mStoreListViewModel.updatePage(responseData.getCurrent());
                }
            }
        });
    }


    private View.OnClickListener retryAgainListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mStoreListActivityBinding.emptyView.show(true);
            mStoreListActivityBinding.emptyView.setTitleText("正在加载");
            mStoreListViewModel.getStoreList();
        }
    };
}
