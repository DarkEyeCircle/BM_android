package com.yueke.app.bmyp.activitys;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.askia.common.base.ARouterPath;
import com.askia.common.base.BaseActivity;
import com.askia.common.base.ViewManager;
import com.askia.common.util.ToastUtils;
import com.askia.coremodel.GlobalUserDataStore;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.UpgradeInfo;
import com.yueke.app.bmyp.R;
import com.yueke.app.bmyp.databinding.SettingCenterActivityBinding;

@Route(path = ARouterPath.ActivitySettingCenter)
public class SettingCenterActivity extends BaseActivity {

    private SettingCenterActivityBinding mSettingCenterActivityBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDataBinding();
        initTopBar();
        initData();
    }

    private void initDataBinding() {
        mSettingCenterActivityBinding = DataBindingUtil.setContentView(SettingCenterActivity.this, R.layout.setting_center_activity);
    }

    private void initTopBar() {
        mSettingCenterActivityBinding.topBar.setTitle(R.string.setting).getPaint().setFakeBoldText(true);
        mSettingCenterActivityBinding.topBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //解决返回键不居中问题
        mSettingCenterActivityBinding.topBar.setGravity(Gravity.CENTER_VERTICAL);
    }

    private void initData() {
        mSettingCenterActivityBinding.setHandlers(this);
    }

    //语言切换
    public void jumpToSwitchLanguage(View view) {
        ToastUtils.info(getResources().getString(R.string.function_not_open), Toast.LENGTH_SHORT);
    }

    //个人信息管理
    public void jumpToPersonalInfoManager(View view) {
        ARouter.getInstance().build(ARouterPath.ActivityMyInformation).navigation();
    }

    //账号管理
    public void jumpToAccountManager(View view) {
        ARouter.getInstance().build(ARouterPath.ActivityAccountManager).navigation();
    }

    //密码管理
    public void jumpToPwdManager(View view) {
        ARouter.getInstance().build(ARouterPath.ActivityPwdManager).navigation();
    }

    //切换账号
    public void jumpToSwitchAccount(View view) {
        ARouter.getInstance().build(ARouterPath.ActivitySwitchAccount).navigation();
    }

    //检查更新
    public void jumpToCheckUpdate(View view) {
        /***** 获取升级信息 *****/
        UpgradeInfo upgradeInfo = Beta.getUpgradeInfo();
        if (upgradeInfo == null) {
            ToastUtils.info("已经是最新版本");
            return;
        }
        Beta.checkUpgrade(false, false);
    }

    //退出
    public void jumpToExit(View view) {
        new QMUIDialog.MessageDialogBuilder(SettingCenterActivity.this)
                .setTitle("提示")
                .setMessage("确定退出该账号？")
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
                        GlobalUserDataStore.getInstance().clearUserInfo();
                        ViewManager.getInstance().finishAllOtherActivitys(SettingCenterActivity.this);
                        ARouter.getInstance().build(ARouterPath.ActivityLogin).navigation();
                        finish();
                    }
                })
                .create(com.qmuiteam.qmui.R.style.QMUI_Dialog).show();
    }

}
