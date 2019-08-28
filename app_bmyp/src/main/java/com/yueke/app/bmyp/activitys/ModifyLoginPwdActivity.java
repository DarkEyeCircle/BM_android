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
import com.askia.coremodel.viewmodel.ModifyLoginPwdViewModel;
import com.yueke.app.bmyp.DataFormat;
import com.yueke.app.bmyp.R;
import com.yueke.app.bmyp.databinding.ModifyLoginPwdActivityBinding;

@Route(path = ARouterPath.ActivityModifyLoginPwd)
public class ModifyLoginPwdActivity extends BaseActivity {

    private ModifyLoginPwdActivityBinding mModifyLoginPwdActivityBinding = null;
    private ModifyLoginPwdViewModel mModifyLoginPwdViewModel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDataBinding();
        initViewModel();
        initTopBar();
        initData();
    }

    private void initDataBinding() {
        mModifyLoginPwdActivityBinding = DataBindingUtil.setContentView(this, R.layout.modify_login_pwd_activity);
    }

    private void initViewModel() {
        mModifyLoginPwdViewModel = ViewModelProviders.of(this).get(ModifyLoginPwdViewModel.class);
        subscribeToModel(mModifyLoginPwdViewModel);
    }

    private void initTopBar() {
        mModifyLoginPwdActivityBinding.topBar.setTitle(R.string.change_login_password).getPaint().setFakeBoldText(true);
        //标题栏返回键监听
        mModifyLoginPwdActivityBinding.topBar.addLeftBackImageButton().setOnClickListener(v -> finish());
        //解决返回键不居中问题
        mModifyLoginPwdActivityBinding.topBar.setGravity(Gravity.CENTER_VERTICAL);
    }

    private void initData() {
        mModifyLoginPwdActivityBinding.setHandlers(this);
        mModifyLoginPwdActivityBinding.setPageData(mModifyLoginPwdViewModel.getPageData());
    }

    public void doConfirmModify(View view) {
        if (!mModifyLoginPwdViewModel.getPageData().getNewPassWord().get().equals(mModifyLoginPwdViewModel.getPageData().getConfirmPwd().get())) {
            ToastUtils.info("两次密码不正确");
            return;
        }
        if (mModifyLoginPwdViewModel.getPageData().getOldPassWord().get().equals(mModifyLoginPwdViewModel.getPageData().getNewPassWord().get())) {
            ToastUtils.info("新密码不能与原密码相同");
            return;
        }
        mModifyLoginPwdViewModel.modifyPwd();
    }

    public void userOldPwdEdittextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() < DataFormat.PassWordLeastLength) {
            mModifyLoginPwdActivityBinding.btConfirm.setEnabled(false);
            return;
        }
        String newPwd = mModifyLoginPwdViewModel.getPageData().getNewPassWord().get();
        String confirm = mModifyLoginPwdViewModel.getPageData().getConfirmPwd().get();
        if (newPwd.length() >= DataFormat.PassWordLeastLength && confirm.length() >= DataFormat.PassWordLeastLength) {
            mModifyLoginPwdActivityBinding.btConfirm.setEnabled(true);
        } else {
            mModifyLoginPwdActivityBinding.btConfirm.setEnabled(false);
        }
    }

    public void userNewPwdEdittextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() < DataFormat.PassWordLeastLength) {
            mModifyLoginPwdActivityBinding.btConfirm.setEnabled(false);
            return;
        }
        String oldPwd = mModifyLoginPwdViewModel.getPageData().getOldPassWord().get();
        String confirm = mModifyLoginPwdViewModel.getPageData().getConfirmPwd().get();
        if (oldPwd.length() >= DataFormat.PassWordLeastLength && confirm.length() >= DataFormat.PassWordLeastLength) {
            mModifyLoginPwdActivityBinding.btConfirm.setEnabled(true);
        } else {
            mModifyLoginPwdActivityBinding.btConfirm.setEnabled(false);
        }
    }

    public void userConfirmPwdEdittextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() < DataFormat.PassWordLeastLength) {
            mModifyLoginPwdActivityBinding.btConfirm.setEnabled(false);
            return;
        }
        String newPwd = mModifyLoginPwdViewModel.getPageData().getNewPassWord().get();
        String oldPwd = mModifyLoginPwdViewModel.getPageData().getOldPassWord().get();
        if (newPwd.length() >= DataFormat.PassWordLeastLength && oldPwd.length() >= DataFormat.PassWordLeastLength) {
            mModifyLoginPwdActivityBinding.btConfirm.setEnabled(true);
        } else {
            mModifyLoginPwdActivityBinding.btConfirm.setEnabled(false);
        }
    }

    private void subscribeToModel(final ModifyLoginPwdViewModel model) {
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
            mModifyLoginPwdViewModel.storeUserPwd();
            ToastUtils.info("密码修改成功");
            finish();

        });
    }

}
