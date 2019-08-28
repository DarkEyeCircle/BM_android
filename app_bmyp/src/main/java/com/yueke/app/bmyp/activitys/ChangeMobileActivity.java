package com.yueke.app.bmyp.activitys;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.askia.common.base.ARouterPath;
import com.askia.common.base.BaseActivity;
import com.askia.common.util.ToastUtils;
import com.askia.coremodel.datamodel.http.entities.BaseResponseData;
import com.askia.coremodel.util.RegexUtils;
import com.askia.coremodel.util.RxUtils;
import com.askia.coremodel.viewmodel.ChangeMobileViewModel;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.yueke.app.bmyp.DataFormat;
import com.yueke.app.bmyp.R;
import com.yueke.app.bmyp.databinding.ChangeMobileActivityBinding;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

@Route(path = ARouterPath.ActivityChangeMobile)
public class ChangeMobileActivity extends BaseActivity {

    private ChangeMobileActivityBinding mChangeMobileActivityBinding = null;
    private ChangeMobileViewModel mChangeMobileViewModel = null;
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
        mChangeMobileActivityBinding = DataBindingUtil.setContentView(this, R.layout.change_mobile_activity);
    }

    private void initTopBar() {
        mChangeMobileActivityBinding.topBar.setTitle(R.string.check_phone_num).getPaint().setFakeBoldText(true);
        mChangeMobileActivityBinding.topBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //解决返回键不居中问题
        mChangeMobileActivityBinding.topBar.setGravity(Gravity.CENTER_VERTICAL);
    }

    private void initViewModel() {
        mChangeMobileViewModel = ViewModelProviders.of(this).get(ChangeMobileViewModel.class);
        subscribeToModel(mChangeMobileViewModel);
    }

    private void initData() {
        mChangeMobileActivityBinding.setHandlers(this);
        mChangeMobileActivityBinding.setPageData(mChangeMobileViewModel.getPageData());
    }

    //验证手机号码
    public void doCheckMobile(View view) {
        showLogadingDialog();
        mChangeMobileViewModel.checkMobile();
    }

    //拨打电话
    public void callPhone(View view) {
        final String phoneNum = mChangeMobileActivityBinding.linkTextView.getText().toString().replaceAll(" ", "");
        new QMUIDialog.MessageDialogBuilder(ChangeMobileActivity.this)
                .setTitle("提示")
                .setMessage("确定拨打电话" + phoneNum + "?")
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction(0, "确定", QMUIDialogAction.ACTION_PROP_NEGATIVE, new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        Uri data = Uri.parse("tel:" + phoneNum);
                        intent.setData(data);
                        startActivity(intent);
                    }
                })
                .create(com.qmuiteam.qmui.R.style.QMUI_Dialog).show();


    }

    //获取验证码
    public void getVerificationCode(View view) {
        String mobileNum = mChangeMobileViewModel.getPageData().getPhoneNum().get().replaceAll(" ", "");
        if (!RegexUtils.isMobileExact(mobileNum)) {
            ToastUtils.info(getResources().getString(R.string.input_correct_phone_num), Toast.LENGTH_SHORT);
            return;
        }
        showLogadingDialog();
        mChangeMobileViewModel.senSmsCode();
    }

    //验证码输入框内容变化
    public void verificationCodeEdittextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() < DataFormat.VerificaCodeLeastLength) {
            mChangeMobileActivityBinding.btConfirm.setEnabled(false);
            return;
        }
        mChangeMobileActivityBinding.btConfirm.setEnabled(true);
    }

    private void subscribeToModel(final ChangeMobileViewModel model) {
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
            mChangeMobileActivityBinding.btGetVerificationCode.setVisibility(View.GONE);
            mChangeMobileActivityBinding.tvCountdown.setVisibility(View.VISIBLE);
            mDisposable = RxUtils.CountDown(DataFormat.VerificaCodeValidTime, new Consumer<Long>() {
                @Override
                public void accept(Long aLong) throws Exception {
                    mChangeMobileActivityBinding.tvCountdown.setText(aLong + "秒");
                }
            }, new Action() {
                @Override
                public void run() throws Exception {
                    mChangeMobileActivityBinding.btGetVerificationCode.setText("重新获取验证码");
                    mChangeMobileActivityBinding.btGetVerificationCode.setVisibility(View.VISIBLE);
                    mChangeMobileActivityBinding.tvCountdown.setVisibility(View.GONE);
                }
            });
        });

        //验证手机号
        model.getModifyMoileLiveData().observe(this, new Observer<BaseResponseData>() {
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
                ARouter.getInstance().build(ARouterPath.ActivityBindNewMobile).navigation();
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
