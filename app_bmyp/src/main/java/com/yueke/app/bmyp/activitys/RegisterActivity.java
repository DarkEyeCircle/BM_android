package com.yueke.app.bmyp.activitys;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.askia.common.base.ARouterPath;
import com.askia.common.base.BaseActivity;
import com.askia.common.util.ToastUtils;
import com.askia.coremodel.datamodel.database.repository.DBRepository;
import com.askia.coremodel.util.RegexUtils;
import com.askia.coremodel.util.RxUtils;
import com.askia.coremodel.viewmodel.RegisterViewModel;
import com.yueke.app.bmyp.DataFormat;
import com.yueke.app.bmyp.R;
import com.yueke.app.bmyp.databinding.RegisteredActivityBinding;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

@Route(path = ARouterPath.ActivityRegister)
public class RegisterActivity extends BaseActivity {

    private RegisteredActivityBinding mRegisteredActivityBinding;
    private RegisterViewModel mRegisterViewModel = null;
    private Disposable mDisposable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDataBinding();
        initTopBar();
        initViewModel();
        initData();
    }

    private void initDataBinding() {
        mRegisteredActivityBinding = DataBindingUtil.setContentView(this, R.layout.registered_activity);
    }

    private void initTopBar() {
        mRegisteredActivityBinding.topBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //解决返回键不居中问题
        mRegisteredActivityBinding.topBar.setGravity(Gravity.CENTER_VERTICAL);
    }

    private void initViewModel() {
        mRegisterViewModel = ViewModelProviders.of(this).get(RegisterViewModel.class);
        subscribeToModel(mRegisterViewModel);
    }

    private void initData() {
        mRegisteredActivityBinding.setHandlers(this);
        mRegisteredActivityBinding.setPageData(mRegisterViewModel.getPageData());
    }

    //注册
    public void doRegister(View view) {
        mRegisteredActivityBinding.pwdInput.setInputType(0x81);
        mRegisteredActivityBinding.btShowPwd.setText("显示");
        showLogadingDialog("正在注册");
        mRegisterViewModel.getAlpha();
    }

    //获取验证码
    public void getVerificationCode(View view) {
        String mobileNum = mRegisterViewModel.getPageData().getPhoneNum().get().replaceAll(" ", "");
        if (!RegexUtils.isMobileExact(mobileNum)) {
            ToastUtils.info(getResources().getString(R.string.input_correct_phone_num), Toast.LENGTH_SHORT);
            return;
        }
        mRegisterViewModel.getPageData().setRealPhoneNum(mobileNum);
        showLogadingDialog();
        mRegisterViewModel.senSmsCode();

    }

    //查看用户协议
    public void lookAtUserAgreement(View view) {
        ARouter.getInstance().build(ARouterPath.ActivityServiceTerms).navigation();
    }

    //已有账号去登陆
    public void jumpToLogin(View view) {
        finish();
    }


    //订阅数据变化来刷新UI
    private void subscribeToModel(final RegisterViewModel model) {
        //获取验证码
        model.getSmsLiveData().observe(this, responseData -> {
            closeLogadingDialog();
            if (responseData == null) {
                ToastUtils.showNetNoConnected();
                return;
            }
            if (responseData.isError()) {
                resolveResponseErrorCode(responseData.getCode(), responseData.getMsg());
                return;
            }
            //开始倒计时
            mRegisteredActivityBinding.btGetVerificationCode.setVisibility(View.GONE);
            mRegisteredActivityBinding.tvCountdown.setVisibility(View.VISIBLE);
            mDisposable = RxUtils.CountDown(DataFormat.VerificaCodeValidTime, new Consumer<Long>() {
                @Override
                public void accept(Long aLong) throws Exception {
                    mRegisteredActivityBinding.tvCountdown.setText(aLong + "秒");
                }
            }, new Action() {
                @Override
                public void run() throws Exception {
                    mRegisteredActivityBinding.btGetVerificationCode.setText("重新获取验证码");
                    mRegisteredActivityBinding.btGetVerificationCode.setVisibility(View.VISIBLE);
                    mRegisteredActivityBinding.tvCountdown.setVisibility(View.GONE);
                }
            });
        });

        //注册
        model.getRegistLiveData().observe(this, responseData -> {
            closeLogadingDialog();
            if (responseData == null) {
                ToastUtils.showNetNoConnected();
                return;
            }
            if (responseData.isError()) {
                resolveResponseErrorCode(responseData.getCode(), responseData.getMsg());
                return;
            }
            ToastUtils.info("注册成功");
            DBRepository.UpdateUserNumAndPwd(mRegisterViewModel.getPageData().getRealPhoneNum(),
                    mRegisterViewModel.getPageData().getPassWord().get());
            setResult(RESULT_OK);
            finish();
        });

        //Alpha
        model.getAlphaLiveData().observe(this, responseData -> {
            closeLogadingDialog();
            if (responseData == null) {
                ToastUtils.showNetNoConnected();
                return;
            }
            if (responseData.isError()) {
                resolveResponseErrorCode(responseData.getCode(), responseData.getMsg());
                return;
            }
            mRegisterViewModel.registAccount((String) responseData.getObj());
        });

    }


    //用户手机号码输入框内容变化
    public void mobileNumEdittextChanged(CharSequence s, int start, int before, int count) {
        if (s == null || s.length() == 0) {
            mRegisteredActivityBinding.btRegist.setEnabled(false);
            return;
        }
        if (s.toString().length() != DataFormat.MobileNum) {
            mRegisteredActivityBinding.btRegist.setEnabled(false);
            mRegisteredActivityBinding.btGetVerificationCode.setEnabled(false);
            return;
        }
        mRegisteredActivityBinding.btGetVerificationCode.setEnabled(true);
        //验证码
        String verifiCode = mRegisterViewModel.getPageData().getVerificationCode().get();
        //密码
        String userPwd = mRegisterViewModel.getPageData().getPassWord().get();
        if (verifiCode.length() >= DataFormat.VerificaCodeLeastLength && userPwd.length() >= DataFormat.PassWordLeastLength) {
            mRegisteredActivityBinding.btRegist.setEnabled(true);
        } else {
            mRegisteredActivityBinding.btRegist.setEnabled(false);
        }

    }

    //验证码输入框内容变化
    public void verificationCodeEdittextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() < DataFormat.VerificaCodeLeastLength) {
            mRegisteredActivityBinding.btRegist.setEnabled(false);
            return;
        }
        //用户编号
        String userNum = mRegisterViewModel.getPageData().getPhoneNum().get();
        //密码
        String userPwd = mRegisterViewModel.getPageData().getPassWord().get();
        if (userNum.length() == DataFormat.MobileNum && userPwd.length() >= DataFormat.PassWordLeastLength) {
            mRegisteredActivityBinding.btRegist.setEnabled(true);
        } else {
            mRegisteredActivityBinding.btRegist.setEnabled(false);
        }
    }

    //用户密码输入框内容变化
    public void userPwdEdittextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() < DataFormat.PassWordLeastLength) {
            mRegisteredActivityBinding.btRegist.setEnabled(false);
            return;
        }
        //用户编号
        String userNum = mRegisterViewModel.getPageData().getPhoneNum().get();
        //验证码
        String verifiCode = mRegisterViewModel.getPageData().getVerificationCode().get();
        if (verifiCode.length() >= DataFormat.VerificaCodeLeastLength && userNum.length() == DataFormat.MobileNum) {
            mRegisteredActivityBinding.btRegist.setEnabled(true);
        } else {
            mRegisteredActivityBinding.btRegist.setEnabled(false);
        }
    }

    //显示密码
    public void showPwd(View view) {
        //设置密码可见
        if (mRegisteredActivityBinding.pwdInput.getInputType() == 0x81) {
            mRegisteredActivityBinding.pwdInput.setInputType(0x90);
            mRegisteredActivityBinding.btShowPwd.setText("隐藏");
        } else { //不可见
            mRegisteredActivityBinding.pwdInput.setInputType(0x81);
            mRegisteredActivityBinding.btShowPwd.setText("显示");
        }
        mRegisteredActivityBinding.pwdInput.requestFocus();
        mRegisteredActivityBinding.pwdInput.setSelection(mRegisterViewModel.getPageData().getPassWord().get().length());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDisposable != null) {
            mDisposable.dispose();
        }
    }

}
