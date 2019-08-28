package com.yueke.app.bmyp.activitys;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.askia.common.base.ARouterPath;
import com.askia.common.base.BaseActivity;
import com.askia.common.base.BaseFragment;
import com.askia.common.base.ViewManager;
import com.askia.common.util.ToastUtils;
import com.askia.common.util.Utils;
import com.askia.coremodel.GlobalUserDataStore;
import com.askia.coremodel.viewmodel.MainViewModel;
import com.qmuiteam.qmui.util.QMUIResHelper;
import com.qmuiteam.qmui.widget.QMUITabSegment;
import com.tencent.bugly.beta.Beta;
import com.yueke.app.bmyp.R;
import com.yueke.app.bmyp.databinding.MainActivityBinding;

@Route(path = ARouterPath.ActivityMain)
public class MainActivity extends BaseActivity {

    public static final String Intent_Auto_Login = "Intent_Auto_Login";
    private long exitTime = 0;
    private MainActivityBinding mMainActivityBinding;
    private MainViewModel mMainViewModel;
    /*Fragments*/
    private BaseFragment mHomeFragment = null;
    private BaseFragment mNetShoppingFragment = null;
    private BaseFragment mFunShoppingFragment = null;
    private BaseFragment mMeFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainActivityBinding = DataBindingUtil.setContentView(this, R.layout.main_activity);
        mMainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        subscribeToModel(mMainViewModel);
        initTabs();
        switchFragment(0);
        checkLogin();
        Beta.checkUpgrade(false, false);
    }

    private void initTabs() {
        int normalColor = QMUIResHelper.getAttrColor(MainActivity.this, R.attr.qmui_config_color_gray_6);
        int selectColor = QMUIResHelper.getAttrColor(MainActivity.this, R.attr.qmui_config_color_blue);
        mMainActivityBinding.tabs.setDefaultNormalColor(normalColor);
        mMainActivityBinding.tabs.setDefaultSelectedColor(selectColor);
        mMainActivityBinding.tabs.setHasIndicator(false);
        QMUITabSegment.Tab component = new QMUITabSegment.Tab(
                ContextCompat.getDrawable(MainActivity.this, R.drawable.icon_home),
                ContextCompat.getDrawable(MainActivity.this, R.drawable.icon_home_select),
                "首页", false
        );

        QMUITabSegment.Tab util = new QMUITabSegment.Tab(
                ContextCompat.getDrawable(MainActivity.this, R.drawable.icon_shopping_net),
                ContextCompat.getDrawable(MainActivity.this, R.drawable.icon_shopping_net_select),
                "网购", false
        );
        QMUITabSegment.Tab lab = new QMUITabSegment.Tab(
                ContextCompat.getDrawable(MainActivity.this, R.drawable.icon_shopping_fun),
                ContextCompat.getDrawable(MainActivity.this, R.drawable.icon_shopping_fun_select),
                "乐购", false
        );
        QMUITabSegment.Tab me = new QMUITabSegment.Tab(
                ContextCompat.getDrawable(MainActivity.this, R.drawable.icon_me),
                ContextCompat.getDrawable(MainActivity.this, R.drawable.icon_me_select),
                "我的", false
        );
        mMainActivityBinding.tabs
                .addTab(component)
                .addTab(util)
                .addTab(lab)
                .addTab(me);
        mMainActivityBinding.tabs.selectTab(0);
        mMainActivityBinding.tabs.notifyDataChanged();
        mMainActivityBinding.tabs.addOnTabSelectedListener(new QMUITabSegment.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int index) {
                switchFragment(index);
            }

            @Override
            public void onTabUnselected(int index) {

            }

            @Override
            public void onTabReselected(int index) {

            }

            @Override
            public void onDoubleTap(int index) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void switchFragment(int index) {
        hideFragment(mHomeFragment, mNetShoppingFragment, mFunShoppingFragment, mMeFragment);
        switch (index) {
            case 0: //首页
                mMainActivityBinding.mainLayout.setBackground(getResources().getDrawable(R.drawable.color_shape));
                if (mHomeFragment == null) {
                    mHomeFragment = (BaseFragment) ARouter.getInstance().build(ARouterPath.FragmentHome).navigation();
                    addFragment(mHomeFragment, mMainActivityBinding.fragmentContainer.getId());
                } else {
                    showFragment(mHomeFragment);
                }
                break;
            case 1: //网购
                mMainActivityBinding.mainLayout.setBackgroundColor(getResources().getColor(R.color.white));
                if (mNetShoppingFragment == null) {
                    mNetShoppingFragment = (BaseFragment) ARouter.getInstance().build(ARouterPath.FragmentNetShopping).navigation();
                    addFragment(mNetShoppingFragment, R.id.fragment_container);
                } else {
                    showFragment(mNetShoppingFragment);
                }
                break;
            case 2: //乐购
                mMainActivityBinding.mainLayout.setBackgroundColor(getResources().getColor(R.color.white));
                if (mFunShoppingFragment == null) {
                    mFunShoppingFragment = (BaseFragment) ARouter.getInstance().build(ARouterPath.FragmentFunShopping).navigation();
                    addFragment(mFunShoppingFragment, R.id.fragment_container);
                } else {
                    showFragment(mFunShoppingFragment);
                }
                break;
            case 3: //我的
                mMainActivityBinding.mainLayout.setBackgroundColor(getResources().getColor(R.color.white));
                if (mMeFragment == null) {
                    mMeFragment = (BaseFragment) ARouter.getInstance().build(ARouterPath.FragmentMine).navigation();
                    addFragment(mMeFragment, R.id.fragment_container);
                } else {
                    showFragment(mMeFragment);
                }
                break;
        }
    }

    public void checkLogin() {
        if (getIntent().getBooleanExtra(Intent_Auto_Login, false) && !GlobalUserDataStore.getInstance().isLogin()) {
            showLogadingDialog();
            mMainViewModel.doLogin();
        }
    }

    //订阅数据变化来刷新UI
    private void subscribeToModel(final MainViewModel model) {
        //观察数据变化来刷新UI
        model.getLiveLoginData().observe(this, user -> {
            closeLogadingDialog();
            if (user == null) {
                ToastUtils.showNetNoConnected();
                return;
            }
            if (user.isError()) {
                return;
            }
            //缓存数据
            GlobalUserDataStore.getInstance().updateUserData(user);
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                ViewManager.getInstance().exitApp(Utils.getContext());
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
