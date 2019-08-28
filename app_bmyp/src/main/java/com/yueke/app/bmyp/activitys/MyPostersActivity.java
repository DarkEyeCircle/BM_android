package com.yueke.app.bmyp.activitys;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.apkfuns.logutils.LogUtils;
import com.askia.common.base.ARouterPath;
import com.askia.common.base.BaseActivity;
import com.askia.common.util.ToastUtils;
import com.askia.common.util.Utils;
import com.askia.coremodel.GlobalUserDataStore;
import com.askia.coremodel.datamodel.http.entities.QRUrlData;
import com.askia.coremodel.datamodel.http.entities.ScanfQRCodeResult;
import com.askia.coremodel.viewmodel.MyPosterViewModel;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.yueke.app.bmyp.R;
import com.yueke.app.bmyp.databinding.MyPostersActivityBinding;

import io.reactivex.disposables.Disposable;

@Route(path = ARouterPath.ActivityMyPosters)
public class MyPostersActivity extends BaseActivity {

    private MyPostersActivityBinding mMyPostersActivityBinding = null;
    private MyPosterViewModel mMyPosterViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDataBinding();
        initViewModel();
        initTopBar();
        initData();
    }

    private void initDataBinding() {
        mMyPostersActivityBinding = DataBindingUtil.setContentView(MyPostersActivity.this, R.layout.my_posters_activity);
    }

    private void initViewModel() {
        mMyPosterViewModel = ViewModelProviders.of(this).get(MyPosterViewModel.class);
        subscribeToModel(mMyPosterViewModel);
    }

    private void initTopBar() {
        mMyPostersActivityBinding.topBar.setTitle(R.string.my_posters).getPaint().setFakeBoldText(true);
        mMyPostersActivityBinding.topBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });
        //解决返回键不居中问题
        mMyPostersActivityBinding.topBar.setGravity(Gravity.CENTER_VERTICAL);
    }


    private void initData() {
        mMyPostersActivityBinding.imgvHead.setImageURI(GlobalUserDataStore.getInstance().getAvatar());
        mMyPostersActivityBinding.tvNickName.setText(GlobalUserDataStore.getInstance().getNickName());
        String introducer = GlobalUserDataStore.getInstance().getIntroduceCode();
        mMyPostersActivityBinding.tvIntroduceCode.setText(introducer);
        mMyPosterViewModel.getQrCode();
        showLogadingDialog();
    }

    private void subscribeToModel(final MyPosterViewModel model) {

        model.getQRLiveData().observe(this, new Observer<QRUrlData>() {
            @Override
            public void onChanged(@Nullable QRUrlData responseData) {
                closeLogadingDialog();
                if (responseData == null) {
                    ToastUtils.showNetNoConnected();
                    return;
                }
                if (responseData.isError()) {
                    resolveResponseErrorCode(responseData.getCode(), responseData.getMsg());
                    return;
                }
                String url = GlobalUserDataStore.getInstance().getAvatar();
                Utils.getLogoForQR(url, new BaseBitmapDataSubscriber() {
                    @Override
                    protected void onNewResultImpl(Bitmap bitmap) {
                        ScanfQRCodeResult qrCodeResult = ScanfQRCodeResult.parsing(responseData.getObj());
                        LogUtils.d("qrCodeResult:" + qrCodeResult.toString());
                        Utils.generateQRForPayment(responseData.getObj(), bitmap, new io.reactivex.Observer<Bitmap>() {

                            private Disposable disposable;

                            @Override
                            public void onSubscribe(Disposable d) {
                                disposable = d;
                            }

                            @Override
                            public void onNext(Bitmap bitmap) {
                                mMyPostersActivityBinding.imgvQRCode.setImageBitmap(bitmap);
                            }

                            @Override
                            public void onError(Throwable e) {
                                disposable.dispose();
                                ToastUtils.info("生成二维码失败");
                            }

                            @Override
                            public void onComplete() {
                                disposable.dispose();
                            }
                        });
                    }

                    @Override
                    protected void onFailureImpl(DataSource<CloseableReference<CloseableImage>> dataSource) {
                        ToastUtils.info("生成二维码失败");
                    }
                });

            }
        });
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        finish();
        super.onBackPressed();
    }
}
