package com.yueke.app.bmyp.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.askia.common.base.ARouterPath;
import com.askia.common.base.BaseFragment;
import com.askia.common.util.ToastUtils;
import com.askia.common.util.Utils;
import com.askia.common.widget.SpacesItemDecoration;
import com.askia.coremodel.GlobalUserDataStore;
import com.askia.coremodel.datamodel.http.entities.NearSotreData;
import com.askia.coremodel.datamodel.http.entities.TodayRecommendedData;
import com.askia.coremodel.datamodel.loaction.LocationData;
import com.askia.coremodel.util.RxUtils;
import com.askia.coremodel.viewmodel.HomeViewModel;
import com.baidu.location.BDLocation;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.sunfusheng.marqueeview.MarqueeView;
import com.yueke.app.bmyp.R;
import com.yueke.app.bmyp.activitys.LoginActivity;
import com.yueke.app.bmyp.activitys.StoreDetailsActivity;
import com.yueke.app.bmyp.adapters.HomeAdapter;
import com.yueke.app.bmyp.databinding.HomeFragmentBinding;
import com.yueke.app.bmyp.databinding.HomeFragmetBodyBinding;
import com.yueke.app.bmyp.widgets.MyContentBehavior;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;

@Route(path = ARouterPath.FragmentHome)
public class HomeFragment extends BaseFragment {

    private HomeFragmentBinding mHomeFragmentBinding;
    private HomeFragmetBodyBinding mHomeFragmetBodyBinding;
    private HomeViewModel mHomeViewModel;
    private HomeAdapter mHomeAdapter = null;
    private boolean isFirst = true;
    private List<HomeAdapter.HomeListBean> mHomeListBeanList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        QMUIStatusBarHelper.setStatusBarDarkMode(getActivity());
        mHomeFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.home_fragment, container, false);
        initViewModel();
        initData();
        initViews();
        //请求定位权限
        RxUtils.RequestLocationPermisoon(getHoldingActivity(), new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if (aBoolean) {
                    subscribeToModel(mHomeViewModel);
                }
            }
        });
        return mHomeFragmentBinding.getRoot();
    }

    private void initViewModel() {
        mHomeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
    }

    private void initData() {
        mHomeFragmentBinding.setHandlers(this);
        mHomeFragmentBinding.setPageData(mHomeViewModel.getPageData());
    }

    private void initViews() {
        MyContentBehavior behavior = new MyContentBehavior();
        CoordinatorLayout.LayoutParams params =
                (CoordinatorLayout.LayoutParams) mHomeFragmentBinding.dataView.getLayoutParams();
        params.setBehavior(behavior);
        mHomeFragmentBinding.dataView.setNestedScrollingEnabled(true);
        //初始化Adapter
        mHomeAdapter = new HomeAdapter(mHomeListBeanList);
        mHomeFragmetBodyBinding = DataBindingUtil.bind(LayoutInflater.from(getHoldingActivity()).inflate(R.layout.home_fragmet_body, null));
        mHomeFragmetBodyBinding.setHandlers(HomeFragment.this);
        mHomeAdapter.addHeaderView(mHomeFragmetBodyBinding.getRoot());
        mHomeAdapter.addFooterView(LayoutInflater.from(getHoldingActivity()).inflate(R.layout.item_home_data_bottom_layout, null));
        //开启动画(默认为渐显效果)
        mHomeAdapter.openLoadAnimation();
        //设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(getHoldingActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mHomeFragmentBinding.dataView.setLayoutManager(layoutManager);
        //设置Item间距
        mHomeFragmentBinding.dataView.addItemDecoration(new SpacesItemDecoration(1));
        // 设置适配器
        mHomeFragmentBinding.dataView.setAdapter(mHomeAdapter);
        //显示今日推荐假数据
        showRecommendData();
        //监听事件
        mHomeAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.bt_more) {
                    if (!GlobalUserDataStore.getInstance().isLogin()) {
                        ARouter.getInstance().build(ARouterPath.ActivityLogin).withBoolean(LoginActivity.IntentFromMine, true).navigation();
                        return;
                    }
                    ARouter.getInstance().build(ARouterPath.ActivityNearStoreList)
                            .navigation();
                }
            }
        });
        mHomeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (!GlobalUserDataStore.getInstance().isLogin()) {
                    ARouter.getInstance().build(ARouterPath.ActivityLogin).withBoolean(LoginActivity.IntentFromMine, true).navigation();
                    return;
                }
                HomeAdapter.HomeListBean listBean = (HomeAdapter.HomeListBean) adapter.getItem(position);
                switch (listBean.getItemType()) {
                    case HomeAdapter.HomeListBean.TYPE_NEAR_STORE:
                        ARouter.getInstance().build(ARouterPath.ActivityStoreDetails)
                                .withParcelable(StoreDetailsActivity.INTENT_DATA, (NearSotreData.ListBean) listBean.getHomeFragmentData())
                                .navigation();
                        break;
                    case HomeAdapter.HomeListBean.TYPE_RECOMMENDED:
                        ToastUtils.info("功能未开发");
                        break;

                }
            }
        });

    }

    //跳转到二维码扫描界面
    public void jumpToScanOR(View view) {
        if (!GlobalUserDataStore.getInstance().isLogin()) {
            ARouter.getInstance().build(ARouterPath.ActivityLogin).withBoolean(LoginActivity.IntentFromMine, true).navigation();
            return;
        }
        RxUtils.RequestCameraPermissions(getHoldingActivity(), new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if (aBoolean) {
                    ARouter.getInstance().build(ARouterPath.ActivityScanQR).navigation();
                } else {
                    ToastUtils.info("没有相机权限");
                }
            }
        });
    }

    //跳转到付款界面
    public void jumpToPayment(View view) {
        if (!GlobalUserDataStore.getInstance().isLogin()) {
            ARouter.getInstance().build(ARouterPath.ActivityLogin).withBoolean(LoginActivity.IntentFromMine, true).navigation();
            return;
        }
        ARouter.getInstance().build(ARouterPath.ActivityPayment).navigation();
    }

    //跳转到收款界面
    public void jumpToProceeds(View view) {
        if (!GlobalUserDataStore.getInstance().isLogin()) {
            ARouter.getInstance().build(ARouterPath.ActivityLogin).withBoolean(LoginActivity.IntentFromMine, true).navigation();
            return;
        }
        if (!GlobalUserDataStore.getInstance().isIdentityApprove()) {
            ARouter.getInstance().build(ARouterPath.ActivityIdentityApprove).navigation();
            return;
        }
        ARouter.getInstance().build(ARouterPath.ActivityProceeds).navigation();
    }


    //跳转到消息界面
    public void jumpToMes(View view) {
        if (!GlobalUserDataStore.getInstance().isLogin()) {
            ARouter.getInstance().build(ARouterPath.ActivityLogin).withBoolean(LoginActivity.IntentFromMine, true).navigation();
            return;
        }
        ToastUtils.info("功能未开放");
    }

    //跳转到充值界面
    public void jumpToRecharge(View view) {
        if (!GlobalUserDataStore.getInstance().isLogin()) {
            ARouter.getInstance().build(ARouterPath.ActivityLogin).withBoolean(LoginActivity.IntentFromMine, true).navigation();
            return;
        }
        ARouter.getInstance().build(ARouterPath.ActivityRecharge).navigation();
    }

    //跳转到卡包界面
    public void jumpToCardBag(View view) {
        if (!GlobalUserDataStore.getInstance().isLogin()) {
            ARouter.getInstance().build(ARouterPath.ActivityLogin).withBoolean(LoginActivity.IntentFromMine, true).navigation();
            return;
        }
        ARouter.getInstance().build(ARouterPath.ActivityCardBag).navigation();
    }

    //跳转到转账界面
    public void jumpToTransfer(View view) {
        if (!GlobalUserDataStore.getInstance().isLogin()) {
            ARouter.getInstance().build(ARouterPath.ActivityLogin).withBoolean(LoginActivity.IntentFromMine, true).navigation();
            return;
        }
        ARouter.getInstance().build(ARouterPath.ActivityTransfer).navigation();
    }

    //跳转到社交圈界面
    public void jumpToSocialCircle(View view) {
        ToastUtils.info("功能未开放");
//        ARouter.getInstance().build(ARouterPath.ActivitySocialCircle).navigation();
    }

    //消息模块
    public void jumpToChat(View view) {
        if (!GlobalUserDataStore.getInstance().isLogin()) {
            ARouter.getInstance().build(ARouterPath.ActivityLogin).withBoolean(LoginActivity.IntentFromMine, true).navigation();
            return;
        }
        ToastUtils.info("功能未开放");
    }

    @Override
    public void onStart() {
        super.onStart();
        RxUtils.RequestLocationPermisoon(getActivity(), new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if (aBoolean) {
                    LocationData.init(Utils.getContext());
                }
            }
        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            QMUIStatusBarHelper.setStatusBarLightMode(getActivity());
        } else {
            QMUIStatusBarHelper.setStatusBarDarkMode(getActivity());
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void subscribeToModel(final HomeViewModel model) {
        model.getLocationLiveData().observe(this, new Observer<BDLocation>() {
            @Override
            public void onChanged(@Nullable BDLocation bdLocation) {
                mHomeViewModel.getPageData().updateLocation(bdLocation);
                String province = bdLocation.getProvince();
                String city = bdLocation.getCity();
                if (!GlobalUserDataStore.getInstance().isLogin()) {
                    return;
                }
                if (bdLocation != null && !TextUtils.isEmpty(province) && !TextUtils.isEmpty(city)) {
                    if (isFirst) {
                        mHomeViewModel.getNearStoreList();
                        isFirst = false;
                    }
                }
            }
        });
        model.getNearStoreLiveData().observe(this, new Observer<NearSotreData>() {
            @Override
            public void onChanged(@Nullable NearSotreData nearSotreData) {
                if (nearSotreData == null) {
                    ToastUtils.showNetNoConnected();
                    return;
                }
                if (nearSotreData.isError() || nearSotreData.getList() == null || nearSotreData.getList().size() <= 0) {
                    //ToastUtils.info("附近门店加载失败");
                    return;
                }
                /*今日推荐*/
                HomeAdapter.HomeListBean todayRecommondedTitle = new HomeAdapter.HomeListBean(HomeAdapter.HomeListBean.TYPE_TODAY_RECOMMEND_TTTLE, "附近门店");
                mHomeListBeanList.add(todayRecommondedTitle);

                for (NearSotreData.ListBean listBean : nearSotreData.getList()) {
                    HomeAdapter.HomeListBean homeListBean = new HomeAdapter.HomeListBean(HomeAdapter.HomeListBean.TYPE_NEAR_STORE, listBean);
                    mHomeListBeanList.add(homeListBean);
                }
                mHomeAdapter.notifyDataSetChanged();
            }
        });
    }

    private void showRecommendData() {
        //今日推荐
        HomeAdapter.HomeListBean todayRecommondedTitle = new HomeAdapter.HomeListBean(HomeAdapter.HomeListBean.TYPE_RECOMMENDED_TTTLE, "今日推荐");
        mHomeListBeanList.add(todayRecommondedTitle);
        //模拟网络数据
        TodayRecommendedData.ListBean recommondedData = new TodayRecommendedData.ListBean();
        recommondedData.setDesc("畅呼吸空气净化器");
        recommondedData.setImageUrl(R.drawable.test_bg2);
        HomeAdapter.HomeListBean dataBean = new HomeAdapter.HomeListBean(HomeAdapter.HomeListBean.TYPE_RECOMMENDED, recommondedData);
        mHomeListBeanList.add(dataBean);
        mHomeListBeanList.add(dataBean);
        mHomeListBeanList.add(dataBean);
        /*热门资讯*/
        List<String> info = new ArrayList<>();
        info.add("功能未开放,尽请期待！");
        info.add("功能未开放,尽请期待！");
        mHomeFragmetBodyBinding.marqueeView.startWithList(info);
        mHomeFragmetBodyBinding.marqueeView.setOnItemClickListener(new MarqueeView.OnItemClickListener() {
            @Override
            public void onItemClick(int position, TextView textView) {
                if (!GlobalUserDataStore.getInstance().isLogin()) {
                    ARouter.getInstance().build(ARouterPath.ActivityLogin).withBoolean(LoginActivity.IntentFromMine, true).navigation();
                    return;
                }
                ToastUtils.info("功能未开放");
            }
        });
    }
}
