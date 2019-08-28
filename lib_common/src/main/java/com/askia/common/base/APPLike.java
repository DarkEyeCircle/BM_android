package com.askia.common.base;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.multidex.MultiDex;

import com.alibaba.android.arouter.launcher.ARouter;
import com.apkfuns.logutils.LogUtils;
import com.askia.common.BuildConfig;
import com.askia.common.util.BuglyUtils;
import com.askia.common.util.Fresco_ImagePipelineConfigUtil;
import com.askia.common.util.Utils;
import com.askia.coremodel.datamodel.database.repository.DBRepository;
import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.bugly.beta.Beta;
import com.tencent.tinker.loader.app.DefaultApplicationLike;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

public class APPLike extends DefaultApplicationLike {

    public static final String TAG = "Tinker.MyApplicationLike";


    public APPLike(Application application, int tinkerFlags,
                   boolean tinkerLoadVerifyFlag, long applicationStartElapsedTime,
                   long applicationStartMillisTime, Intent tinkerResultIntent) {
        super(application, tinkerFlags, tinkerLoadVerifyFlag, applicationStartElapsedTime, applicationStartMillisTime, tinkerResultIntent);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Application sInstance = getApplication();
        if (LeakCanary.isInAnalyzerProcess(sInstance)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(sInstance);
        // 这里实现SDK初始化，appId替换成你的在Bugly平台申请的appId
        // 调试时，将第三个参数改为true
        BuglyUtils.init(sInstance);
        //初始化百度地图
        SDKInitializer.initialize(sInstance);
        SDKInitializer.setCoordType(CoordType.BD09LL);
        //初始化工具类
        Utils.init(sInstance);
        //初始化Arouter
        //注：一定要写在router init() 前面
        if (BuildConfig.DEBUG) {
            ARouter.openDebug();
            ARouter.openLog();
            //初始化LOG
            LogUtils.getLogConfig()
                    .configAllowLog(true)
                    .configTagPrefix("askia")
                    .configShowBorders(false)
                    .configFormatTag("%d{HH:mm:ss:SSS} %t %c{-5}");

        }
        ARouter.init(sInstance);
        //Fresco初始化
        Fresco.initialize(sInstance, Fresco_ImagePipelineConfigUtil.getDefaultImagePipelineConfig(sInstance));
        //二维码扫描初始化
        ZXingLibrary.initDisplayOpinion(sInstance);
        //DB 初始化
        DBRepository.init(sInstance);
    }


    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onBaseContextAttached(Context base) {
        super.onBaseContextAttached(base);
        // you must install multiDex whatever tinker is installed!
        MultiDex.install(base);
        // 安装tinker
        // TinkerManager.installTinker(this); 替换成下面Bugly提供的方法
        Beta.installTinker(this);
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void registerActivityLifecycleCallback(Application.ActivityLifecycleCallbacks callbacks) {
        getApplication().registerActivityLifecycleCallbacks(callbacks);
    }

}