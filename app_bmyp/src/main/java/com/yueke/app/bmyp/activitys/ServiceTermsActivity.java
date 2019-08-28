package com.yueke.app.bmyp.activitys;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebSettings;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.askia.common.base.ARouterPath;
import com.askia.common.base.BaseActivity;
import com.yueke.app.bmyp.R;
import com.yueke.app.bmyp.databinding.ServiceTermsActivityBinding;

@Route(path = ARouterPath.ActivityServiceTerms)
public class ServiceTermsActivity extends BaseActivity {

    private ServiceTermsActivityBinding mServiceTermsActivityBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mServiceTermsActivityBinding = DataBindingUtil.setContentView(this, R.layout.service_terms_activity);
        mServiceTermsActivityBinding.setHandlers(this);
        mServiceTermsActivityBinding.topBar.setTitle(R.string.terms_of_service).getPaint().setFakeBoldText(true);
        mServiceTermsActivityBinding.topBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //解决返回键不居中问题
        mServiceTermsActivityBinding.topBar.setGravity(Gravity.CENTER_VERTICAL);
        WebSettings settings = mServiceTermsActivityBinding.webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setSupportZoom(false);
        settings.setBuiltInZoomControls(false);
        settings.setDisplayZoomControls(false);
        String url = "file:///android_asset/index.html";
        mServiceTermsActivityBinding.webView.loadUrl(url);
    }
}
