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
import com.askia.coremodel.GlobalUserDataStore;
import com.askia.coremodel.datamodel.http.entities.BaseResponseData;
import com.askia.coremodel.util.RegexUtils;
import com.askia.coremodel.util.RxUtils;
import com.askia.coremodel.viewmodel.SettingSecurityPwdViewModel;
import com.yueke.app.bmyp.DataFormat;
import com.yueke.app.bmyp.R;
import com.yueke.app.bmyp.databinding.SettingSecurityPwdActivityBinding;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

@Route(path = ARouterPath.ActivitySettingSecurityPwd)
public class SettingSecurityPwdActivity extends BaseActivity {

    private SettingSecurityPwdActivityBinding mSettingSecurityPwdActivityBinding = null;
    private SettingSecurityPwdViewModel mSettingSecurityPwdViewModel = null;
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
        mSettingSecurityPwdActivityBinding = DataBindingUtil.setContentView(this, R.layout.setting_security_pwd_activity);
    }

    private void initTopBar() {
        mSettingSecurityPwdActivityBinding.topBar.setTitle(R.string.setting_pay_password).getPaint().setFakeBoldText(true);
        mSettingSecurityPwdActivityBinding.topBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //解决返回键不居中问题
        mSettingSecurityPwdActivityBinding.topBar.setGravity(Gravity.CENTER_VERTICAL);
    }

    private void initViewModel() {
        mSettingSecurityPwdViewModel = ViewModelProviders.of(this).get(SettingSecurityPwdViewModel.class);
        subscribeToModel(mSettingSecurityPwdViewModel);
    }

    private void initData() {
        mSettingSecurityPwdActivityBinding.setHandlers(this);
        mSettingSecurityPwdActivityBinding.setPageData(mSettingSecurityPwdViewModel.getPageData());
    }

    //找回密码
    public void doSet(View view) {
        if (!mSettingSecurityPwdViewModel.getPageData().getPassWord().get().equals(mSettingSecurityPwdViewModel.getPageData().getConfirmPwd().get())) {
            ToastUtils.info("两次密码不正确");
            return;
        }
        showLogadingDialog();
        mSettingSecurityPwdViewModel.settingSecurityPwd();
    }

    //获取验证码
    public void getVerificationCode(View view) {
        String mobileNum = mSettingSecurityPwdViewModel.getPageData().getPhoneNum().get().replaceAll(" ", "");
        if (!RegexUtils.isMobileExact(mobileNum)) {
            ToastUtils.info(getResources().getString(R.string.input_correct_phone_num), Toast.LENGTH_SHORT);
            return;
        }
        showLogadingDialog();
        mSettingSecurityPwdViewModel.senSmsCode();
    }

    //用户手机号码输入框内容变化
    public void mobileNumEdittextChanged(CharSequence s, int start, int before, int count) {
        if (s == null || s.length() == 0) {
            mSettingSecurityPwdActivityBinding.btConfirm.setEnabled(false);
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            if (i != 3 && i != 8 && s.charAt(i) == ' ') {
                continue;
            } else {
                sb.append(s.charAt(i));
                if ((sb.length() == 4 || sb.length() == 9) && sb.charAt(sb.length() - 1) != ' ') {
                    sb.insert(sb.length() - 1, ' ');
                }
            }
        }
        if (!sb.toString().equals(s.toString())) {
            int index = start + 1;
            if (sb.charAt(start) == ' ') {
                if (before == 0) {
                    index++;
                } else {
                    index--;
                }
            } else {
                if (before == 1) {
                    index--;
                }
            }
            mSettingSecurityPwdActivityBinding.etPhone.setText(sb.toString());
            mSettingSecurityPwdActivityBinding.etPhone.setSelection(index);
        }
        if (s.length() != DataFormat.MobileNum) {
            mSettingSecurityPwdActivityBinding.btConfirm.setEnabled(false);
            mSettingSecurityPwdActivityBinding.btGetVerificationCode.setEnabled(false);
            return;
        }
        mSettingSecurityPwdActivityBinding.btGetVerificationCode.setEnabled(true);
        //验证码
        String verifiCode = mSettingSecurityPwdViewModel.getPageData().getVerificationCode().get();
        //密码
        String userPwd = mSettingSecurityPwdViewModel.getPageData().getPassWord().get();
        String confirmPwd = mSettingSecurityPwdViewModel.getPageData().getConfirmPwd().get();
        if (verifiCode.length() >= DataFormat.VerificaCodeLeastLength &&
                userPwd.length() >= DataFormat.PassWordLeastLength &&
                confirmPwd.length() >= DataFormat.PassWordLeastLength) {
            mSettingSecurityPwdActivityBinding.btConfirm.setEnabled(true);
        } else {
            mSettingSecurityPwdActivityBinding.btConfirm.setEnabled(false);
        }

    }

    //验证码输入框内容变化
    public void verificationCodeEdittextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() < DataFormat.VerificaCodeLeastLength) {
            mSettingSecurityPwdActivityBinding.btConfirm.setEnabled(false);
            return;
        }
        //用户编号
        String userNum = mSettingSecurityPwdViewModel.getPageData().getPhoneNum().get();
        //密码
        String userPwd = mSettingSecurityPwdViewModel.getPageData().getPassWord().get();
        String confirmPwd = mSettingSecurityPwdViewModel.getPageData().getConfirmPwd().get();
        if (userNum.length() == DataFormat.MobileNum &&
                userPwd.length() >= DataFormat.PassWordLeastLength &&
                confirmPwd.length() >= DataFormat.PassWordLeastLength) {
            mSettingSecurityPwdActivityBinding.btConfirm.setEnabled(true);
        } else {
            mSettingSecurityPwdActivityBinding.btConfirm.setEnabled(false);
        }
    }

    //用户密码输入框内容变化
    public void userPwdEdittextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() < DataFormat.PassWordLeastLength) {
            mSettingSecurityPwdActivityBinding.btConfirm.setEnabled(false);
            return;
        }
        //用户编号
        String userNum = mSettingSecurityPwdViewModel.getPageData().getPhoneNum().get();
        //验证码
        String verifiCode = mSettingSecurityPwdViewModel.getPageData().getVerificationCode().get();
        String confirmPwd = mSettingSecurityPwdViewModel.getPageData().getConfirmPwd().get();
        if (verifiCode.length() >= DataFormat.VerificaCodeLeastLength &&
                userNum.length() == DataFormat.MobileNum &&
                confirmPwd.length() >= DataFormat.PassWordLeastLength) {
            mSettingSecurityPwdActivityBinding.btConfirm.setEnabled(true);
        } else {
            mSettingSecurityPwdActivityBinding.btConfirm.setEnabled(false);
        }
    }

    //用户再次确认密码输入框内容变化
    public void userConfirmPwdEdittextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() < DataFormat.PassWordLeastLength) {
            mSettingSecurityPwdActivityBinding.btConfirm.setEnabled(false);
            return;
        }
        //用户编号
        String userNum = mSettingSecurityPwdViewModel.getPageData().getPhoneNum().get();
        //验证码
        String verifiCode = mSettingSecurityPwdViewModel.getPageData().getVerificationCode().get();
        //密码
        String userPwd = mSettingSecurityPwdViewModel.getPageData().getPassWord().get();
        if (verifiCode.length() >= DataFormat.VerificaCodeLeastLength &&
                userNum.length() == DataFormat.MobileNum &&
                userPwd.length() >= DataFormat.PassWordLeastLength) {
            mSettingSecurityPwdActivityBinding.btConfirm.setEnabled(true);
        } else {
            mSettingSecurityPwdActivityBinding.btConfirm.setEnabled(false);
        }
    }

    private void subscribeToModel(final SettingSecurityPwdViewModel model) {
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
            mSettingSecurityPwdActivityBinding.btGetVerificationCode.setVisibility(View.GONE);
            mSettingSecurityPwdActivityBinding.tvCountdown.setVisibility(View.VISIBLE);
            mDisposable = RxUtils.CountDown(DataFormat.VerificaCodeValidTime, new Consumer<Long>() {
                @Override
                public void accept(Long aLong) throws Exception {
                    mSettingSecurityPwdActivityBinding.tvCountdown.setText(aLong + "秒");
                }
            }, new Action() {
                @Override
                public void run() throws Exception {
                    mSettingSecurityPwdActivityBinding.btGetVerificationCode.setText("重新获取验证码");
                    mSettingSecurityPwdActivityBinding.btGetVerificationCode.setVisibility(View.VISIBLE);
                    mSettingSecurityPwdActivityBinding.tvCountdown.setVisibility(View.GONE);
                }
            });
        });

        //设置安全密码
        model.getSetPwdLiveData().observe(this, new Observer<BaseResponseData>() {
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
                ToastUtils.info("设置成功");
                GlobalUserDataStore.getInstance().setSecurityPassword("######");
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
