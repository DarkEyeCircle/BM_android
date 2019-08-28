package com.askia.common.util;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.MainThread;
import android.widget.Toast;

import com.askia.common.R;

import es.dmoral.toasty.Toasty;

/**
 * Toast相关工具类
 */
public class ToastUtils {

    private static Context context;

    private ToastUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 初始化工具类
     *
     * @param context 上下文
     */
    public static void init(Context context) {
        ToastUtils.context = context.getApplicationContext();
        //Toast 初始化
        Toasty.Config.getInstance()
                .setErrorColor(ToastUtils.context.getResources().getColor(R.color.lighter_gray))
                .setInfoColor(ToastUtils.context.getResources().getColor(R.color.lighter_gray))
                .setSuccessColor(ToastUtils.context.getResources().getColor(R.color.lighter_gray))
                .setWarningColor(ToastUtils.context.getResources().getColor(R.color.lighter_gray))
                .setTextColor(ToastUtils.context.getResources().getColor(R.color.lighter_gray))
                .tintIcon(false)
                .setToastTypeface(Typeface.DEFAULT)
                .setTextSize(12)
                .setTextColor(ToastUtils.context.getResources().getColor(R.color.white))
                .apply();
    }

    @MainThread
    public static void error(String mes, int duration) {
        Toasty.error(ToastUtils.context, mes, duration, false).show();
    }

    @MainThread
    public static void success(String mes, int duration) {
        Toasty.success(ToastUtils.context, mes, duration, false).show();
    }

    @MainThread
    public static void info(String mes, int duration) {
        Toasty.info(ToastUtils.context, mes, duration, false).show();
    }

    @MainThread
    public static void info(String mes) {
        info(mes, Toast.LENGTH_SHORT);
    }

    @MainThread
    public static void warning(String mes, int duration) {
        Toasty.warning(ToastUtils.context, mes, duration, false).show();
    }

    @MainThread
    public static void normal(String mes, int duration) {
        Toasty.normal(ToastUtils.context, mes).show();
    }

    @MainThread
    public static void showNetNoConnected() {
        Toasty.normal(ToastUtils.context, "网络未连接，请检查网络").show();
    }

}