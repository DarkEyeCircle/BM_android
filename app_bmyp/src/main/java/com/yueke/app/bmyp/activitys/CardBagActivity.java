package com.yueke.app.bmyp.activitys;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.apkfuns.logutils.LogUtils;
import com.askia.common.base.ARouterPath;
import com.askia.common.base.BaseActivity;
import com.askia.common.widget.SpacesItemDecoration;
import com.askia.coremodel.datamodel.http.entities.CardsData;
import com.askia.coremodel.viewmodel.CardBagViewModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yueke.app.bmyp.R;
import com.yueke.app.bmyp.adapters.CardBagListAdapter;
import com.yueke.app.bmyp.databinding.CardBagActivityBinding;

@Route(path = ARouterPath.ActivityCardBag)
public class CardBagActivity extends BaseActivity {

    private CardBagActivityBinding mCardBagActivityBinding = null;
    private CardBagListAdapter mCardBagListAdapter = null;
    private CardBagViewModel mCardBagViewModel = null;
    private Button bt_history = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDataBinding();
        initViewModel();
        initTopBar();
        initViews();
    }

    private void initDataBinding() {
        mCardBagActivityBinding = DataBindingUtil.setContentView(this, R.layout.card_bag_activity);
    }

    private void initTopBar() {
        mCardBagActivityBinding.topBar.setTitle(getResources().getString(R.string.card_bag)).getPaint().setFakeBoldText(true);
        mCardBagActivityBinding.topBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        bt_history = mCardBagActivityBinding.topBar.addRightTextButton("购买记录", R.id.bt_save);
        bt_history.setTextColor(getResources().getColor(R.color.black));
        bt_history.setOnClickListener(view -> {
            ARouter.getInstance().build(ARouterPath.ActivityBuyCardHistory).navigation();
        });
        //解决返回键不居中问题
        mCardBagActivityBinding.topBar.setGravity(Gravity.CENTER_VERTICAL);
    }

    private void initViewModel() {
        mCardBagViewModel = ViewModelProviders.of(this).get(CardBagViewModel.class);
        subscribeToModel(mCardBagViewModel);
    }

    private void initViews() {
        bt_history.setVisibility(View.GONE);
        mCardBagActivityBinding.cardList.setVisibility(View.GONE);
        mCardBagListAdapter = new CardBagListAdapter(R.layout.item_cards_layout);
        //开启动画(默认为渐显效果)
        mCardBagListAdapter.openLoadAnimation();
        //设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(getBaseContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mCardBagActivityBinding.cardList.setLayoutManager(layoutManager);
        //设置Item间距
        mCardBagActivityBinding.cardList.addItemDecoration(new SpacesItemDecoration(15));
        // 设置适配器
        mCardBagActivityBinding.cardList.setAdapter(mCardBagListAdapter);
        //设置上拉刷新 加载更多数据
        mCardBagListAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                LogUtils.d("onLoadMoreRequested------------------");
                mCardBagViewModel.getCardList();
            }
        }, mCardBagActivityBinding.cardList);
        mCardBagListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.bt_buy) {
                    CardsData.ListBean listBean = (CardsData.ListBean) adapter.getItem(position);
                    ARouter.getInstance().build(ARouterPath.ActivityBuyCard)
                            .withString(BuyCardActivity.INTENT_DATA_VALUE, listBean.getFaceValue())
                            .withString(BuyCardActivity.INTENT_DATA_URL, listBean.getCoverUrl())
                            .withString(BuyCardActivity.INTENT_DATA_ID, listBean.getId())
                            .withString(BuyCardActivity.INTENT_DATA_INTEGRAL_MULRIPLE, listBean.getMultiple())
                            .navigation();
                }
            }
        });
    }

    private void subscribeToModel(final CardBagViewModel model) {
        //获取卡包
        model.getCardListLiveData().observe(this, new Observer<CardsData>() {
            @Override
            public void onChanged(@Nullable CardsData responseData) {
                if (mCardBagListAdapter.getItemCount() > 0) { //页面已显示数据
                    if (responseData == null) {
                        mCardBagListAdapter.loadMoreFail();
                        return;
                    }
                    if (responseData.isError()) {
                        mCardBagListAdapter.loadMoreFail();
                        resolveResponseErrorCode(responseData.getCode(), responseData.getMsg());
                        return;
                    }
                    if (responseData.getList().size() <= 0) {
                        mCardBagListAdapter.loadMoreEnd();
                        return;
                    }
                    mCardBagListAdapter.addData(responseData.getList());
                    mCardBagListAdapter.loadMoreComplete();
                    mCardBagViewModel.updatePage(responseData.getCurrent());
                } else {
                    if (responseData == null) {
                        mCardBagActivityBinding.emptyView.show(false, getResources().getString(R.string.emptyView_mode_desc_fail_desc), getResources().getString(R.string.emptyView_mode_desc_fail_desc), getResources().getString(R.string.emptyView_mode_desc_retry), retryAgainListener);
                        return;
                    }
                    if (responseData.isError()) {
                        mCardBagActivityBinding.emptyView.show(false, getResources().getString(R.string.emptyView_mode_desc_fail_title), null, getResources().getString(R.string.emptyView_mode_desc_retry), retryAgainListener);
                        resolveResponseErrorCode(responseData.getCode(), responseData.getMsg());
                        return;
                    }
                    if (responseData.getList() == null || responseData.getList().size() <= 0) {
                        mCardBagActivityBinding.emptyView.show(false);
                        mCardBagActivityBinding.emptyView.setTitleText("暂无数据");
                        return;
                    }
                    bt_history.setVisibility(View.VISIBLE);
                    mCardBagActivityBinding.emptyView.show(false);
                    mCardBagActivityBinding.cardList.setVisibility(View.VISIBLE);
                    mCardBagListAdapter.setNewData(responseData.getList());
                    mCardBagListAdapter.disableLoadMoreIfNotFullPage(mCardBagActivityBinding.cardList);
                    mCardBagViewModel.updatePage(responseData.getCurrent());
                }
            }
        });
    }


    private View.OnClickListener retryAgainListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            bt_history.setVisibility(View.GONE);
            mCardBagActivityBinding.emptyView.show(true);
            mCardBagActivityBinding.emptyView.setTitleText("正在加载");
            mCardBagViewModel.getCardList();
        }
    };

}
