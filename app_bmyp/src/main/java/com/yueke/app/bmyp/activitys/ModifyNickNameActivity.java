package com.yueke.app.bmyp.activitys;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.askia.common.base.ARouterPath;
import com.askia.common.base.BaseActivity;
import com.askia.common.util.ToastUtils;
import com.askia.coremodel.GlobalUserDataStore;
import com.askia.coremodel.viewmodel.ModifyNickNameViewModel;
import com.yueke.app.bmyp.DataFormat;
import com.yueke.app.bmyp.R;
import com.yueke.app.bmyp.databinding.ModifyNickNameActivityBinding;

@Route(path = ARouterPath.ActivityModifyNickName)
public class ModifyNickNameActivity extends BaseActivity {

    private ModifyNickNameViewModel mModifyNickNameViewModel;
    private ModifyNickNameActivityBinding mModifyNickNameActivityBinding = null;
    private Button btSave = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDataBinding();
        initTopBar();
        initViewModel();
        initData();
    }

    private void initDataBinding() {
        mModifyNickNameActivityBinding = DataBindingUtil.setContentView(this, R.layout.modify_nick_name_activity);
    }

    private void initTopBar() {
        mModifyNickNameActivityBinding.topBar.setTitle(R.string.modify_nick_name).getPaint().setFakeBoldText(true);
        //标题栏返回键监听
        mModifyNickNameActivityBinding.topBar.addLeftBackImageButton().setOnClickListener(v -> finish());
        btSave = mModifyNickNameActivityBinding.topBar.addRightTextButton(getResources().getString(R.string.save), R.id.bt_save);
        btSave.setTextColor(getResources().getColor(R.color.line_color));
        btSave.setEnabled(false);
        btSave.setOnClickListener(view -> {
            if (mModifyNickNameViewModel.getPageData().getNickName().get().length() <= DataFormat.NickNameLeastLength) {
                ToastUtils.info("昵称过短", Toast.LENGTH_SHORT);
                return;
            }
            if (GlobalUserDataStore.getInstance().getNickName().equals(mModifyNickNameViewModel.getPageData().getNickName().get())) {
                ToastUtils.info("修改成功");
                finish();
                return;
            }
            showLogadingDialog();
            mModifyNickNameViewModel.modifyHeadImg(mModifyNickNameViewModel.getPageData().getNickName().get());
        });
        //解决返回键不居中问题
        mModifyNickNameActivityBinding.topBar.setGravity(Gravity.CENTER_VERTICAL);
    }

    private void initViewModel() {
        mModifyNickNameViewModel = ViewModelProviders.of(this).get(ModifyNickNameViewModel.class);
        subscribeToModel(mModifyNickNameViewModel);
    }

    private void initData() {
        mModifyNickNameActivityBinding.setHandlers(this);
        mModifyNickNameActivityBinding.setPageData(mModifyNickNameViewModel.getPageData());
    }

    public void userNickNameEdittextChanged(CharSequence s, int start, int before, int count) {
        mModifyNickNameViewModel.getPageData().getInputConut().set(s.toString().length() + "");
        if (s.length() > DataFormat.NickNameLeastLength) {
            btSave.setEnabled(true);
            btSave.setTextColor(getResources().getColor(R.color.app_color_blue_2));
        } else {
            btSave.setEnabled(false);
            btSave.setTextColor(getResources().getColor(R.color.line_color));
        }
    }

    private void subscribeToModel(final ModifyNickNameViewModel model) {
        //修改昵称
        model.getModifyNickNameResponseData().observe(this, responseData -> {
            closeLogadingDialog();
            if (responseData == null) {
                ToastUtils.showNetNoConnected();
                return;
            }
            if (responseData.isError()) {
                resolveResponseErrorCode(responseData.getCode(), responseData.getMsg());
                return;
            }
            model.update(responseData.getObj());
            ToastUtils.info("修改成功");
            finish();
        });
    }

}
