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
import com.askia.coremodel.datamodel.http.entities.BaseResponseData;
import com.askia.coremodel.util.RegexUtils;
import com.askia.coremodel.util.RxUtils;
import com.askia.coremodel.viewmodel.RetrieveLoginPwdViewModel;
import com.yueke.app.bmyp.DataFormat;
import com.yueke.app.bmyp.R;
import com.yueke.app.bmyp.databinding.RetrieveLoginPwdActivityBinding;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

@Route(path = ARouterPath.ActivityRetrieveLoginPwd)
public class RetrieveLoginPwdActivity extends BaseActivity {

    private RetrieveLoginPwdActivityBinding mRetrieveLoginPwdActivityBinding = null;
    private RetrieveLoginPwdViewModel mRetrieveLoginPwdViewModel = null;
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
        mRetrieveLoginPwdActivityBinding = DataBindingUtil.setContentView(this, R.layout.retrieve_login_pwd_activity);
    }

    private void initTopBar() {
        mRetrieveLoginPwdActivityBinding.topBar.setTitle(R.string.retrieve_login_password).getPaint().setFakeBoldText(true);
        mRetrieveLoginPwdActivityBinding.topBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //解决返回键不居中问题
        mRetrieveLoginPwdActivityBinding.topBar.setGravity(Gravity.CENTER_VERTICAL);
    }

    private void initViewModel() {
        mRetrieveLoginPwdViewModel = ViewModelProviders.of(this).get(RetrieveLoginPwdViewModel.class);
        subscribeToModel(mRetrieveLoginPwdViewModel);
    }

    private void initData() {
        mRetrieveLoginPwdActivityBinding.setHandlers(this);
        mRetrieveLoginPwdActivityBinding.setPageData(mRetrieveLoginPwdViewModel.getPageData());
    }

    //找回密码
    public void doFindBack(View view) {
        if (!mRetrieveLoginPwdViewModel.getPageData().getNewPassWord().get().equals(mRetrieveLoginPwdViewModel.getPageData().getConfirmPwd().get())) {
            ToastUtils.info("两次密码不正确");
            return;
        }
        showLogadingDialog();
        mRetrieveLoginPwdViewModel.findBackLoginPwd();
    }

    //获取验证码
    public void getVerificationCode(View view) {
        String mobileNum = mRetrieveLoginPwdViewModel.getPageData().getRealPhoneNum();
        if (!RegexUtils.isMobileExact(mobileNum)) {
            ToastUtils.info(getResources().getString(R.string.input_correct_phone_num), Toast.LENGTH_SHORT);
            return;
        }
        showLogadingDialog();
        mRetrieveLoginPwdViewModel.senSmsCode();
    }

    //验证码输入框内容变化
    public void verificationCodeEdittextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() < DataFormat.VerificaCodeLeastLength) {
            mRetrieveLoginPwdActivityBinding.btConfirm.setEnabled(false);
            return;
        }
        //密码
        String userPwd = mRetrieveLoginPwdViewModel.getPageData().getNewPassWord().get();
        String confirmPwd = mRetrieveLoginPwdViewModel.getPageData().getConfirmPwd().get();
        if (userPwd.length() >= DataFormat.PassWordLeastLength &&
                confirmPwd.length() >= DataFormat.PassWordLeastLength) {
            mRetrieveLoginPwdActivityBinding.btConfirm.setEnabled(true);
        } else {
            mRetrieveLoginPwdActivityBinding.btConfirm.setEnabled(false);
        }
    }

    //用户密码输入框内容变化
    public void userNewPwdEdittextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() < DataFormat.PassWordLeastLength) {
            mRetrieveLoginPwdActivityBinding.btConfirm.setEnabled(false);
            return;
        }
        //验证码
        String verifiCode = mRetrieveLoginPwdViewModel.getPageData().getVerificationCode().get();
        String confirmPwd = mRetrieveLoginPwdViewModel.getPageData().getConfirmPwd().get();
        if (verifiCode.length() >= DataFormat.VerificaCodeLeastLength &&
                confirmPwd.length() >= DataFormat.PassWordLeastLength) {
            mRetrieveLoginPwdActivityBinding.btConfirm.setEnabled(true);
        } else {
            mRetrieveLoginPwdActivityBinding.btConfirm.setEnabled(false);
        }
    }

    //用户再次确认密码输入框内容变化
    public void userConfirmPwdEdittextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() < DataFormat.PassWordLeastLength) {
            mRetrieveLoginPwdActivityBinding.btConfirm.setEnabled(false);
            return;
        }
        //验证码
        String verifiCode = mRetrieveLoginPwdViewModel.getPageData().getVerificationCode().get();
        //密码
        String userPwd = mRetrieveLoginPwdViewModel.getPageData().getNewPassWord().get();
        if (verifiCode.length() >= DataFormat.VerificaCodeLeastLength &&
                userPwd.length() >= DataFormat.PassWordLeastLength) {
            mRetrieveLoginPwdActivityBinding.btConfirm.setEnabled(true);
        } else {
            mRetrieveLoginPwdActivityBinding.btConfirm.setEnabled(false);
        }
    }

    private void subscribeToModel(final RetrieveLoginPwdViewModel model) {
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
            mRetrieveLoginPwdActivityBinding.btGetVerificationCode.setVisibility(View.GONE);
            mRetrieveLoginPwdActivityBinding.tvCountdown.setVisibility(View.VISIBLE);
            mDisposable = RxUtils.CountDown(DataFormat.VerificaCodeValidTime, new Consumer<Long>() {
                @Override
                public void accept(Long aLong) throws Exception {
                    mRetrieveLoginPwdActivityBinding.tvCountdown.setText(aLong + "秒");
                }
            }, new Action() {
                @Override
                public void run() throws Exception {
                    mRetrieveLoginPwdActivityBinding.btGetVerificationCode.setText("重新获取验证码");
                    mRetrieveLoginPwdActivityBinding.btGetVerificationCode.setVisibility(View.VISIBLE);
                    mRetrieveLoginPwdActivityBinding.tvCountdown.setVisibility(View.GONE);
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
