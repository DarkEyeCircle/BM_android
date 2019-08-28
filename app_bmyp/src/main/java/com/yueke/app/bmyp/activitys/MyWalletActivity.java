package com.yueke.app.bmyp.activitys;

import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.Gravity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.askia.common.base.ARouterPath;
import com.askia.common.base.BaseActivity;
import com.askia.common.base.BaseFragment;
import com.qmuiteam.qmui.widget.QMUITabSegment;
import com.yueke.app.bmyp.R;
import com.yueke.app.bmyp.databinding.MyWalletActivityBinding;

import java.util.ArrayList;
import java.util.List;

@Route(path = ARouterPath.ActivityMyWallet)
public class MyWalletActivity extends BaseActivity {

    private MyWalletActivityBinding mMyWalletActivityBinding;
    private MyPagerAdapter mPageAdapter = null;
    private List<BaseFragment> fragmentList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMyWalletActivityBinding = DataBindingUtil.setContentView(this, R.layout.my_wallet_activity);
        mMyWalletActivityBinding.topBar.setTitle(R.string.mine_wallet).getPaint().setFakeBoldText(true);
        mMyWalletActivityBinding.topBar.addLeftBackImageButton().setOnClickListener(v -> finish());
        //解决返回键不居中问题
        mMyWalletActivityBinding.topBar.setGravity(Gravity.CENTER_VERTICAL);
        initTabsAndPagers();
    }


    private void initTabsAndPagers() {
        mMyWalletActivityBinding.tabs.setDefaultNormalColor(getResources().getColor(R.color.lighter_gray));
        mMyWalletActivityBinding.tabs.setDefaultSelectedColor(getResources().getColor(R.color.black));
        Drawable drawable = getBaseContext().getResources().getDrawable(R.drawable.indicator_style);
        mMyWalletActivityBinding.tabs.setIndicatorDrawable(drawable);
        mMyWalletActivityBinding.tabs.setHasIndicator(true);
        mMyWalletActivityBinding.tabs.setIndicatorPosition(false);
        mMyWalletActivityBinding.tabs.setMode(QMUITabSegment.MODE_FIXED);
        mMyWalletActivityBinding.tabs.addTab(new QMUITabSegment.Tab(getString(R.string.baimeng_wallet)));
        mMyWalletActivityBinding.tabs.addTab(new QMUITabSegment.Tab(getString(R.string.merchants_wallet)));
        mMyWalletActivityBinding.tabs.addTab(new QMUITabSegment.Tab(getString(R.string.vip_wallet)));

        mMyWalletActivityBinding.contentViewPager.setOffscreenPageLimit(2);//设置缓存view 的个数（实际有3个，缓存2个+正在显示的1个）
        fragmentList.add((BaseFragment) ARouter.getInstance().build(ARouterPath.FragmentBMWallet).navigation());
        fragmentList.add((BaseFragment) ARouter.getInstance().build(ARouterPath.FragmentMerchantsWallet).navigation());
        fragmentList.add((BaseFragment) ARouter.getInstance().build(ARouterPath.FragmentMembersWallet).navigation());
        mPageAdapter = new MyPagerAdapter(getSupportFragmentManager(), fragmentList);
        mMyWalletActivityBinding.contentViewPager.setAdapter(mPageAdapter);
        mMyWalletActivityBinding.contentViewPager.setCurrentItem(0);
        mMyWalletActivityBinding.tabs.setupWithViewPager(mMyWalletActivityBinding.contentViewPager, false);
        mMyWalletActivityBinding.tabs.selectTab(mMyWalletActivityBinding.contentViewPager.getCurrentItem());
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        private List<BaseFragment> mFragmentList;

        public MyPagerAdapter(FragmentManager fm, List<BaseFragment> fragmentList) {
            super(fm);
            this.mFragmentList = fragmentList;
        }

        @Override
        public int getCount() {
            return this.mFragmentList.size();
        }

        @Override
        public Fragment getItem(int position) {
            return this.mFragmentList.get(position);
        }
    }

}
