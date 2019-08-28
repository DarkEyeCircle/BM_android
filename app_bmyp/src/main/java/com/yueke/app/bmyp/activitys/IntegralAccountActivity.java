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
import com.yueke.app.bmyp.databinding.IntegralAccountActivityBinding;
import com.yueke.app.bmyp.fragments.TotalMallFragment;

import java.util.ArrayList;
import java.util.List;

@Route(path = ARouterPath.ActivityIntegralAccount)
public class IntegralAccountActivity extends BaseActivity {

    private IntegralAccountActivityBinding mIntegralAccountActivityBinding;
    private MyPagerAdapter mPageAdapter = null;
    private List<BaseFragment> fragmentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIntegralAccountActivityBinding = DataBindingUtil.setContentView(this, R.layout.integral_account_activity);
        mIntegralAccountActivityBinding.topBar.setTitle(R.string.integral_account).getPaint().setFakeBoldText(true);
        mIntegralAccountActivityBinding.topBar.addLeftBackImageButton().setOnClickListener(v -> finish());
        //解决返回键不居中问题
        mIntegralAccountActivityBinding.topBar.setGravity(Gravity.CENTER_VERTICAL);
        initTabsAndPagers();
    }


    private void initTabsAndPagers() {
        mIntegralAccountActivityBinding.tabs.setDefaultNormalColor(getResources().getColor(R.color.lighter_gray));
        mIntegralAccountActivityBinding.tabs.setDefaultSelectedColor(getResources().getColor(R.color.black));
        Drawable drawable = getBaseContext().getResources().getDrawable(R.drawable.indicator_style);
        mIntegralAccountActivityBinding.tabs.setIndicatorDrawable(drawable);
        mIntegralAccountActivityBinding.tabs.setHasIndicator(true);
        mIntegralAccountActivityBinding.tabs.setIndicatorPosition(false);
        mIntegralAccountActivityBinding.tabs.setMode(QMUITabSegment.MODE_FIXED);
        mIntegralAccountActivityBinding.tabs.addTab(new QMUITabSegment.Tab(getString(R.string.mall_total)));
        mIntegralAccountActivityBinding.tabs.addTab(new QMUITabSegment.Tab(getString(R.string.wait_active_total)));

        mIntegralAccountActivityBinding.contentViewPager.setOffscreenPageLimit(1);
        fragmentList.add((BaseFragment) ARouter.getInstance().build(ARouterPath.FragmentTotalMall).navigation());
        fragmentList.add((BaseFragment) ARouter.getInstance().build(ARouterPath.FragmentTotalWaitActive).navigation());
        mPageAdapter = new MyPagerAdapter(getSupportFragmentManager(), fragmentList);
        mIntegralAccountActivityBinding.contentViewPager.setAdapter(mPageAdapter);
        mIntegralAccountActivityBinding.contentViewPager.setCurrentItem(0);
        mIntegralAccountActivityBinding.tabs.setupWithViewPager(mIntegralAccountActivityBinding.contentViewPager, false);
        mIntegralAccountActivityBinding.tabs.selectTab(mIntegralAccountActivityBinding.contentViewPager.getCurrentItem());
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

    //更新商城积分
    public void updateMallTotal() {
        if (fragmentList != null) {
            BaseFragment baseFragment = fragmentList.get(0);
            if (baseFragment instanceof TotalMallFragment) {
                ((TotalMallFragment) baseFragment).updateData();
            }
        }
    }
}
