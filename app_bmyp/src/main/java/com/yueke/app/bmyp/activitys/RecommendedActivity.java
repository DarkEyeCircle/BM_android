package com.yueke.app.bmyp.activitys;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.askia.common.base.ARouterPath;
import com.askia.common.base.BaseActivity;
import com.askia.common.util.ToastUtils;
import com.askia.common.widget.SpacesItemDecoration;
import com.askia.coremodel.datamodel.http.entities.FansData;
import com.askia.coremodel.viewmodel.RecommendedViewModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yueke.app.bmyp.R;
import com.yueke.app.bmyp.adapters.FansListAdapter;
import com.yueke.app.bmyp.databinding.RecommendedActivityBinding;

@Route(path = ARouterPath.ActivityRecommended)
public class RecommendedActivity extends BaseActivity {

    private RecommendedActivityBinding mRecommendedActivityBinding = null;
    private RecommendedViewModel mRecommendedViewModel = null;
    private FansListAdapter mFansListAdapter = null;
    private Button btMultiUserChat = null;

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
        mRecommendedActivityBinding = DataBindingUtil.setContentView(this, R.layout.recommended_activity);
    }

    private void initTopBar() {
        mRecommendedActivityBinding.topBar.setTitle(R.string.recommended).getPaint().setFakeBoldText(true);
        mRecommendedActivityBinding.topBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btMultiUserChat = mRecommendedActivityBinding.topBar.addRightTextButton("多人聊天", R.id.bt_save);
        btMultiUserChat.setTextColor(Color.BLACK);
        btMultiUserChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtils.info("功能尚未开放");
            }
        });
        //解决返回键不居中问题
        mRecommendedActivityBinding.topBar.setGravity(Gravity.CENTER_VERTICAL);
    }

    private void initViewModel() {
        mRecommendedViewModel = ViewModelProviders.of(RecommendedActivity.this).get(RecommendedViewModel.class);
        subscribeToModel(mRecommendedViewModel);
    }

    private void initData() {
        mRecommendedActivityBinding.setHandlers(this);
        mRecommendedActivityBinding.setPageData(mRecommendedViewModel.getPageData());
    }

    private void initViews() {
        btMultiUserChat.setVisibility(View.GONE);
        mRecommendedActivityBinding.noFansLayout.setVisibility(View.GONE);
        mRecommendedActivityBinding.fansLayout.setVisibility(View.GONE);

        mFansListAdapter = new FansListAdapter(R.layout.item_fans_layout);
        //开启动画(默认为渐显效果)
        mFansListAdapter.openLoadAnimation();
        //设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(getBaseContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecommendedActivityBinding.fansList.setLayoutManager(layoutManager);
        //设置Item间距
        mRecommendedActivityBinding.fansList.addItemDecoration(new SpacesItemDecoration(1));
        // 设置适配器
        mRecommendedActivityBinding.fansList.setAdapter(mFansListAdapter);
        mFansListAdapter.setOnItemChildClickListener((adapter, view, position) -> {

        });
        //设置上拉刷新 加载更多数据
        mFansListAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mRecommendedViewModel.getFansListData();
            }
        }, mRecommendedActivityBinding.fansList);
        mFansListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.bt_chat) {
                    ToastUtils.info("功能尚未开放");
                    return;
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        btMultiUserChat.setVisibility(View.GONE);
        mRecommendedActivityBinding.emptyView.show(true);
        mRecommendedActivityBinding.emptyView.setTitleText("正在加载");
        mRecommendedViewModel.getFansListData();

    }

    //分享海报
    public void jumpToShare(View view) {
        ARouter.getInstance().build(ARouterPath.ActivityMyPosters).navigation();
    }

    private void subscribeToModel(final RecommendedViewModel model) {
        //获取银行卡列表
        model.getFansDataListLiveData().observe(this, new Observer<FansData>() {
            @Override
            public void onChanged(@Nullable FansData responseData) {
                if (mFansListAdapter.getItemCount() > 0) { //页面已显示数据
                    if (responseData == null) {
                        ToastUtils.showNetNoConnected();
                        mFansListAdapter.loadMoreFail();
                        return;
                    }
                    if (responseData.isError()) {
                        mFansListAdapter.loadMoreFail();
                        resolveResponseErrorCode(responseData.getCode(), responseData.getMsg());
                        return;
                    }
                    if (responseData.getList().size() <= 0) {
                        mFansListAdapter.loadMoreEnd();
                        return;
                    }
                    mFansListAdapter.addData(responseData.getList());
                    mFansListAdapter.loadMoreComplete();
                    mRecommendedViewModel.updatePage(responseData.getCurrent());
                } else {
                    if (responseData == null) {
                        mRecommendedActivityBinding.emptyView.show(false, getResources().getString(R.string.emptyView_mode_desc_fail_desc), getResources().getString(R.string.emptyView_mode_desc_fail_desc), getResources().getString(R.string.emptyView_mode_desc_retry), retryAgainListener);
                        return;
                    }
                    if (responseData.isError()) {
                        mRecommendedActivityBinding.emptyView.show(false, getResources().getString(R.string.emptyView_mode_desc_fail_title), null, getResources().getString(R.string.emptyView_mode_desc_retry), retryAgainListener);
                        resolveResponseErrorCode(responseData.getCode(), responseData.getMsg());
                        return;
                    }
                    if (responseData.getList() == null || responseData.getList().size() <= 0) {
                        mRecommendedActivityBinding.emptyView.show(false);
                        mRecommendedActivityBinding.noFansLayout.setVisibility(View.VISIBLE);
                        return;
                    }
                    btMultiUserChat.setVisibility(View.VISIBLE);
                    mRecommendedActivityBinding.emptyView.show(false);
                    mRecommendedActivityBinding.fansLayout.setVisibility(View.VISIBLE);
                    mFansListAdapter.setNewData(responseData.getList());
                    mFansListAdapter.disableLoadMoreIfNotFullPage(mRecommendedActivityBinding.fansList);
                    mRecommendedViewModel.updatePage(responseData.getCurrent());
                }
            }
        });

    }

    private View.OnClickListener retryAgainListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            btMultiUserChat.setVisibility(View.GONE);
            mRecommendedActivityBinding.emptyView.show(true);
            mRecommendedActivityBinding.emptyView.setTitleText("正在加载");
            mRecommendedViewModel.getFansListData();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            btMultiUserChat.setVisibility(View.GONE);
            mRecommendedActivityBinding.emptyView.show(true);
            mRecommendedActivityBinding.emptyView.setTitleText("正在加载");
            mRecommendedViewModel.getFansListData();
        }
    }

}
