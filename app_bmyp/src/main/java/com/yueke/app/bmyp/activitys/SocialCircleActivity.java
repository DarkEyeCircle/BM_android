package com.yueke.app.bmyp.activitys;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.askia.common.base.ARouterPath;
import com.askia.common.base.BaseActivity;
import com.yueke.app.bmyp.R;
import com.yueke.app.bmyp.databinding.SocialCircleActivityBinding;

@Route(path = ARouterPath.ActivitySocialCircle)
public class SocialCircleActivity extends BaseActivity {

    private SocialCircleActivityBinding mSocialCircleActivityBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSocialCircleActivityBinding = DataBindingUtil.setContentView(this, R.layout.social_circle_activity);
        mSocialCircleActivityBinding.topBar.setTitle(R.string.social_circle).getPaint().setFakeBoldText(true);
        mSocialCircleActivityBinding.topBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //解决返回键不居中问题
        mSocialCircleActivityBinding.topBar.setGravity(Gravity.CENTER_VERTICAL);
    }
}
