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
import com.askia.coremodel.datamodel.http.entities.User;
import com.askia.coremodel.util.RegexUtils;
import com.askia.coremodel.util.RxUtils;
import com.askia.coremodel.viewmodel.BindNewMobileViewModel;
import com.yueke.app.bmyp.DataFormat;
import com.yueke.app.bmyp.R;
import com.yueke.app.bmyp.databinding.BindNewMobileActivityBinding;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

@Route(path = ARouterPath.ActivityBindNewMobile)
public class BindNewMobileActivity extends BaseActivity {

    private BindNewMobileActivityBinding mBindNewMobileActivityBinding = null;
    private BindNewMobileViewModel mBindNewMobileViewModel = null;
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
        mBindNewMobileActivityBinding = DataBindingUtil.setContentView(this, R.layout.bind_new_mobile_activity);
    }

    private void initTopBar() {
        mBindNewMobileActivityBinding.topBar.setTitle(R.string.bind_new_moible).getPaint().setFakeBoldText(true);
        mBindNewMobileActivityBinding.topBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //解决返回键不居中问题
        mBindNewMobileActivityBinding.topBar.setGravity(Gravity.CENTER_VERTICAL);
    }

    private void initViewModel() {
        mBindNewMobileViewModel = ViewModelProviders.of(this).get(BindNewMobileViewModel.class);
        subscribeToModel(mBindNewMobileViewModel);
    }

    private void initData() {
        mBindNewMobileActivityBinding.setHandlers(this);
        mBindNewMobileActivityBinding.setPageData(mBindNewMobileViewModel.getPageData());
    }

    //绑定新手机号
    public void doBindMobile(View view) {
        String mobileNum = mBindNewMobileViewModel.getPageData().getPhoneNum().get().replaceAll(" ", "");
        if (mobileNum.equals(GlobalUserDataStore.getInstance().getMobile())) {
            ToastUtils.info("与当前绑定手机一致，请重新输入");
            return;
        }
        showLogadingDialog();
        mBindNewMobileViewModel.checkMobile();
    }

    //获取验证码
    public void getVerificationCode(View view) {
        String mobileNum = mBindNewMobileViewModel.getPageData().getPhoneNum().get().replaceAll(" ", "");
        if (!RegexUtils.isMobileExact(mobileNum)) {
            ToastUtils.info(getResources().getString(R.string.input_correct_phone_num), Toast.LENGTH_SHORT);
            return;
        }
        showLogadingDialog();
        mBindNewMobileViewModel.senSmsCode();
    }

    //用户手机号码输入框内容变化
    public void mobileNumEdittextChanged(CharSequence s, int start, int before, int count) {
        if (s == null || s.length() == 0) {
            mBindNewMobileActivityBinding.btConfirm.setEnabled(false);
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
            mBindNewMobileActivityBinding.etPhone.setText(sb.toString());
            mBindNewMobileActivityBinding.etPhone.setSelection(index);
        }
        if (s.length() != DataFormat.MobileNum) {
            mBindNewMobileActivityBinding.btConfirm.setEnabled(false);
            mBindNewMobileActivityBinding.btGetVerificationCode.setEnabled(false);
            return;
        }
        mBindNewMobileActivityBinding.btGetVerificationCode.setEnabled(true);
        //验证码
        String verifiCode = mBindNewMobileViewModel.getPageData().getVerificationCode().get();
        if (verifiCode.length() >= DataFormat.VerificaCodeLeastLength) {
            mBindNewMobileActivityBinding.btConfirm.setEnabled(true);
        } else {
            mBindNewMobileActivityBinding.btConfirm.setEnabled(false);
        }

    }

    //验证码输入框内容变化
    public void verificationCodeEdittextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() < DataFormat.VerificaCodeLeastLength) {
            mBindNewMobileActivityBinding.btConfirm.setEnabled(false);
            return;
        }
        //用户编号
        String userNum = mBindNewMobileViewModel.getPageData().getPhoneNum().get();
        if (userNum.length() == DataFormat.MobileNum) {
            mBindNewMobileActivityBinding.btConfirm.setEnabled(true);
        } else {
            mBindNewMobileActivityBinding.btConfirm.setEnabled(false);
        }
    }

    private void subscribeToModel(final BindNewMobileViewModel model) {
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
            mBindNewMobileActivityBinding.btGetVerificationCode.setVisibility(View.GONE);
            mBindNewMobileActivityBinding.tvCountdown.setVisibility(View.VISIBLE);
            mDisposable = RxUtils.CountDown(DataFormat.VerificaCodeValidTime, new Consumer<Long>() {
                @Override
                public void accept(Long aLong) throws Exception {
                    mBindNewMobileActivityBinding.tvCountdown.setText(aLong + "秒");
                }
            }, new Action() {
                @Override
                public void run() throws Exception {
                    mBindNewMobileActivityBinding.btGetVerificationCode.setText("重新获取验证码");
                    mBindNewMobileActivityBinding.btGetVerificationCode.setVisibility(View.VISIBLE);
                    mBindNewMobileActivityBinding.tvCountdown.setVisibility(View.GONE);
                }
            });
        });

        //验证手机号
        model.getBindMoileLiveData().observe(this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable User responseData) {
                closeLogadingDialog();
                if (responseData == null) {
                    ToastUtils.showNetNoConnected();
                    return;
                }
                if (responseData.isError()) {
                    resolveResponseErrorCode(responseData.getCode(), responseData.getMsg());
                    return;
                }
                GlobalUserDataStore.getInstance().updateUserData(responseData);
                ToastUtils.info("修改成功");
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
