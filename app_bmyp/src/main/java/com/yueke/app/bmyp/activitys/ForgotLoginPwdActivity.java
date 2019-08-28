package com.yueke.app.bmyp.activitys;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.askia.common.base.ARouterPath;
import com.askia.common.base.BaseActivity;
import com.askia.common.util.ToastUtils;
import com.askia.coremodel.datamodel.database.repository.DBRepository;
import com.askia.coremodel.datamodel.http.entities.BaseResponseData;
import com.askia.coremodel.util.RegexUtils;
import com.askia.coremodel.util.RxUtils;
import com.askia.coremodel.viewmodel.ForgotLoginPwdViewModel;
import com.yueke.app.bmyp.DataFormat;
import com.yueke.app.bmyp.R;
import com.yueke.app.bmyp.databinding.ForgotLoginPwdActivityBinding;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

@Route(path = ARouterPath.ActivityForgotLoginPwd)
public class ForgotLoginPwdActivity extends BaseActivity {

    private ForgotLoginPwdActivityBinding mForgotLoginPwdActivityBinding;
    private ForgotLoginPwdViewModel mForgotLoginPwdViewModel;
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
        mForgotLoginPwdActivityBinding = DataBindingUtil.setContentView(ForgotLoginPwdActivity.this, R.layout.forgot_login_pwd_activity);
    }

    private void initTopBar() {
        mForgotLoginPwdActivityBinding.topBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //解决返回键不居中问题
        mForgotLoginPwdActivityBinding.topBar.setGravity(Gravity.CENTER_VERTICAL);
    }

    private void initViewModel() {
        mForgotLoginPwdViewModel = ViewModelProviders.of(this).get(ForgotLoginPwdViewModel.class);
        subscribeToModel(mForgotLoginPwdViewModel);
    }

    private void initData() {
        mForgotLoginPwdActivityBinding.setHandlers(this);
        mForgotLoginPwdActivityBinding.setPageData(mForgotLoginPwdViewModel.getPageData());
    }

    //找回密码
    public void doFindBack(View view) {
        showLogadingDialog();
        mForgotLoginPwdViewModel.findBackLoginPwd();
    }

    //获取验证码
    public void getVerificationCode(View view) {
        String mobileNum = mForgotLoginPwdViewModel.getPageData().getPhoneNum().get().replaceAll(" ", "");
        if (!RegexUtils.isMobileExact(mobileNum)) {
            ToastUtils.info(getResources().getString(R.string.input_correct_phone_num), Toast.LENGTH_SHORT);
            return;
        }
        mForgotLoginPwdViewModel.getPageData().setRealPhoneNum(mobileNum);
        showLogadingDialog();
        mForgotLoginPwdViewModel.senSmsCode();
    }

    //用户手机号码输入框内容变化
    public void mobileNumEdittextChanged(CharSequence s, int start, int before, int count) {
        if (s == null || s.length() == 0) {
            mForgotLoginPwdActivityBinding.btConfirm.setEnabled(false);
            return;
        }
        if (s.length() != DataFormat.MobileNum) {
            mForgotLoginPwdActivityBinding.btConfirm.setEnabled(false);
            mForgotLoginPwdActivityBinding.btGetVerificationCode.setEnabled(false);
            return;
        }
        mForgotLoginPwdActivityBinding.btGetVerificationCode.setEnabled(true);
        //验证码
        String verifiCode = mForgotLoginPwdViewModel.getPageData().getVerificationCode().get();
        //密码
        String userPwd = mForgotLoginPwdViewModel.getPageData().getNewPassWord().get();
        if (verifiCode.length() >= DataFormat.VerificaCodeLeastLength && userPwd.length() >= DataFormat.PassWordLeastLength) {
            mForgotLoginPwdActivityBinding.btConfirm.setEnabled(true);
        } else {
            mForgotLoginPwdActivityBinding.btConfirm.setEnabled(false);
        }

    }

    //验证码输入框内容变化
    public void verificationCodeEdittextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() < DataFormat.VerificaCodeLeastLength) {
            mForgotLoginPwdActivityBinding.btConfirm.setEnabled(false);
            return;
        }
        //用户编号
        String userNum = mForgotLoginPwdViewModel.getPageData().getPhoneNum().get();
        //密码
        String userPwd = mForgotLoginPwdViewModel.getPageData().getNewPassWord().get();
        if (userNum.length() == DataFormat.MobileNum && userPwd.length() >= DataFormat.PassWordLeastLength) {
            mForgotLoginPwdActivityBinding.btConfirm.setEnabled(true);
        } else {
            mForgotLoginPwdActivityBinding.btConfirm.setEnabled(false);
        }
    }

    //用户密码输入框内容变化
    public void userPwdEdittextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() < DataFormat.PassWordLeastLength) {
            mForgotLoginPwdActivityBinding.btConfirm.setEnabled(false);
            return;
        }
        //用户编号
        String userNum = mForgotLoginPwdViewModel.getPageData().getPhoneNum().get();
        //验证码
        String verifiCode = mForgotLoginPwdViewModel.getPageData().getVerificationCode().get();
        if (verifiCode.length() >= DataFormat.VerificaCodeLeastLength && userNum.length() == DataFormat.MobileNum) {
            mForgotLoginPwdActivityBinding.btConfirm.setEnabled(true);
        } else {
            mForgotLoginPwdActivityBinding.btConfirm.setEnabled(false);
        }
    }

    //显示密码
    public void showPwd(View view) {
        //设置密码可见
        if (mForgotLoginPwdActivityBinding.pwdInput.getInputType() == 0x81) {
            mForgotLoginPwdActivityBinding.pwdInput.setInputType(0x90);
            mForgotLoginPwdActivityBinding.btShowPwd.setText("隐藏");
        } else { //不可见
            mForgotLoginPwdActivityBinding.pwdInput.setInputType(0x81);
            mForgotLoginPwdActivityBinding.btShowPwd.setText("显示");
        }
        mForgotLoginPwdActivityBinding.pwdInput.requestFocus();
        mForgotLoginPwdActivityBinding.pwdInput.setSelection(mForgotLoginPwdViewModel.getPageData().getNewPassWord().get().length());
    }

    private void subscribeToModel(final ForgotLoginPwdViewModel model) {
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
            mForgotLoginPwdActivityBinding.btGetVerificationCode.setVisibility(View.GONE);
            mForgotLoginPwdActivityBinding.tvCountdown.setVisibility(View.VISIBLE);
            mDisposable = RxUtils.CountDown(DataFormat.VerificaCodeValidTime, new Consumer<Long>() {
                @Override
                public void accept(Long aLong) throws Exception {
                    mForgotLoginPwdActivityBinding.tvCountdown.setText(aLong + "秒");
                }
            }, new Action() {
                @Override
                public void run() throws Exception {
                    mForgotLoginPwdActivityBinding.btGetVerificationCode.setText("重新获取验证码");
                    mForgotLoginPwdActivityBinding.btGetVerificationCode.setVisibility(View.VISIBLE);
                    mForgotLoginPwdActivityBinding.tvCountdown.setVisibility(View.GONE);
                }
            });
        });

        //找回密码
        model.getFindBackLiveData().observe(this, new Observer<BaseResponseData>() {
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
                ToastUtils.info("找回密码成功");
                DBRepository.UpdateUserNumAndPwd(mForgotLoginPwdViewModel.getPageData().getRealPhoneNum(),
                        mForgotLoginPwdActivityBinding.getPageData().getNewPassWord().get());
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDisposable != null) {
            mDisposable.dispose();
        }
    }


}
