package com.yueke.app.bmyp.activitys;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.alibaba.android.arouter.launcher.ARouter;
import com.askia.common.base.ARouterPath;
import com.askia.common.base.BaseActivity;
import com.askia.common.util.Utils;
import com.askia.coremodel.datamodel.database.db.UserLoginData;
import com.askia.coremodel.datamodel.database.repository.DBRepository;
import com.askia.coremodel.datamodel.loaction.LocationData;
import com.askia.coremodel.util.RxUtils;
import com.askia.coremodel.viewmodel.LauncherViewModel;
import com.yueke.app.bmyp.R;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class LauncherActivity extends BaseActivity {

    private static final int DelayTime = 2; //2秒
    private Disposable mDisposable;
    private LauncherViewModel mLauncherViewModel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //解决在安装应用之后点击了打开 然后按home键 这个时候应用程序进入后台 点击手机桌面的时候图片启动应用程序 程序重新再次启动
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
        setContentView(R.layout.launcher_activity);
        //请求定位权限
        RxUtils.RequestLocationPermisoon(LauncherActivity.this, new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if (aBoolean) {
                    LocationData.init(Utils.getContext());
                }
                initViewModel();
            }
        });
    }

    //初始化ViewModel
    private void initViewModel() {
        mLauncherViewModel = ViewModelProviders.of(LauncherActivity.this).get(LauncherViewModel.class);
        subscribeToModel(mLauncherViewModel);
    }

    //订阅数据变化来刷新UI
    private void subscribeToModel(final LauncherViewModel model) {
        //观察数据变化来刷新UI
        model.getmLiveUserLoginData().observe(this, new android.arch.lifecycle.Observer<UserLoginData>() {
            @Override
            public void onChanged(@Nullable UserLoginData userLoginData) {
                jumpPage(userLoginData.isShowWelcomPage());
            }
        });
    }

    //页面跳转
    private void jumpPage(final boolean showWelcom) {
        mDisposable = RxUtils.CountDown(DelayTime, null, new Action() {
            @Override
            public void run() throws Exception {
                if (showWelcom) {
                    ARouter.getInstance().build(ARouterPath.ActivityWelcome).navigation();
                } else {
                    UserLoginData userLoginData = DBRepository.QueryUserLoginData();
                    String phoneNum = userLoginData.getPhoneNum();
                    String pwd = userLoginData.getUserPwd();
                    if (TextUtils.isEmpty(phoneNum) || TextUtils.isEmpty(pwd)) {
                        ARouter.getInstance().build(ARouterPath.ActivityLogin).navigation();
                    } else {
                        ARouter.getInstance().build(ARouterPath.ActivityMain)
                                .withBoolean(MainActivity.Intent_Auto_Login, true)
                                .navigation();
                    }
                }
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
