package com.yueke.app.bmyp.activitys;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.askia.common.base.ARouterPath;
import com.askia.common.base.BaseActivity;
import com.askia.coremodel.viewmodel.WelcomViewModel;
import com.yueke.app.bmyp.R;

@Route(path = ARouterPath.ActivityWelcome)
public class WelcomeActivity extends BaseActivity {

    WelcomViewModel mWelcomViewModel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_activity);
        mWelcomViewModel = ViewModelProviders.of(WelcomeActivity.this).get(WelcomViewModel.class);
        ARouter.getInstance().build(ARouterPath.ActivityLogin).navigation();
        finish();
    }
}
