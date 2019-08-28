package com.yueke.app.bmyp.activitys;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.askia.common.base.ARouterPath;
import com.askia.common.base.BaseActivity;
import com.askia.common.util.ToastUtils;
import com.askia.common.util.Utils;
import com.askia.coremodel.datamodel.http.entities.ScanfQRCodeResult;
import com.askia.coremodel.util.RxUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.uuzuche.lib_zxing.activity.CaptureFragment;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.yueke.app.bmyp.R;
import com.yueke.app.bmyp.databinding.ScanOrActivityBinding;

import java.util.List;

import io.reactivex.MaybeObserver;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;

@Route(path = ARouterPath.ActivityScanQR)
public class ScanQRActivity extends BaseActivity {
    private ScanOrActivityBinding mScanOrActivityBinding;
    private boolean openBulb = false; //默认闪光灯为关闭状态
    private CaptureFragment captureFragment;
    private final CompositeDisposable mDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mScanOrActivityBinding = DataBindingUtil.setContentView(this, R.layout.scan_or_activity);
        mScanOrActivityBinding.setHandlers(this);
        initViews();
    }

    private void initViews() {

        /**
         *二维码扫描初始化
         * */
        //执行扫面Fragment的初始化操作
        captureFragment = new CaptureFragment();
        // 为二维码扫描界面设置定制化界面
        CodeUtils.setFragmentArgs(captureFragment, R.layout.scan_or_camera_layout);
        captureFragment.setAnalyzeCallback(analyzeCallback);
        //定制的扫描控件
        getSupportFragmentManager().beginTransaction().replace(mScanOrActivityBinding.fragmentContainer.getId(), captureFragment).commit();
    }

    /**
     * 二维码解析回调函数
     */
    CodeUtils.AnalyzeCallback analyzeCallback = new CodeUtils.AnalyzeCallback() {
        @Override
        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
            ScanfQRCodeResult qrCodeResult = ScanfQRCodeResult.parsing(result);
            if (qrCodeResult == null) {
                ToastUtils.info("扫描失败，请重试");
                restartPreviewAndDecode();
                return;
            }
            switch (qrCodeResult.getType()) {
                case 1:
                    ARouter.getInstance().build(ARouterPath.ActivityScanfProceeds).withParcelable(ScanfProceedsActivity.INTENT_DATA_RESULT, qrCodeResult).navigation();
                    finish();
                    break;
                case 2:
                    ARouter.getInstance().build(ARouterPath.ActivityScanfPayment).withParcelable(ScanfPaymentActivity.INTENT_DATA_RESULT, qrCodeResult).navigation();
                    finish();
                    break;
                case 4:
                    ARouter.getInstance().build(ARouterPath.ActivityScanfBecomeFans).withParcelable(ScanfPaymentActivity.INTENT_DATA_RESULT, qrCodeResult).navigation();
                    finish();
                    break;
                default:
                    ToastUtils.info("扫描失败，请重试");
                    restartPreviewAndDecode();
                    break;
            }

        }

        @Override
        public void onAnalyzeFailed() {
        }
    };

    //返回
    public void jumpToBack(View view) {
        finish();
    }

    //打开图库
    public void openGallery(View view) {
        PictureSelector.create(ScanQRActivity.this)
                .openGallery(PictureMimeType.ofImage())
                .selectionMode(PictureConfig.SINGLE)
                .imageSpanCount(4)
                .previewImage(true)
                .isCamera(true)
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }

    //打卡/关闭闪光灯
    public void switchBulb(View view) {
        if (openBulb) {
            CodeUtils.isLightEnable(false);
            openBulb = false;
            return;
        }
        CodeUtils.isLightEnable(true);
        openBulb = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        CodeUtils.isLightEnable(false);
        openBulb = false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true  注意：音视频除外
                    // posters_bg.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true  注意：音视频除外
                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
                    // 图片、视频、音频选择结果回调
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    Utils.DecodeQRCode(selectList.get(0).getPath(), new MaybeObserver<ScanfQRCodeResult>() {
                        Disposable disposable;

                        @Override
                        public void onSubscribe(Disposable d) {
                            disposable = d;
                        }

                        @Override
                        public void onSuccess(ScanfQRCodeResult result) {
                            if (result == null) {
                                ToastUtils.info("解析失败，请重试");
                                return;
                            }
                            switch (result.getType()) {
                                case 1:
                                    ARouter.getInstance().build(ARouterPath.ActivityScanfProceeds).withParcelable(ScanfProceedsActivity.INTENT_DATA_RESULT, result).navigation();
                                    finish();
                                    break;
                                case 2:
                                    ARouter.getInstance().build(ARouterPath.ActivityScanfPayment).withParcelable(ScanfProceedsActivity.INTENT_DATA_RESULT, result).navigation();
                                    finish();
                                    break;
                                case 4:
                                    ARouter.getInstance().build(ARouterPath.ActivityScanfBecomeFans).withParcelable(ScanfPaymentActivity.INTENT_DATA_RESULT, result).navigation();
                                    finish();
                                    break;
                                default:
                                    ToastUtils.info("解析失败，请重试");
                                    break;
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            disposable.dispose();
                            e.printStackTrace();
                            ToastUtils.info("解析失败，请重试");
                        }

                        @Override
                        public void onComplete() {
                            disposable.dispose();
                            ToastUtils.info("解析失败，请重试");
                        }
                    });
                    break;
            }
        }
    }

    public void restartPreviewAndDecode() {
        mDisposable.add(RxUtils.CountDown(2, null, new Action() {
            @Override
            public void run() throws Exception {
                captureFragment.restartPreviewAndDecode();
            }
        }));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDisposable.clear();
    }

}
