package com.askia.common.base;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.apkfuns.logutils.LogUtils;
import com.askia.common.R;
import com.askia.common.util.ToastUtils;
import com.askia.common.util.Utils;
import com.askia.coremodel.datamodel.http.entities.ResponseCode;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.xiasuhuei321.loadingdialog.view.LoadingDialog;


/**
 * <p>Activity基类 </p>
 *
 * @name BaseActivity
 */
public abstract class BaseActivity extends AppCompatActivity {

    private LoadingDialog mLoadingDialog = null;

    /**
     * 封装的findViewByID方法
     */
    @SuppressWarnings("unchecked")
    protected <T extends View> T $(@IdRes int id) {
        return (T) super.findViewById(id);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.d(this.getClass().getSimpleName() + "---onCreate()");
        QMUIStatusBarHelper.translucent(this);
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        ViewManager.getInstance().addActivity(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ViewManager.getInstance().finishActivity(this);
        LogUtils.d(this.getClass().getSimpleName() + "---onDestroy()");
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    /**
     * Setup the toolbar.
     *
     * @param toolbar   toolbar
     * @param hideTitle 是否隐藏Title
     */
    protected void setupToolBar(Toolbar toolbar, boolean hideTitle) {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            if (hideTitle) {
                //隐藏Title
                actionBar.setDisplayShowTitleEnabled(false);
            }
        }
    }


    /**
     * 添加fragment
     *
     * @param fragment
     * @param frameId
     */
    protected void addFragment(BaseFragment fragment, @IdRes int frameId) {
        Utils.checkNotNull(fragment);
        getSupportFragmentManager().beginTransaction()
                .add(frameId, fragment, fragment.getClass().getSimpleName())
                .disallowAddToBackStack()
                .commitAllowingStateLoss();

    }


    /**
     * 替换fragment
     *
     * @param fragment
     * @param frameId
     */
    protected void replaceFragment(BaseFragment fragment, @IdRes int frameId) {
        Utils.checkNotNull(fragment);
        getSupportFragmentManager().beginTransaction()
                .replace(frameId, fragment, fragment.getClass().getSimpleName())
                .disallowAddToBackStack()
                .commitAllowingStateLoss();

    }


    /**
     * 隐藏fragment
     *
     * @param fragments
     */
    protected void hideFragment(BaseFragment... fragments) {
        Utils.checkNotNull(fragments);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        for (BaseFragment fragment : fragments) {
            if (fragment != null) {
                transaction.hide(fragment);
            }
        }
        transaction.commitAllowingStateLoss();
    }


    /**
     * 显示fragment
     *
     * @param fragment
     */
    protected void showFragment(BaseFragment fragment) {
        Utils.checkNotNull(fragment);
        getSupportFragmentManager().beginTransaction()
                .show(fragment)
                .commitAllowingStateLoss();

    }


    /**
     * 移除fragment
     *
     * @param fragment
     */
    protected void removeFragment(BaseFragment fragment) {
        Utils.checkNotNull(fragment);
        getSupportFragmentManager().beginTransaction()
                .remove(fragment)
                .commitAllowingStateLoss();

    }


    /**
     * 弹出栈顶部的Fragment
     */
    protected void popFragment() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }
    }

    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(R.anim.activity_down_in, R.anim.activity_down_out);
    }

    //显示Dialog
    public void showLogadingDialog(@Nullable String mes) {
        mLoadingDialog = new LoadingDialog(this);
        mLoadingDialog.setLoadingText(mes).show();
    }

    //显示Dialog
    public void showLogadingDialog() {
        showLogadingDialog(null);
    }

    public void closeLogadingDialog() {
        if (mLoadingDialog != null) {
            mLoadingDialog.close();
        }
    }

    public void resolveResponseErrorCode(int code, String mes) {
        if (code == ResponseCode.token_expire) {
            new QMUIDialog.MessageDialogBuilder(this)
                    .setTitle("登陆已过期")
                    .setCancelable(false)
                    .setCanceledOnTouchOutside(false)
                    .addAction("确定", new QMUIDialogAction.ActionListener() {
                        @Override
                        public void onClick(QMUIDialog dialog, int index) {
                            dialog.dismiss();
                            ViewManager.getInstance().finishAllOtherActivitys(BaseActivity.this);
                            ARouter.getInstance().build(ARouterPath.ActivityLogin).navigation();
                            finish();
                        }
                    })
                    .create(com.qmuiteam.qmui.R.style.QMUI_Dialog).show();
            return;
        }
        if (code == ResponseCode.OthersLogined) {
            new QMUIDialog.MessageDialogBuilder(this)
                    .setTitle("此账号已在其他设备登陆")
                    .setCancelable(false)
                    .setCanceledOnTouchOutside(false)
                    .addAction("确定", new QMUIDialogAction.ActionListener() {
                        @Override
                        public void onClick(QMUIDialog dialog, int index) {
                            dialog.dismiss();
                            ViewManager.getInstance().finishAllOtherActivitys(BaseActivity.this);
                            ARouter.getInstance().build(ARouterPath.ActivityLogin).navigation();
                            finish();
                        }
                    })
                    .create(com.qmuiteam.qmui.R.style.QMUI_Dialog).show();
            return;
        }
        if (code == ResponseCode.AccountLocked) {
            new QMUIDialog.MessageDialogBuilder(this)
                    .setTitle("此账号已被锁定")
                    .setCancelable(false)
                    .setCanceledOnTouchOutside(false)
                    .addAction("确定", new QMUIDialogAction.ActionListener() {
                        @Override
                        public void onClick(QMUIDialog dialog, int index) {
                            dialog.dismiss();
                            ViewManager.getInstance().finishAllOtherActivitys(BaseActivity.this);
                            ARouter.getInstance().build(ARouterPath.ActivityLogin).navigation();
                            finish();
                        }
                    })
                    .create(com.qmuiteam.qmui.R.style.QMUI_Dialog).show();
            return;
        }
        if (!TextUtils.isEmpty(mes)) {
            ToastUtils.info(mes);
        }
    }
}
