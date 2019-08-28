package com.yueke.app.bmyp.activitys;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.askia.common.base.ARouterPath;
import com.askia.common.base.BaseActivity;
import com.askia.coremodel.GlobalUserDataStore;
import com.yueke.app.bmyp.R;
import com.yueke.app.bmyp.databinding.PwdManagerActivityBinding;

@Route(path = ARouterPath.ActivityPwdManager)
public class PwdManagerActivity extends BaseActivity {

    private PwdManagerActivityBinding mPwdManagerActivityBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDataBinding();
        initTopBar();
        initData();
    }

    private void initDataBinding() {
        mPwdManagerActivityBinding = DataBindingUtil.setContentView(PwdManagerActivity.this, R.layout.pwd_manager_activity);
    }

    private void initTopBar() {
        mPwdManagerActivityBinding.topBar.setTitle(R.string.pwd_manager).getPaint().setFakeBoldText(true);
        mPwdManagerActivityBinding.topBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //解决返回键不居中问题
        mPwdManagerActivityBinding.topBar.setGravity(Gravity.CENTER_VERTICAL);
    }

    private void initData() {
        mPwdManagerActivityBinding.setHandlers(this);
    }

    //修改登陆密码
    public void changeLoginPassword(View view) {
        ARouter.getInstance().build(ARouterPath.ActivityModifyLoginPwd).navigation();
    }

    //找回登陆密码
    public void retrieveLoginPassword(View view) {
        ARouter.getInstance().build(ARouterPath.ActivityRetrieveLoginPwd).navigation();
    }

    //修改支付密码
    public void changePaymentPassword(View view) {
        if (!GlobalUserDataStore.getInstance().isHaveSecurityPwd()) {
            ARouter.getInstance().build(ARouterPath.ActivitySettingSecurityPwd).navigation();
            return;
        }
        ARouter.getInstance().build(ARouterPath.ActivityModifySecurityPwd).navigation();
    }

    //找回支付密码
    public void retrievePaymentPassword(View view) {
        if (!GlobalUserDataStore.getInstance().isHaveSecurityPwd()) {
            ARouter.getInstance().build(ARouterPath.ActivitySettingSecurityPwd).navigation();
            return;
        }
        ARouter.getInstance().build(ARouterPath.ActivityRetrievePayPwd).navigation();
    }

}
