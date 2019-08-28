package com.yueke.app.bmyp.activitys;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.askia.common.base.ARouterPath;
import com.askia.common.base.BaseActivity;
import com.askia.common.base.ViewManager;
import com.askia.common.util.ToastUtils;
import com.askia.coremodel.GlobalUserDataStore;
import com.askia.coremodel.datamodel.http.entities.ResponseCode;
import com.askia.coremodel.util.RegexUtils;
import com.askia.coremodel.viewmodel.SwitchAccountViewModel;
import com.yueke.app.bmyp.DataFormat;
import com.yueke.app.bmyp.R;
import com.yueke.app.bmyp.databinding.SwitchAccountActivityBinding;

@Route(path = ARouterPath.ActivitySwitchAccount)
public class SwitchAccountActivity extends BaseActivity {

    private SwitchAccountViewModel mSwitchAccountViewModel;
    private SwitchAccountActivityBinding mSwitchAccountActivityBinding = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDataBinding();
        initTopBar();
        initViewModel();
        initData();
        initViews();
    }

    private void initDataBinding() {
        mSwitchAccountActivityBinding = DataBindingUtil.setContentView(SwitchAccountActivity.this, R.layout.switch_account_activity);
    }

    private void initTopBar() {
        mSwitchAccountActivityBinding.topBar.setTitle(R.string.switch_account).getPaint().setFakeBoldText(true);
        //标题栏返回键监听
        mSwitchAccountActivityBinding.topBar.addLeftBackImageButton().setOnClickListener(v -> finish());
        //解决返回键不居中问题
        mSwitchAccountActivityBinding.topBar.setGravity(Gravity.CENTER_VERTICAL);
    }

    private void initViewModel() {
        mSwitchAccountViewModel = ViewModelProviders.of(SwitchAccountActivity.this).get(SwitchAccountViewModel.class);
        subscribeToModel(mSwitchAccountViewModel);
    }

    private void initData() {
        mSwitchAccountActivityBinding.setHandlers(SwitchAccountActivity.this);
        mSwitchAccountActivityBinding.setPageData(mSwitchAccountViewModel.getPageData());
    }

    private void initViews() {
        mSwitchAccountActivityBinding.pwdInput.setInputType(0x81); //设置不可见
    }

    //订阅数据变化来刷新UI
    private void subscribeToModel(final SwitchAccountViewModel model) {
        //观察数据变化来刷新UI
        model.getLiveLoginData().observe(this, user -> {
            closeLogadingDialog();
            if (user == null) {
                ToastUtils.showNetNoConnected();
                return;
            }
            if (user.isError()) {
                if (user.getCode() == ResponseCode.OPERATION_FAILED) {
                    mSwitchAccountViewModel.getPageData().getPassWord().set("");
                }
                ToastUtils.info(user.getMsg(), Toast.LENGTH_SHORT);
                return;
            }
            //缓存数据
            GlobalUserDataStore.getInstance().updateUserData(user);
            ViewManager.getInstance().finishAllOtherActivitys(this);
            ARouter.getInstance().build(ARouterPath.ActivityMain).navigation();
            finish();
        });
    }

    //切换账号
    public void doSwitchAccount(View view) {
        String mobileNum = mSwitchAccountViewModel.getPageData().getPhoneNum().get().replace(" ", "");
        if (mobileNum.equals(GlobalUserDataStore.getInstance().getMobile())) {
            ToastUtils.info("此账号已在当前设备登陆");
            return;
        }
        if (!RegexUtils.isMobileExact(mobileNum)) {
            ToastUtils.info(getResources().getString(R.string.input_correct_phone_num), Toast.LENGTH_SHORT);
            return;
        }
        mSwitchAccountActivityBinding.pwdInput.setInputType(0x81);
        mSwitchAccountActivityBinding.btShowPwd.setText("显示");
        showLogadingDialog(getResources().getString(R.string.logining));
        mSwitchAccountViewModel.doLogin();
    }

    //显示密码
    public void showPwd(View view) {
        //设置密码可见
        if (mSwitchAccountActivityBinding.pwdInput.getInputType() == 0x81) {
            mSwitchAccountActivityBinding.pwdInput.setInputType(0x90);
            mSwitchAccountActivityBinding.btShowPwd.setText("隐藏");
        } else { //不可见
            mSwitchAccountActivityBinding.pwdInput.setInputType(0x81);
            mSwitchAccountActivityBinding.btShowPwd.setText("显示");
        }
        mSwitchAccountActivityBinding.pwdInput.requestFocus();
        mSwitchAccountActivityBinding.pwdInput.setSelection(mSwitchAccountViewModel.getPageData().getPassWord().get().length());
    }

    //手机号码输入框内容监听
    public void mobileNumEdittextChanged(CharSequence s, int start, int before, int count) {
        if (TextUtils.isEmpty(s)) {
            mSwitchAccountViewModel.getPageData().getPassWord().set("");
            return;
        }
        if (s.toString().length() != DataFormat.MobileNum) {
            mSwitchAccountActivityBinding.btConfirmSwitch.setEnabled(false);
            return;
        }
        String userPwd = mSwitchAccountViewModel.getPageData().getPassWord().get();
        if (userPwd.length() >= DataFormat.PassWordLeastLength) {
            mSwitchAccountActivityBinding.btConfirmSwitch.setEnabled(true);
            return;
        }

    }

    //密码输入框内容监听
    public void userPwdEdittextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() < 6) {
            mSwitchAccountActivityBinding.btConfirmSwitch.setEnabled(false);
            return;
        }
        String userNum = mSwitchAccountViewModel.getPageData().getPhoneNum().get();
        if (userNum.length() == 13) {
            mSwitchAccountActivityBinding.btConfirmSwitch.setEnabled(true);
            return;
        }
    }

}
