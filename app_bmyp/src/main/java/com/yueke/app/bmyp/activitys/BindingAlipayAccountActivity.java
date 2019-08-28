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
import com.askia.coremodel.GlobalUserDataStore;
import com.askia.coremodel.viewmodel.BindingAlipayAccountViewModel;
import com.yueke.app.bmyp.DataFormat;
import com.yueke.app.bmyp.R;
import com.yueke.app.bmyp.databinding.BindingAlipayAccountActivityBinding;

@Route(path = ARouterPath.ActivityBindingAlipayAccount)
public class BindingAlipayAccountActivity extends BaseActivity {

    private BindingAlipayAccountActivityBinding mBindingAlipayAccountActivityBinding = null;
    private BindingAlipayAccountViewModel mBindingAlipayAccountViewModel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDataBinding();
        initViewModel();
        initTopBar();
        initData();
    }

    private void initDataBinding() {
        mBindingAlipayAccountActivityBinding = DataBindingUtil.setContentView(this, R.layout.binding_alipay_account_activity);
    }

    private void initViewModel() {
        mBindingAlipayAccountViewModel = ViewModelProviders.of(this).get(BindingAlipayAccountViewModel.class);
        subscribeToModel(mBindingAlipayAccountViewModel);
    }

    private void initTopBar() {
        mBindingAlipayAccountActivityBinding.topBar.setTitle("绑定支付宝").getPaint().setFakeBoldText(true);
        //标题栏返回键监听
        mBindingAlipayAccountActivityBinding.topBar.addLeftBackImageButton().setOnClickListener(v -> finish());
        //解决返回键不居中问题
        mBindingAlipayAccountActivityBinding.topBar.setGravity(Gravity.CENTER_VERTICAL);
    }

    private void initData() {
        mBindingAlipayAccountActivityBinding.setHandlers(this);
        mBindingAlipayAccountActivityBinding.setPageData(mBindingAlipayAccountViewModel.getPageData());
    }

    public void doBind(View view) {
        showLogadingDialog();
        mBindingAlipayAccountViewModel.bindAlipay();
    }

    public void alipayAccountEdittextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() < DataFormat.AlipayAccountLeastLength) {
            mBindingAlipayAccountActivityBinding.btConfirm.setEnabled(false);
            return;
        }
        mBindingAlipayAccountActivityBinding.btConfirm.setEnabled(true);
    }

    private void subscribeToModel(final BindingAlipayAccountViewModel model) {
        //观察数据变化来刷新UI
        model.getBindLiveData().observe(this, responseData -> {
            closeLogadingDialog();
            if (responseData == null) {
                ToastUtils.showNetNoConnected();
                return;
            }
            if (responseData.isError()) {
                resolveResponseErrorCode(responseData.getCode(), responseData.getMsg());
                return;
            }
            ToastUtils.info("绑定成功");
            GlobalUserDataStore.getInstance().setAlipayAccount(mBindingAlipayAccountViewModel.getPageData().getAccount().get());
            finish();
        });
    }

}
