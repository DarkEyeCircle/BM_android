package com.yueke.app.bmyp.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.askia.common.base.ARouterPath;
import com.askia.common.base.BaseFragment;
import com.askia.common.util.ToastUtils;
import com.askia.coremodel.GlobalUserDataStore;
import com.askia.coremodel.datamodel.http.parm.UserDictionary;
import com.askia.coremodel.viewmodel.MineFragmentViewModel;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.yueke.app.bmyp.R;
import com.yueke.app.bmyp.activitys.HeadImagePreviewActivity;
import com.yueke.app.bmyp.activitys.LoginActivity;
import com.yueke.app.bmyp.databinding.MineFragmentBinding;

@Route(path = ARouterPath.FragmentMine)
public class MineFragment extends BaseFragment {

    private MineFragmentBinding mMineFragmentBinding = null;
    private MineFragmentViewModel mMineFragmentViewModel = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mMineFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.mine_fragment, container, false);
        initViewModel();
        initData();
        initViews();
        return mMineFragmentBinding.getRoot();
    }

    private void initViewModel() {
        mMineFragmentViewModel = ViewModelProviders.of(this).get(MineFragmentViewModel.class);
    }

    private void initData() {
        mMineFragmentBinding.setHandlers(this);
        mMineFragmentBinding.setPageData(mMineFragmentViewModel.getPageData());
    }

    private void initViews() {
        if (GlobalUserDataStore.getInstance().isLogin()) {
            mMineFragmentBinding.imgvHead.setImageURI(mMineFragmentViewModel.getPageData().getImgHeadUrl());
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mMineFragmentViewModel.getPageData().update();
        if (!GlobalUserDataStore.getInstance().isLogin()) {
            mMineFragmentBinding.mineLayout.setVisibility(View.GONE);
            mMineFragmentBinding.loginLayout.setVisibility(View.VISIBLE);
        } else {
            updateMemberBG();
            mMineFragmentBinding.imgvHead.setImageURI(mMineFragmentViewModel.getPageData().getImgHeadUrl());
            mMineFragmentBinding.mineLayout.setVisibility(View.VISIBLE);
            mMineFragmentBinding.loginLayout.setVisibility(View.GONE);
        }
    }


    //消息模块
    public void jumpToChat(View view) {
        if (!GlobalUserDataStore.getInstance().isLogin()) {
            ARouter.getInstance().build(ARouterPath.ActivityLogin).withBoolean(LoginActivity.IntentFromMine, true).navigation();
            return;
        }
        ToastUtils.info("功能未开放");
    }

    //跳转个人信息
    public void jumpToMyInformation(View view) {
        if (!GlobalUserDataStore.getInstance().isLogin()) {
            ARouter.getInstance().build(ARouterPath.ActivityLogin).withBoolean(LoginActivity.IntentFromMine, true).navigation();
            return;
        }
        ARouter.getInstance().build(ARouterPath.ActivityMyInformation).navigation();
    }

    //跳转到升级会员
    public void jumpToUpgradeMembership(View view) {
        if (!GlobalUserDataStore.getInstance().isLogin()) {
            ARouter.getInstance().build(ARouterPath.ActivityLogin).withBoolean(LoginActivity.IntentFromMine, true).navigation();
            return;
        }
        ARouter.getInstance().build(ARouterPath.ActivityUpgradeMembership).navigation();
    }

    //跳转我的钱包
    public void jumpToMyWallet(View view) {
        if (!GlobalUserDataStore.getInstance().isLogin()) {
            ARouter.getInstance().build(ARouterPath.ActivityLogin).withBoolean(LoginActivity.IntentFromMine, true).navigation();
            return;
        }
        ARouter.getInstance().build(ARouterPath.ActivityMyWallet).navigation();
    }

    //跳转积分账户
    public void jumpToIntegralAccount(View view) {
        if (!GlobalUserDataStore.getInstance().isLogin()) {
            ARouter.getInstance().build(ARouterPath.ActivityLogin).withBoolean(LoginActivity.IntentFromMine, true).navigation();
            return;
        }
        ARouter.getInstance().build(ARouterPath.ActivityIntegralAccount).navigation();
    }

    //跳转账单
    public void jumpToBill(View view) {
        if (!GlobalUserDataStore.getInstance().isLogin()) {
            ARouter.getInstance().build(ARouterPath.ActivityLogin).withBoolean(LoginActivity.IntentFromMine, true).navigation();
            return;
        }
        ARouter.getInstance().build(ARouterPath.ActivityBill).navigation();
    }

    //跳转到地址管理
    public void jumpToAddrManager(View view) {
        if (!GlobalUserDataStore.getInstance().isLogin()) {
            ARouter.getInstance().build(ARouterPath.ActivityLogin).withBoolean(LoginActivity.IntentFromMine, true).navigation();
            return;
        }
        ARouter.getInstance().build(ARouterPath.ActivityAddressManager).navigation();
    }

    //跳转到设置中心
    public void jumpToSetting(View view) {
        if (!GlobalUserDataStore.getInstance().isLogin()) {
            ARouter.getInstance().build(ARouterPath.ActivityLogin).withBoolean(LoginActivity.IntentFromMine, true).navigation();
            return;
        }
        ARouter.getInstance().build(ARouterPath.ActivitySettingCenter).navigation();
    }

    //跳转到服务条款
    public void jumpToServiceTerms(View view) {
        ARouter.getInstance().build(ARouterPath.ActivityServiceTerms).navigation();
    }

    //跳转到代理
    public void jumpToAngency(View view) {
        if (!GlobalUserDataStore.getInstance().isLogin()) {
            ARouter.getInstance().build(ARouterPath.ActivityLogin).withBoolean(LoginActivity.IntentFromMine, true).navigation();
            return;
        }
        if (!GlobalUserDataStore.getInstance().isIdentityApprove()) {
            ARouter.getInstance().build(ARouterPath.ActivityIdentityApprove).navigation();
            return;
        }

        ARouter.getInstance().build(ARouterPath.ActivityAgency).navigation();

    }

    //跳转到推荐
    public void jumpToRecommended(View view) {
        if (!GlobalUserDataStore.getInstance().isLogin()) {
            ARouter.getInstance().build(ARouterPath.ActivityLogin).withBoolean(LoginActivity.IntentFromMine, true).navigation();
            return;
        }
        ARouter.getInstance().build(ARouterPath.ActivityRecommended).navigation();
    }

    //查看头像
    public void lookHead(View view) {
        if (!GlobalUserDataStore.getInstance().isLogin()) {
            ARouter.getInstance().build(ARouterPath.ActivityLogin).withBoolean(LoginActivity.IntentFromMine, true).navigation();
            return;
        }
        ARouter.getInstance().build(ARouterPath.ActivityHeadImagePreview)
                .withString(HeadImagePreviewActivity.INTENT_DATA_IMAGE_URL, GlobalUserDataStore.getInstance().getAvatar()).navigation();
    }

    //更新会员背景
    public void updateMemberBG() {
        switch (Integer.valueOf(GlobalUserDataStore.getInstance().getDictLevel())) {
            case UserDictionary.MEMBER_LEVEL_0:
                mMineFragmentBinding.memberLayout.setBackground(getResources().getDrawable(R.drawable.member_level_bg_0));
                break;
            case UserDictionary.MEMBER_LEVEL_1:
                mMineFragmentBinding.memberLayout.setBackground(getResources().getDrawable(R.drawable.member_level_bg_1));
                break;
            case UserDictionary.MEMBER_LEVEL_2:
                mMineFragmentBinding.memberLayout.setBackground(getResources().getDrawable(R.drawable.member_level_bg_2));
                break;
            case UserDictionary.MEMBER_LEVEL_3:
                mMineFragmentBinding.memberLayout.setBackground(getResources().getDrawable(R.drawable.member_level_bg_3));
                break;
            case UserDictionary.MEMBER_LEVEL_4:
                mMineFragmentBinding.memberLayout.setBackground(getResources().getDrawable(R.drawable.member_level_bg_4));
                break;
            case UserDictionary.MEMBER_LEVEL_5:
                mMineFragmentBinding.memberLayout.setBackground(getResources().getDrawable(R.drawable.member_level_bg_5));
                break;
            case UserDictionary.MEMBER_LEVEL_6:
                mMineFragmentBinding.memberLayout.setBackground(getResources().getDrawable(R.drawable.member_level_bg_6));
                break;
            default:
                mMineFragmentBinding.memberLayout.setBackground(getResources().getDrawable(R.drawable.radius_shape_bg));
                break;
        }
    }
}
