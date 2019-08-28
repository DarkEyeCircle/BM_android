package com.yueke.app.bmyp.activitys;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.askia.common.base.ARouterPath;
import com.askia.common.base.BaseActivity;
import com.askia.common.util.OpenLocalMapUtil;
import com.askia.common.util.ToastUtils;
import com.askia.coremodel.datamodel.http.entities.NearSotreData;
import com.askia.coremodel.datamodel.loaction.LocationData;
import com.askia.coremodel.util.RxUtils;
import com.askia.coremodel.viewmodel.StoreDetilsViewModel;
import com.baidu.location.BDLocation;
import com.yueke.app.bmyp.R;
import com.yueke.app.bmyp.databinding.StoreDetailsActivityBinding;

import io.reactivex.functions.Consumer;

@Route(path = ARouterPath.ActivityStoreDetails)
public class StoreDetailsActivity extends BaseActivity {

    public static final String INTENT_DATA = "INTENT_DATA";
    private StoreDetailsActivityBinding mStoreDetailsActivityBinding;
    private StoreDetilsViewModel mStoreDetilsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDataBinding();
        initTopBar();
        initViewModel();
        initData();
        initViews();
    }

    private void initDataBinding() {
        mStoreDetailsActivityBinding = DataBindingUtil.setContentView(this, R.layout.store_details_activity);
    }

    private void initTopBar() {
        mStoreDetailsActivityBinding.topBar.setTitle("门店详情").getPaint().setFakeBoldText(true);
        mStoreDetailsActivityBinding.topBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //解决返回键不居中问题
        mStoreDetailsActivityBinding.topBar.setGravity(Gravity.CENTER_VERTICAL);
    }

    private void initViewModel() {
        mStoreDetilsViewModel = ViewModelProviders.of(this).get(StoreDetilsViewModel.class);
    }


    private void initData() {
        mStoreDetailsActivityBinding.setHandlers(this);
        mStoreDetailsActivityBinding.setPageData(mStoreDetilsViewModel.getPageData());
        NearSotreData.ListBean listBean = getIntent().getParcelableExtra(INTENT_DATA);
        mStoreDetilsViewModel.init(listBean);
    }

    private void initViews() {
        mStoreDetailsActivityBinding.imgvStore.setImageURI(mStoreDetilsViewModel.getPageData().getLogoUrl());
    }

    public void jumpToMap(View view) {
        openBaiduMap();
    }

    public void openScanQR(View view) {
        RxUtils.RequestCameraPermissions(this, new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if (aBoolean) {
                    ARouter.getInstance().build(ARouterPath.ActivityScanQR).navigation();
                } else {
                }
            }
        });
    }

    /**
     * 打开百度地图
     */
    private void openBaiduMap() {
        String lat = mStoreDetilsViewModel.getPageData().getLat();
        String lon = mStoreDetilsViewModel.getPageData().getLon();
        String addr = mStoreDetilsViewModel.getPageData().getArea().get();
        String name = mStoreDetilsViewModel.getPageData().getName().get();
        if (OpenLocalMapUtil.isBaiduMapInstalled()) {
            try {
                String uri = OpenLocalMapUtil.getBaiduMapUri(lat, lon, addr,
                        String.valueOf(lat), String.valueOf(lon), name, addr, getResources().getString(R.string.app_name));
                Intent intent = Intent.parseUri(uri, 0);
                startActivity(intent); //启动调用
            } catch (Exception e) {
                e.printStackTrace();
                jumpToMark();
            }
        } else {
            jumpToMark();
        }
    }

    //跳转到应用市场
    private void jumpToMark() {
        /**(1)百度地图
         //market为路径，id为包名
         //显示手机上所有的market商店
         Toast.makeText(context, "您尚未安装百度地图", Toast.LENGTH_LONG).show();
         Uri uri = Uri.parse("market://details?id=com.baidu.BaiduMap");
         Intent intent = new Intent(Intent.ACTION_VIEW, uri);
         startActivity(intent);
         (2)高德地图
         Toast.makeText(context, "您尚未安装高德地图", Toast.LENGTH_LONG).show();
         Uri uri = Uri.parse("market://details?id=com.autonavi.minimap");
         Intent intent = new Intent(Intent.ACTION_VIEW, uri);
         startActivity(intent);
         (3)Google地图
         Toast.makeText(context, "您尚未安装谷歌地图", Toast.LENGTH_LONG).show();
         Uri uri = Uri.parse("market://details?id=com.google.android.apps.maps");
         Intent intent = new Intent(Intent.ACTION_VIEW, uri);
         startActivity(intent);*/
        try {
            ToastUtils.info("您尚未安装百度地图");
            Uri uri = Uri.parse("market://details?id=com.baidu.BaiduMap");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
