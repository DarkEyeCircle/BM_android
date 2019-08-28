package com.yueke.app.bmyp.activitys;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.apkfuns.logutils.LogUtils;
import com.askia.common.base.ARouterPath;
import com.askia.common.base.BaseActivity;
import com.askia.common.util.InputTools;
import com.askia.common.util.ToastUtils;
import com.askia.coremodel.GlobalUserDataStore;
import com.askia.coremodel.datamodel.http.entities.AgencyData;
import com.askia.coremodel.datamodel.http.entities.BaseResponseData;
import com.askia.coremodel.datamodel.http.parm.UserDictionary;
import com.askia.coremodel.viewmodel.AgencyViewModel;
import com.askia.keyboard.OnPasswordInputFinish;
import com.askia.keyboard.widget.PopEnterPassword;
import com.yueke.app.bmyp.R;
import com.yueke.app.bmyp.databinding.AgencyActivityBinding;
import com.yueke.app.bmyp.fragments.ApplyAgencyStateFragment;

@Route(path = ARouterPath.ActivityAgency)
public class AgencyActivity extends BaseActivity {

    private AgencyActivityBinding mAgencyActivityBinding = null;
    private AgencyViewModel mAgencyViewModel = null;
    private ApplyAgencyStateFragment applyAgencyStateFragment;
    private static final int RequestCodeApplyAgencySuccess = 112;
    private PopEnterPassword popEnterPassword = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDataBinding();
        initTopBar();
        initViews();
        initViewModel();
        initData();
    }

    private void initDataBinding() {
        mAgencyActivityBinding = DataBindingUtil.setContentView(this, R.layout.agency_activity);
    }

    private void initTopBar() {
        mAgencyActivityBinding.topBar.setTitle(R.string.agency).getPaint().setFakeBoldText(true);
        mAgencyActivityBinding.topBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //解决返回键不居中问题
        mAgencyActivityBinding.topBar.setGravity(Gravity.CENTER_VERTICAL);
    }

    private void initViews() {
        mAgencyActivityBinding.noCardLayout.setVisibility(View.GONE);
        mAgencyActivityBinding.mainLayout.setVisibility(View.GONE);
    }

    private void initViewModel() {
        mAgencyViewModel = ViewModelProviders.of(AgencyActivity.this).get(AgencyViewModel.class);
        subscribeToModel(mAgencyViewModel);
    }

    private void initData() {
        mAgencyActivityBinding.setHandlers(this);
        mAgencyActivityBinding.setPageData(mAgencyViewModel.getPageData());
    }

    //申请代理
    public void jumpToApplyAgency(View view) {
        ARouter.getInstance().build(ARouterPath.ActivityApplyAgency).navigation(this, RequestCodeApplyAgencySuccess);
    }

    //领取奖励
    public void getAward(View view) {
        if (!GlobalUserDataStore.getInstance().isHaveSecurityPwd()) {
            ARouter.getInstance().build(ARouterPath.ActivitySettingSecurityPwd).navigation();
            return;
        }
        showPayKeyBoard();
    }

    //显示支付键盘
    public void showPayKeyBoard() {
        InputTools.HideKeyboard(mAgencyActivityBinding.mainLayout);
        if (null == popEnterPassword) {
            popEnterPassword = new PopEnterPassword(this, new OnPasswordInputFinish() {
                @Override
                public void inputFinish(String password) {
                    popEnterPassword.close();
                    showLogadingDialog();
                    mAgencyViewModel.checkSecurityPwd(password);
                }
            });
        }
        popEnterPassword.show("0", null, findViewById(R.id.layoutContent));
    }

    private void subscribeToModel(final AgencyViewModel model) {
        //验证安全密码
        model.getCheckSecurityPwdLiveData().observe(this, new Observer<BaseResponseData>() {
            @Override
            public void onChanged(@Nullable BaseResponseData responseData) {
                if (responseData == null) {
                    closeLogadingDialog();
                    ToastUtils.showNetNoConnected();
                    return;
                }
                if (responseData.isError()) {
                    closeLogadingDialog();
                    resolveResponseErrorCode(responseData.getCode(), responseData.getMsg());
                    return;
                }
                mAgencyViewModel.getAward();
            }
        });
        //代理信息
        model.getAgencyInfoLiveData().observe(this, new Observer<AgencyData>() {
            @Override
            public void onChanged(@Nullable AgencyData responseData) {
                closeLogadingDialog();
                if (responseData == null) {
                    mAgencyActivityBinding.emptyView.show(false, getResources().getString(R.string.emptyView_mode_desc_fail_desc), getResources().getString(R.string.emptyView_mode_desc_fail_desc), getResources().getString(R.string.emptyView_mode_desc_retry), retryAgainListener);
                    return;
                }
                if (responseData.isError()) {
                    resolveResponseErrorCode(responseData.getCode(), responseData.getMsg());
                    return;
                }
                //未申请代理
                if (null == responseData.getObj() || Integer.valueOf(responseData.getObj().getDictStatus()) == UserDictionary.APPLY_AGENCY_NOT) {
                    mAgencyActivityBinding.emptyView.show(false);
                    mAgencyActivityBinding.noCardLayout.setVisibility(View.VISIBLE);
                    return;
                }
                //已申请代理
                if (Integer.valueOf(responseData.getObj().getDictStatus()) != UserDictionary.APPLY_AGENCY_SUCCESS) {
                    mAgencyActivityBinding.emptyView.show(false);
                    applyAgencyStateFragment = new ApplyAgencyStateFragment();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(ApplyAgencyStateFragment.INTENT_DATA, responseData.getObj());
                    applyAgencyStateFragment.setArguments(bundle);
                    addFragment(applyAgencyStateFragment, R.id.emptyView);
                    return;
                }

                mAgencyActivityBinding.emptyView.show(false);
                mAgencyActivityBinding.mainLayout.setVisibility(View.VISIBLE);
                mAgencyViewModel.update(responseData.getObj());
                if (Float.valueOf(mAgencyViewModel.getPageData().getIntegral().get()) > 0) {
                    mAgencyActivityBinding.btAward.setEnabled(true);
                } else {
                    mAgencyActivityBinding.btAward.setEnabled(false);
                }
            }
        });
        //领取积分奖励
        model.getAwardLiveData().observe(this, new Observer<BaseResponseData>() {
            @Override
            public void onChanged(@Nullable BaseResponseData responseData) {
                closeLogadingDialog();
                if (responseData == null) {
                    ToastUtils.showNetNoConnected();
                    return;
                }
                if (responseData.isError()) {
                    resolveResponseErrorCode(responseData.getCode(), responseData.getMsg());
                    return;
                }
                ToastUtils.info("领取成功");
                mAgencyActivityBinding.mainLayout.setVisibility(View.GONE);
                mAgencyActivityBinding.noCardLayout.setVisibility(View.GONE);
                mAgencyActivityBinding.emptyView.show(true);
                mAgencyActivityBinding.emptyView.setTitleText("正在加载");
                mAgencyViewModel.getAgencyInfo();
            }
        });
    }

    private View.OnClickListener retryAgainListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mAgencyActivityBinding.mainLayout.setVisibility(View.GONE);
            mAgencyActivityBinding.noCardLayout.setVisibility(View.GONE);
            mAgencyActivityBinding.emptyView.show(true);
            mAgencyActivityBinding.emptyView.setTitleText("正在加载");
            mAgencyViewModel.getAgencyInfo();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RequestCodeApplyAgencySuccess && resultCode == RESULT_OK) {
            LogUtils.d("onActivityResult");
            mAgencyActivityBinding.mainLayout.setVisibility(View.GONE);
            mAgencyActivityBinding.noCardLayout.setVisibility(View.GONE);
            mAgencyActivityBinding.emptyView.show(true);
            mAgencyActivityBinding.emptyView.setTitleText("正在加载");
            mAgencyViewModel.getAgencyInfo();
        }
    }

    public void applyAgencyAgain() {
        removeFragment(applyAgencyStateFragment);
        mAgencyActivityBinding.mainLayout.setVisibility(View.GONE);
        mAgencyActivityBinding.noCardLayout.setVisibility(View.GONE);
        mAgencyActivityBinding.emptyView.show(true);
        mAgencyActivityBinding.emptyView.setTitleText("正在加载");
        mAgencyViewModel.getAgencyInfo();
        applyAgencyStateFragment = null;
    }

}
