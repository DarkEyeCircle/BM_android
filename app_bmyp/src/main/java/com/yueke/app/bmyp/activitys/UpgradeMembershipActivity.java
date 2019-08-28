package com.yueke.app.bmyp.activitys;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.askia.common.base.ARouterPath;
import com.askia.common.base.BaseActivity;
import com.askia.coremodel.GlobalUserDataStore;
import com.askia.coremodel.datamodel.http.entities.MemberUpdateBagsData;
import com.askia.coremodel.datamodel.http.parm.UserDictionary;
import com.askia.coremodel.util.RxUtils;
import com.askia.coremodel.viewmodel.UrgradeMembershipViewModel;
import com.yueke.app.bmyp.R;
import com.yueke.app.bmyp.databinding.UpgradeMembershipActivityBinding;

import io.reactivex.functions.Consumer;

@Route(path = ARouterPath.ActivityUpgradeMembership)
public class UpgradeMembershipActivity extends BaseActivity {

    private UpgradeMembershipActivityBinding mUpgradeMembershipActivityBinding;
    private UrgradeMembershipViewModel mUrgradeMembershipViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDataBinding();
        initViewModel();
        initTopBar();
        initViews();
        initData();
    }


    private void initDataBinding() {
        mUpgradeMembershipActivityBinding = DataBindingUtil.setContentView(this, R.layout.upgrade_membership_activity);
    }

    private void initTopBar() {
        mUpgradeMembershipActivityBinding.topBar.setTitle("升级会员").getPaint().setFakeBoldText(true);
        mUpgradeMembershipActivityBinding.topBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //解决返回键不居中问题
        mUpgradeMembershipActivityBinding.topBar.setGravity(Gravity.CENTER_VERTICAL);
    }

    private void initViewModel() {
        mUrgradeMembershipViewModel = ViewModelProviders.of(this).get(UrgradeMembershipViewModel.class);
        subscribeToModel(mUrgradeMembershipViewModel);
    }

    private void initViews() {
        mUpgradeMembershipActivityBinding.mainLayout.setVisibility(View.GONE);
    }

    private void initData() {
        mUpgradeMembershipActivityBinding.setHandlers(this);
        mUpgradeMembershipActivityBinding.setPageData(mUrgradeMembershipViewModel.getPageData());
    }

    @Override
    protected void onStart() {
        super.onStart();
        mUpgradeMembershipActivityBinding.mainLayout.setVisibility(View.GONE);
        mUpgradeMembershipActivityBinding.emptyView.show(true);
        mUpgradeMembershipActivityBinding.emptyView.setTitleText("正在加载");
        mUrgradeMembershipViewModel.getMemberBags();
    }

    private void subscribeToModel(final UrgradeMembershipViewModel model) {
        model.getMemberBagsLiveData().observe(this, new Observer<MemberUpdateBagsData>() {
            @Override
            public void onChanged(@Nullable MemberUpdateBagsData responseData) {
                if (responseData == null) {
                    mUpgradeMembershipActivityBinding.emptyView.show(false, getResources().getString(R.string.emptyView_mode_desc_fail_desc), getResources().getString(R.string.emptyView_mode_desc_fail_desc), getResources().getString(R.string.emptyView_mode_desc_retry), retryAgainListener);
                    return;
                }
                if (responseData.isError()) {
                    mUpgradeMembershipActivityBinding.emptyView.show(false, getResources().getString(R.string.emptyView_mode_desc_fail_title), null, getResources().getString(R.string.emptyView_mode_desc_retry), retryAgainListener);
                    return;
                }
                if (responseData.getList() == null || responseData.getList().size() <= 0) {
                    mUpgradeMembershipActivityBinding.emptyView.show(false, getResources().getString(R.string.emptyView_mode_desc_fail_title), null, getResources().getString(R.string.emptyView_mode_desc_retry), retryAgainListener);
                    return;
                }
                mUrgradeMembershipViewModel.init(responseData);
                if (responseData.getObj() == UserDictionary.MEMBER_LEVEL_6) {
                    mUpgradeMembershipActivityBinding.btBuy.setEnabled(false);
                } else {
                    mUpgradeMembershipActivityBinding.btBuy.setEnabled(true);
                }
                mUpgradeMembershipActivityBinding.imgvHead.setImageURI(mUrgradeMembershipViewModel.getPageData().getImgvHeadUrl());
                mUpgradeMembershipActivityBinding.imgvMemberCover.setImageURI(mUrgradeMembershipViewModel.getPageData().getImgUrl());
                mUpgradeMembershipActivityBinding.emptyView.show(false);
                mUpgradeMembershipActivityBinding.mainLayout.setVisibility(View.VISIBLE);
            }
        });
    }


    public void buyMember(View view) {
        ARouter.getInstance().build(ARouterPath.ActivityBuyMember)
                .withString(BuyMemberActivity.INTENT_DATA_VALUE, mUrgradeMembershipViewModel.getPageData().getPrice().get())
                .withString(BuyMemberActivity.INTENT_DATA_URL, mUrgradeMembershipViewModel.getPageData().getImgUrl())
                .withString(BuyMemberActivity.INTENT_DATA_ID, mUrgradeMembershipViewModel.getPageData().getId())
                .navigation();
    }

    private View.OnClickListener retryAgainListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mUpgradeMembershipActivityBinding.mainLayout.setVisibility(View.GONE);
            mUpgradeMembershipActivityBinding.emptyView.show(true);
            mUpgradeMembershipActivityBinding.emptyView.setTitleText("正在加载");
            mUrgradeMembershipViewModel.getMemberBags();
        }
    };

}
