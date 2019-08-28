package com.yueke.app.bmyp.activitys;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.view.Gravity;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.askia.common.base.ARouterPath;
import com.askia.common.base.BaseActivity;
import com.askia.coremodel.GlobalUserDataStore;
import com.askia.coremodel.viewmodel.AccountManagerViewModel;
import com.yueke.app.bmyp.R;
import com.yueke.app.bmyp.databinding.AccountManagerActivityBinding;

@Route(path = ARouterPath.ActivityAccountManager)
public class AccountManagerActivity extends BaseActivity {

    private AccountManagerActivityBinding mAccountManagerActivityBinding;
    private AccountManagerViewModel mAccountManagerViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDataBinding();
        initTopBar();
        initViewModel();
        initData();
    }

    private void initDataBinding() {
        mAccountManagerActivityBinding = DataBindingUtil.setContentView(AccountManagerActivity.this, R.layout.account_manager_activity);
    }

    private void initTopBar() {
        mAccountManagerActivityBinding.topBar.setTitle(R.string.account_manager).getPaint().setFakeBoldText(true);
        mAccountManagerActivityBinding.topBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //解决返回键不居中问题
        mAccountManagerActivityBinding.topBar.setGravity(Gravity.CENTER_VERTICAL);
    }

    private void initViewModel() {
        mAccountManagerViewModel = ViewModelProviders.of(AccountManagerActivity.this).get(AccountManagerViewModel.class);

    }

    private void initData() {
        mAccountManagerActivityBinding.setHandlers(this);
        mAccountManagerActivityBinding.setPageData(mAccountManagerViewModel.getPageData());
    }

    //更改手机号码
    public void jumpToModifyPhoneNum(View view) {
        if (!GlobalUserDataStore.getInstance().isIdentityApprove()) {
            ARouter.getInstance().build(ARouterPath.ActivityIdentityApprove).navigation();
            return;
        }
        ARouter.getInstance().build(ARouterPath.ActivityChangeMobile).navigation();
    }

    //绑定微信账号
    public void jumpToUnBindWechat(View view) {
        return;
    }

    //绑定支付宝账号
    public void jumpToUnBindAlipay(View view) {
        if (!GlobalUserDataStore.getInstance().isIdentityApprove()) {
            ARouter.getInstance().build(ARouterPath.ActivityIdentityApprove).navigation();
            return;
        }

        ARouter.getInstance().build(ARouterPath.ActivityBindingAlipayAccount).navigation();
    }

    //银行卡
    public void jumpToEditorBankCard(View view) {
        if (!GlobalUserDataStore.getInstance().isIdentityApprove()) {
            ARouter.getInstance().build(ARouterPath.ActivityIdentityApprove).navigation();
            return;
        }
        ARouter.getInstance().build(ARouterPath.ActivityBankCardManager).navigation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAccountManagerViewModel.update();
    }

}
