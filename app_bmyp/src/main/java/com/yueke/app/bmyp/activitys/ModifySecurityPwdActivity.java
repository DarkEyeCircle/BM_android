package com.yueke.app.bmyp.activitys;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.askia.common.base.ARouterPath;
import com.askia.common.base.BaseActivity;
import com.askia.common.util.ToastUtils;
import com.askia.coremodel.viewmodel.ModifySecurityPwdViewModel;
import com.yueke.app.bmyp.DataFormat;
import com.yueke.app.bmyp.R;
import com.yueke.app.bmyp.databinding.ModifySecurityPwdActivityBinding;

@Route(path = ARouterPath.ActivityModifySecurityPwd)
public class ModifySecurityPwdActivity extends BaseActivity {

    private ModifySecurityPwdActivityBinding mSettingSecurityPwdViewModel = null;
    private ModifySecurityPwdViewModel mModifySecurityPwdViewModel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDataBinding();
        initViewModel();
        initTopBar();
        initData();
    }

    private void initDataBinding() {
        mSettingSecurityPwdViewModel = DataBindingUtil.setContentView(this, R.layout.modify_security_pwd_activity);
    }

    private void initViewModel() {
        mModifySecurityPwdViewModel = ViewModelProviders.of(this).get(ModifySecurityPwdViewModel.class);
        subscribeToModel(mModifySecurityPwdViewModel);
    }

    private void initTopBar() {
        mSettingSecurityPwdViewModel.topBar.setTitle(R.string.change_payment_password).getPaint().setFakeBoldText(true);
        //标题栏返回键监听
        mSettingSecurityPwdViewModel.topBar.addLeftBackImageButton().setOnClickListener(v -> finish());
        //解决返回键不居中问题
        mSettingSecurityPwdViewModel.topBar.setGravity(Gravity.CENTER_VERTICAL);
    }

    private void initData() {
        mSettingSecurityPwdViewModel.setHandlers(this);
        mSettingSecurityPwdViewModel.setPageData(mModifySecurityPwdViewModel.getPageData());
    }

    public void doConfirmModify(View view) {
        if (!mModifySecurityPwdViewModel.getPageData().getNewPassWord().get().equals(mModifySecurityPwdViewModel.getPageData().getConfirmPwd().get())) {
            ToastUtils.info("两次密码不正确");
            return;
        }
        if (mModifySecurityPwdViewModel.getPageData().getOldPassWord().get().equals(mModifySecurityPwdViewModel.getPageData().getNewPassWord().get())) {
            ToastUtils.info("新密码不能与原密码相同");
            return;
        }
        mModifySecurityPwdViewModel.modifyPwd();
    }

    public void userOldPwdEdittextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() < DataFormat.PassWordLeastLength) {
            mSettingSecurityPwdViewModel.btConfirm.setEnabled(false);
            return;
        }
        String newPwd = mModifySecurityPwdViewModel.getPageData().getNewPassWord().get();
        String confirm = mModifySecurityPwdViewModel.getPageData().getConfirmPwd().get();
        if (newPwd.length() >= DataFormat.PassWordLeastLength && confirm.length() >= DataFormat.PassWordLeastLength) {
            mSettingSecurityPwdViewModel.btConfirm.setEnabled(true);
        } else {
            mSettingSecurityPwdViewModel.btConfirm.setEnabled(false);
        }
    }

    public void userNewPwdEdittextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() < DataFormat.PassWordLeastLength) {
            mSettingSecurityPwdViewModel.btConfirm.setEnabled(false);
            return;
        }
        String oldPwd = mModifySecurityPwdViewModel.getPageData().getOldPassWord().get();
        String confirm = mModifySecurityPwdViewModel.getPageData().getConfirmPwd().get();
        if (oldPwd.length() >= DataFormat.PassWordLeastLength && confirm.length() >= DataFormat.PassWordLeastLength) {
            mSettingSecurityPwdViewModel.btConfirm.setEnabled(true);
        } else {
            mSettingSecurityPwdViewModel.btConfirm.setEnabled(false);
        }
    }

    public void userConfirmPwdEdittextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() < DataFormat.PassWordLeastLength) {
            mSettingSecurityPwdViewModel.btConfirm.setEnabled(false);
            return;
        }
        String newPwd = mModifySecurityPwdViewModel.getPageData().getNewPassWord().get();
        String oldPwd = mModifySecurityPwdViewModel.getPageData().getOldPassWord().get();
        if (newPwd.length() >= DataFormat.PassWordLeastLength && oldPwd.length() >= DataFormat.PassWordLeastLength) {
            mSettingSecurityPwdViewModel.btConfirm.setEnabled(true);
        } else {
            mSettingSecurityPwdViewModel.btConfirm.setEnabled(false);
        }
    }

    private void subscribeToModel(final ModifySecurityPwdViewModel model) {
        //观察数据变化来刷新UI
        model.getBaseResponseData().observe(this, responseData -> {
            closeLogadingDialog();
            if (responseData == null) {
                ToastUtils.showNetNoConnected();
                return;
            }
            if (responseData.isError()) {
                resolveResponseErrorCode(responseData.getCode(), responseData.getMsg());
                return;
            }
            mModifySecurityPwdViewModel.storeUserPwd();
            ToastUtils.info("密码修改成功");
            finish();

        });
    }

}
