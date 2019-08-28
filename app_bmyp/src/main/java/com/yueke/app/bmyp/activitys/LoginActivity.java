package com.yueke.app.bmyp.activitys;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.apkfuns.logutils.LogUtils;
import com.askia.common.base.ARouterPath;
import com.askia.common.base.BaseActivity;
import com.askia.common.util.ToastUtils;
import com.askia.coremodel.GlobalUserDataStore;
import com.askia.coremodel.datamodel.database.db.UserLoginData;
import com.askia.coremodel.datamodel.database.repository.DBRepository;
import com.askia.coremodel.datamodel.http.entities.ResponseCode;
import com.askia.coremodel.util.RegexUtils;
import com.askia.coremodel.viewmodel.LoginViewModel;
import com.yueke.app.bmyp.DataFormat;
import com.yueke.app.bmyp.R;
import com.yueke.app.bmyp.databinding.LoginActivityBinding;

@Route(path = ARouterPath.ActivityLogin)
public class LoginActivity extends BaseActivity {

    public static final String IntentFromMine = "IntentFromMine";
    public static final int IntentRegisterSuccessCode = 0x110;
    public static final int IntentFindPwdSuccessCode = 0x111;

    private LoginActivityBinding mLoginActivityBinding;
    private LoginViewModel mLoginViewModel = null;
    //判断是否从"我的"页面进入
    private boolean intoFromMine = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDataBinding();
        initTopBar();
        initViewModel();
        initData();
        initViews();
    }

    //初始化DataBinding
    private void initDataBinding() {
        mLoginActivityBinding = DataBindingUtil.setContentView(this, R.layout.login_activity);
        mLoginActivityBinding.setHandlers(this);
    }

    //初始化TopBar
    private void initTopBar() {
        //标题栏返回键监听
        mLoginActivityBinding.topBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (intoFromMine) {
                    finish();
                } else {
                    ARouter.getInstance().build(ARouterPath.ActivityMain).navigation();
                    finish();
                }
            }
        });
        //解决返回键不居中问题
        mLoginActivityBinding.topBar.setGravity(Gravity.CENTER_VERTICAL);
    }

    //初始化数据
    private void initData() {
        //初始化Inten传递过来的数据
        if (getIntent() != null) {
            intoFromMine = getIntent().getBooleanExtra(IntentFromMine, false);
        }
        //初始化用户名密码
        mLoginActivityBinding.setPageData(mLoginViewModel.getPageData());
    }

    //初始化ViewModel
    private void initViewModel() {
        mLoginViewModel = ViewModelProviders.of(LoginActivity.this).get(LoginViewModel.class);
        subscribeToModel(mLoginViewModel);
    }

    private void initViews() {
        mLoginActivityBinding.pwdInput.setInputType(0x81); //设置不可见
    }

    //订阅数据变化来刷新UI
    private void subscribeToModel(final LoginViewModel model) {
        //观察数据变化来刷新UI
        model.getLiveLoginData().observe(this, user -> {
            closeLogadingDialog();
            if (user == null) {
                ToastUtils.showNetNoConnected();
                return;
            }
            if (user.isError()) {
                if (user.getCode() == ResponseCode.OPERATION_FAILED) {
                    mLoginViewModel.getPageData().getPassWord().set("");
                }
                ToastUtils.info(user.getMsg(), Toast.LENGTH_SHORT);
                return;
            }
            //缓存数据
            GlobalUserDataStore.getInstance().updateUserData(user);
            if (intoFromMine) {
                finish();
            } else {
                ARouter.getInstance().build(ARouterPath.ActivityMain).navigation();
                finish();
            }

        });
    }

    //登陆
    public void jumpToLogin(View view) {
        String mobileNum = mLoginViewModel.getPageData().getPhoneNum().get().replace(" ", "");
        if (!RegexUtils.isMobileExact(mobileNum)) {
            ToastUtils.info(getResources().getString(R.string.input_correct_phone_num), Toast.LENGTH_SHORT);
            return;
        }
        mLoginActivityBinding.pwdInput.setInputType(0x81);
        mLoginActivityBinding.btShowPwd.setText("显示");
        showLogadingDialog(getResources().getString(R.string.logining));
        mLoginViewModel.doLogin();
    }


    //注册用户
    public void jumpToRegisterPage(View view) {
        ARouter.getInstance().build(ARouterPath.ActivityRegister)
                .navigation(LoginActivity.this, IntentRegisterSuccessCode);
    }

    //忘记密码
    public void jumpToForgotPwdPage(View view) {
        ARouter.getInstance().build(ARouterPath.ActivityForgotLoginPwd).navigation(this, IntentFindPwdSuccessCode);
    }

    //显示密码
    public void showPwd(View view) {
        //设置密码可见
        if (mLoginActivityBinding.pwdInput.getInputType() == 0x81) {
            mLoginActivityBinding.pwdInput.setInputType(0x90);
            mLoginActivityBinding.btShowPwd.setText("隐藏");
        } else { //不可见
            mLoginActivityBinding.pwdInput.setInputType(0x81);
            mLoginActivityBinding.btShowPwd.setText("显示");
        }
        mLoginActivityBinding.pwdInput.requestFocus();
        mLoginActivityBinding.pwdInput.setSelection(mLoginViewModel.getPageData().getPassWord().get().length());
    }

    //微信登陆
    public void jumpToWechatPage(View view) {
        return;
    }

    //支付宝登陆
    public void jumpToAlipayPage(View view) {
        return;
    }

    //用户手机号码输入框内容变化
    public void mobileNumEdittextChanged(CharSequence s, int start, int before, int count) {
        if (TextUtils.isEmpty(s)) {
            mLoginViewModel.getPageData().getPassWord().set("");
            return;
        }
        if (s.toString().length() != DataFormat.MobileNum) {
            mLoginActivityBinding.btLogin.setEnabled(false);
            return;
        }
        String userPwd = mLoginViewModel.getPageData().getPassWord().get();
        if (userPwd.length() >= DataFormat.PassWordLeastLength) {
            mLoginActivityBinding.btLogin.setEnabled(true);
            return;
        }
    }

    //用户密码输入框内容变化
    public void userPwdEdittextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() < DataFormat.PassWordLeastLength) {
            mLoginActivityBinding.btLogin.setEnabled(false);
            return;
        }
        String userNum = mLoginViewModel.getPageData().getPhoneNum().get();
        if (userNum.length() == DataFormat.MobileNum) {
            mLoginActivityBinding.btLogin.setEnabled(true);
            return;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == IntentRegisterSuccessCode) {
                UserLoginData userLoginData = DBRepository.QueryUserLoginData();
                mLoginViewModel.getPageData().getPhoneNum().set(userLoginData.getPhoneNum());
                mLoginViewModel.getPageData().getPassWord().set("");
            } else if (requestCode == IntentFindPwdSuccessCode) {
                UserLoginData userLoginData = DBRepository.QueryUserLoginData();
                mLoginViewModel.getPageData().getPhoneNum().set(userLoginData.getPhoneNum());
                mLoginViewModel.getPageData().getPassWord().set("");
            }
        }
    }
}
