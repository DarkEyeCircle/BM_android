package com.yueke.app.bmyp.activitys;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.askia.common.base.ARouterPath;
import com.askia.common.base.BaseActivity;
import com.askia.common.util.ToastUtils;
import com.askia.coremodel.datamodel.http.entities.BankCardInfoData;
import com.askia.coremodel.datamodel.http.entities.BaseResponseData;
import com.askia.coremodel.util.RegexUtils;
import com.askia.coremodel.util.RxUtils;
import com.askia.coremodel.viewmodel.AddBankCardViewModel;
import com.yueke.app.bmyp.DataFormat;
import com.yueke.app.bmyp.R;
import com.yueke.app.bmyp.databinding.AddBankCardActivityBinding;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

@Route(path = ARouterPath.ActivityAddBankCard)
public class AddBankCardActivity extends BaseActivity {

    private AddBankCardActivityBinding mAddBankCardActivityBinding = null;
    private AddBankCardViewModel mAddBankCardViewModel = null;
    private Disposable mDisposable = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDataBinding();
        initTopBar();
        initViewModel();
        initData();
    }

    private void initDataBinding() {
        mAddBankCardActivityBinding = DataBindingUtil.setContentView(this, R.layout.add_bank_card_activity);
    }

    private void initTopBar() {
        mAddBankCardActivityBinding.topBar.setTitle(R.string.add_bank_card).getPaint().setFakeBoldText(true);
        mAddBankCardActivityBinding.topBar.addLeftBackImageButton().setOnClickListener(v -> finish());
        mAddBankCardActivityBinding.topBar.setGravity(Gravity.CENTER_VERTICAL);
    }

    private void initViewModel() {
        mAddBankCardViewModel = ViewModelProviders.of(AddBankCardActivity.this).get(AddBankCardViewModel.class);
        subscribeToModel(mAddBankCardViewModel);
    }

    private void initData() {
        mAddBankCardActivityBinding.setHandlers(this);
        mAddBankCardActivityBinding.setPageData(mAddBankCardViewModel.getPageData());
        mAddBankCardActivityBinding.etBankNum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                String bankCardNum = mAddBankCardViewModel.getPageData().getBankCardNum().get();
                if (!b) {
                    if (!TextUtils.isEmpty(bankCardNum)) {
                        mAddBankCardViewModel.getBankCardInfo();
                    }
                }
            }
        });
    }

    //保存银行卡
    public void storeBankCard(View view) {
        mAddBankCardViewModel.addBankCard();
        showLogadingDialog();
    }

    //获取验证码
    public void getVerificationCode(View view) {
        String mobileNum = mAddBankCardViewModel.getPageData().getPhoneNum().get().replaceAll(" ", "");
        if (!RegexUtils.isMobileExact(mobileNum)) {
            ToastUtils.info(getResources().getString(R.string.input_correct_phone_num), Toast.LENGTH_SHORT);
            return;
        }
        mAddBankCardViewModel.getPageData().setRealPhoneNum(mobileNum);
        showLogadingDialog();
        mAddBankCardViewModel.senSmsCode();
    }

    //用户手机号码输入框内容变化
    public void mobileNumEdittextChanged(CharSequence s, int start, int before, int count) {
        if (s == null || s.length() == 0) {
            mAddBankCardActivityBinding.btSave.setEnabled(false);
            return;
        }
        if (s.toString().length() != DataFormat.MobileNum) {
            mAddBankCardActivityBinding.btSave.setEnabled(false);
            mAddBankCardActivityBinding.btGetVerificationCode.setEnabled(false);
            return;
        }
        mAddBankCardActivityBinding.btGetVerificationCode.setEnabled(true);
        //银行卡号
        String bankCardNum = mAddBankCardViewModel.getPageData().getBankCardNum().get();
        //开户行
        String bankNmae = mAddBankCardViewModel.getPageData().getBankName().get();
        //开户行所在城市
        String bankCity = mAddBankCardViewModel.getPageData().getBankCity().get();
        //验证码
        String verifiCode = mAddBankCardViewModel.getPageData().getVerifiCode().get();
        if (bankCardNum.length() >= DataFormat.BankCardNumLeastLength &&
                bankNmae.length() >= DataFormat.BankNameLeastLength &&
                verifiCode.length() >= DataFormat.VerificaCodeLeastLength &&
                bankCity.length() >= DataFormat.BankCardCityLeastLength) {
            mAddBankCardActivityBinding.btSave.setEnabled(true);
            return;
        }
        mAddBankCardActivityBinding.btSave.setEnabled(false);
    }

    //银行卡号输入框内容变化
    public void userCardNumEdittextChanged(CharSequence s, int start, int before, int count) {
        mAddBankCardViewModel.getPageData().getBankName().set("");
        if (s.length() < DataFormat.BankCardNumLeastLength) {
            mAddBankCardActivityBinding.btSave.setEnabled(false);
            return;
        }
        //开户行
        String bankNmae = mAddBankCardViewModel.getPageData().getBankName().get();
        //验证码
        String verifiCode = mAddBankCardViewModel.getPageData().getVerifiCode().get();
        //手机号
        String phoneNum = mAddBankCardViewModel.getPageData().getPhoneNum().get();
        //开户行所在城市
        String bankCity = mAddBankCardViewModel.getPageData().getBankCity().get();
        if (phoneNum.length() == DataFormat.MobileNum &&
                bankNmae.length() >= DataFormat.BankNameLeastLength &&
                verifiCode.length() >= DataFormat.VerificaCodeLeastLength &&
                bankCity.length() >= DataFormat.BankCardCityLeastLength) {
            mAddBankCardActivityBinding.btSave.setEnabled(true);
            return;
        }
        mAddBankCardActivityBinding.btSave.setEnabled(false);

    }

    //开户行所在城市
    public void bankCardCityEdittextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() < DataFormat.BankCardCityLeastLength) {
            mAddBankCardActivityBinding.btSave.setEnabled(false);
            return;
        }
        //银行卡号
        String bankCardNum = mAddBankCardViewModel.getPageData().getBankCardNum().get();
        //开户行
        String bankNmae = mAddBankCardViewModel.getPageData().getBankName().get();
        //手机号
        String phoneNum = mAddBankCardViewModel.getPageData().getPhoneNum().get();
        //验证码
        String verifiCode = mAddBankCardViewModel.getPageData().getVerifiCode().get();
        if (phoneNum.length() == DataFormat.MobileNum &&
                bankCardNum.length() >= DataFormat.BankCardNumLeastLength &&
                bankNmae.length() >= DataFormat.BankNameLeastLength &&
                verifiCode.length() >= DataFormat.VerificaCodeLeastLength) {
            mAddBankCardActivityBinding.btSave.setEnabled(true);
            return;
        }
        mAddBankCardActivityBinding.btSave.setEnabled(false);
    }

    //验证码
    public void userVerifiCodeEdittextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() < DataFormat.VerificaCodeLeastLength) {
            mAddBankCardActivityBinding.btSave.setEnabled(false);
            return;
        }
        //银行卡号
        String bankCardNum = mAddBankCardViewModel.getPageData().getBankCardNum().get();
        //开户行
        String bankNmae = mAddBankCardViewModel.getPageData().getBankName().get();
        //手机号
        String phoneNum = mAddBankCardViewModel.getPageData().getPhoneNum().get();
        //开户行所在城市
        String bankCity = mAddBankCardViewModel.getPageData().getBankCity().get();
        if (phoneNum.length() == DataFormat.MobileNum &&
                bankCardNum.length() >= DataFormat.BankCardNumLeastLength &&
                bankNmae.length() >= DataFormat.BankNameLeastLength &&
                bankCity.length() >= DataFormat.BankCardCityLeastLength) {
            mAddBankCardActivityBinding.btSave.setEnabled(true);
            return;
        }
        mAddBankCardActivityBinding.btSave.setEnabled(false);
    }

    private void subscribeToModel(final AddBankCardViewModel model) {
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
            mAddBankCardActivityBinding.btGetVerificationCode.setVisibility(View.GONE);
            mAddBankCardActivityBinding.tvCountdown.setVisibility(View.VISIBLE);
            mDisposable = RxUtils.CountDown(DataFormat.VerificaCodeValidTime, new Consumer<Long>() {
                @Override
                public void accept(Long aLong) throws Exception {
                    mAddBankCardActivityBinding.tvCountdown.setText(aLong + "秒");
                }
            }, new Action() {
                @Override
                public void run() throws Exception {
                    mAddBankCardActivityBinding.btGetVerificationCode.setText("重新获取验证码");
                    mAddBankCardActivityBinding.btGetVerificationCode.setVisibility(View.VISIBLE);
                    mAddBankCardActivityBinding.tvCountdown.setVisibility(View.GONE);
                }
            });
        });

        //添加银行卡
        model.getAddBankCardLiveData().observe(this, new Observer<BaseResponseData>() {
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
                ToastUtils.info("添加成功");
                setResult(RESULT_OK);
                finish();
            }
        });
        //银行卡信息
        model.getCardInfoLiveData().observe(this, new Observer<BankCardInfoData>() {
            @Override
            public void onChanged(@Nullable BankCardInfoData responseData) {
                closeLogadingDialog();
                if (responseData == null) {
                    ToastUtils.showNetNoConnected();
                    return;
                }
                if (responseData.isError()) {
                    resolveResponseErrorCode(responseData.getCode(), responseData.getMsg());
                    return;
                }
                mAddBankCardViewModel.getPageData().getBankName().set(responseData.getObj().getOpeningBank());
                //银行卡号
                String bankCardNum = mAddBankCardViewModel.getPageData().getBankCardNum().get();
                //验证码
                String verifiCode = mAddBankCardViewModel.getPageData().getVerifiCode().get();
                //手机号
                String phoneNum = mAddBankCardViewModel.getPageData().getPhoneNum().get();
                //开户行所在城市
                String bankCity = mAddBankCardViewModel.getPageData().getBankCity().get();
                if (phoneNum.length() == DataFormat.MobileNum &&
                        bankCardNum.length() >= DataFormat.BankCardNumLeastLength &&
                        verifiCode.length() >= DataFormat.VerificaCodeLeastLength &&
                        bankCity.length() >= DataFormat.BankCardCityLeastLength) {
                    mAddBankCardActivityBinding.btSave.setEnabled(true);
                    return;
                }
                mAddBankCardActivityBinding.btSave.setEnabled(false);
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
