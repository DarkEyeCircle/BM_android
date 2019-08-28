package com.yueke.app.bmyp.activitys;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.askia.common.base.ARouterPath;
import com.askia.common.base.BaseActivity;
import com.askia.common.util.ToastUtils;
import com.askia.common.util.Utils;
import com.askia.common.widget.SpacesItemDecoration;
import com.askia.coremodel.GlobalUserDataStore;
import com.askia.coremodel.datamodel.http.entities.ProceedsData;
import com.askia.coremodel.datamodel.http.entities.QRUrlData;
import com.askia.coremodel.util.RxUtils;
import com.askia.coremodel.viewmodel.ProceedsViewModel;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.yueke.app.bmyp.R;
import com.yueke.app.bmyp.adapters.ProceedsAdapter;
import com.yueke.app.bmyp.databinding.ProceedsActivityBinding;
import com.yueke.app.bmyp.databinding.ProceedsBodyLayoutBinding;
import com.yueke.app.bmyp.widgets.ProceedsAmountDialog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

@Route(path = ARouterPath.ActivityProceeds)
public class ProceedsActivity extends BaseActivity {

    private ProceedsActivityBinding mProceedsActivityBinding;
    private ProceedsBodyLayoutBinding mProceedsBodyLayoutBinding;
    private ProceedsViewModel mProceedsViewModel;
    private final CompositeDisposable mDisposable = new CompositeDisposable();
    private ProceedsAmountDialog proceedsAmountDialog;
    private ProceedsAdapter mProceedsAdapter = null;
    private boolean isFirst = true;

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
        mProceedsActivityBinding = DataBindingUtil.setContentView(this, R.layout.proceeds_activity);
        mProceedsBodyLayoutBinding = DataBindingUtil.bind(LayoutInflater.from(this).inflate(R.layout.proceeds_body_layout, null));
    }

    private void initTopBar() {
        mProceedsActivityBinding.topBar.setTitle(R.string.proceeds).getPaint().setFakeBoldText(true);
        mProceedsActivityBinding.topBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //解决返回键不居中问题
        mProceedsActivityBinding.topBar.setGravity(Gravity.CENTER_VERTICAL);
    }

    private void initViewModel() {
        mProceedsViewModel = ViewModelProviders.of(this).get(ProceedsViewModel.class);
        subscribeToModel(mProceedsViewModel);
    }

    private void initData() {
        mProceedsActivityBinding.setHandlers(this);
        mProceedsBodyLayoutBinding.setHandlers(this);
        mProceedsViewModel.getProceedQRCode();
    }

    private void initViews() {
        mProceedsActivityBinding.dataView.setVisibility(View.GONE);
        mProceedsBodyLayoutBinding.titleLayout.setVisibility(View.GONE);
        //初始化Adapter
        mProceedsAdapter = new ProceedsAdapter(R.layout.item_proceeds_layout, new ArrayList<ProceedsData.ObjBean>());
        mProceedsAdapter.addHeaderView(mProceedsBodyLayoutBinding.getRoot());
        //开启动画(默认为渐显效果)
        mProceedsAdapter.openLoadAnimation();
        //设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mProceedsActivityBinding.dataView.setLayoutManager(layoutManager);
        //设置Item间距
        mProceedsActivityBinding.dataView.addItemDecoration(new SpacesItemDecoration(1));
        // 设置适配器
        mProceedsActivityBinding.dataView.setAdapter(mProceedsAdapter);

    }

    public void setMoney(View view) {
        if (proceedsAmountDialog == null) {
            proceedsAmountDialog = new ProceedsAmountDialog(this, new ProceedsAmountDialog.OnAmountInputFinishListener() {
                @Override
                public void inputFinish(String amount) {
                    if (Float.valueOf(amount) <= 0f) {
                        ToastUtils.info("请输入正确金额");
                        return;
                    }
                    proceedsAmountDialog.close();
                    mProceedsViewModel.getPageData().setMoney(amount);
                    showLogadingDialog();
                    mProceedsViewModel.getProceedQRCodeForSetMoney();
                }
            });
        }
        proceedsAmountDialog.showPopupWindow(findViewById(R.id.layoutContent));
    }

    public void clearMoney(View view) {
        showLogadingDialog();
        mProceedsViewModel.getProceedQRCode();
    }

    public void saveQRCode(View view) {
        showLogadingDialog();
        BitmapDrawable bitmapDrawable = (BitmapDrawable) mProceedsBodyLayoutBinding.imgvQRCode.getDrawable();
        Observable.create(new SaveObservable(bitmapDrawable.getBitmap()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.Observer<String>() {
                    Disposable disposable;

                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(String s) {
                        ToastUtils.info("保存成功，路径：" + s, Toast.LENGTH_LONG);
                    }

                    @Override
                    public void onError(Throwable e) {
                        disposable.dispose();
                        closeLogadingDialog();
                        ToastUtils.info("保存失败");
                    }

                    @Override
                    public void onComplete() {
                        disposable.dispose();
                        closeLogadingDialog();
                    }
                });
    }

    private void subscribeToModel(final ProceedsViewModel model) {
        //获取固定金额收款二维码
        model.getQRLiveDataForSetMoney().observe(this, new Observer<QRUrlData>() {
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
                Utils.getLogoForQR(GlobalUserDataStore.getInstance().getAvatar(), new BaseBitmapDataSubscriber() {
                    @Override
                    protected void onNewResultImpl(Bitmap bitmap) {

                        Utils.generateQRForPayment(responseData.getObj(), bitmap, new io.reactivex.Observer<Bitmap>() {

                            private Disposable disposable;

                            @Override
                            public void onSubscribe(Disposable d) {
                                disposable = d;
                            }

                            @Override
                            public void onNext(Bitmap bitmap) {
                                mProceedsViewModel.getPageData().setCodeId(responseData.parsing().getCodeId());
                                mProceedsBodyLayoutBinding.imgvQRCode.setImageBitmap(bitmap);
                                mProceedsBodyLayoutBinding.btSetMoney.setVisibility(View.GONE);
                                mProceedsBodyLayoutBinding.btClearMoney.setVisibility(View.VISIBLE);
                                mProceedsBodyLayoutBinding.moneyLayout.setVisibility(View.VISIBLE);
                                mProceedsBodyLayoutBinding.tvMoney.setText(mProceedsViewModel.getPageData().getMoney());
                                mDisposable.clear();
                                //开始轮训
                                mDisposable.add(RxUtils.CountDown(1, null, new Action() {
                                    @Override
                                    public void run() throws Exception {
                                        mProceedsViewModel.getProceedsState();
                                    }
                                }));
                            }

                            @Override
                            public void onError(Throwable e) {
                                disposable.dispose();
                                ToastUtils.info("生成二维码失败，请重试");
                            }

                            @Override
                            public void onComplete() {
                                disposable.dispose();
                            }
                        });
                    }

                    @Override
                    protected void onFailureImpl(DataSource<CloseableReference<CloseableImage>> dataSource) {
                        ToastUtils.info("生成二维码失败，请重试");
                    }
                });

            }
        });
        //获取收款二维码
        model.getQRLiveData().observe(this, new Observer<QRUrlData>() {
            @Override
            public void onChanged(@Nullable QRUrlData responseData) {
                closeLogadingDialog();
                if (responseData == null) {
                    mProceedsActivityBinding.emptyView.show(false, getResources().getString(R.string.emptyView_mode_desc_fail_desc), getResources().getString(R.string.emptyView_mode_desc_fail_desc), getResources().getString(R.string.emptyView_mode_desc_retry), retryAgainListener);
                    return;
                }
                if (responseData.isError() || null == responseData.getObj()) {
                    mProceedsActivityBinding.emptyView.show(false, getResources().getString(R.string.emptyView_mode_desc_fail_title), null, getResources().getString(R.string.emptyView_mode_desc_retry), retryAgainListener);
                    resolveResponseErrorCode(responseData.getCode(), responseData.getMsg());
                    return;
                }
                Utils.getLogoForQR(GlobalUserDataStore.getInstance().getAvatar(), new BaseBitmapDataSubscriber() {
                    @Override
                    protected void onNewResultImpl(Bitmap bitmap) {

                        Utils.generateQRForPayment(responseData.getObj(), bitmap, new io.reactivex.Observer<Bitmap>() {

                            private Disposable disposable;

                            @Override
                            public void onSubscribe(Disposable d) {
                                disposable = d;
                            }

                            @Override
                            public void onNext(Bitmap bitmap) {
                                mProceedsViewModel.getPageData().setCodeId(responseData.parsing().getCodeId());
                                if (isFirst) {
                                    mProceedsActivityBinding.emptyView.setLoadingShowing(false);
                                    mProceedsActivityBinding.dataView.setVisibility(View.VISIBLE);
                                    isFirst = false;
                                }
                                mProceedsBodyLayoutBinding.imgvQRCode.setImageBitmap(bitmap);
                                mProceedsBodyLayoutBinding.btSetMoney.setVisibility(View.VISIBLE);
                                mProceedsBodyLayoutBinding.btClearMoney.setVisibility(View.GONE);
                                mProceedsBodyLayoutBinding.moneyLayout.setVisibility(View.GONE);
                                //开始轮训
                                mDisposable.clear();
                                mDisposable.add(RxUtils.CountDown(1, null, new Action() {
                                    @Override
                                    public void run() throws Exception {
                                        mProceedsViewModel.getProceedsState();
                                    }
                                }));
                            }

                            @Override
                            public void onError(Throwable e) {
                                disposable.dispose();
                                e.printStackTrace();
                                if (isFirst) {
                                    mProceedsActivityBinding.emptyView.show(false, getResources().getString(R.string.emptyView_mode_desc_fail_title), null, getResources().getString(R.string.emptyView_mode_desc_retry), retryAgainListener);
                                    return;
                                }
                                ToastUtils.info("生成二维码失败，请重试");
                            }

                            @Override
                            public void onComplete() {
                                disposable.dispose();
                            }
                        });
                    }

                    @Override
                    protected void onFailureImpl(DataSource<CloseableReference<CloseableImage>> dataSource) {
                        if (isFirst) {
                            mProceedsActivityBinding.emptyView.show(false, getResources().getString(R.string.emptyView_mode_desc_fail_title), null, getResources().getString(R.string.emptyView_mode_desc_retry), retryAgainListener);
                            return;
                        }
                        ToastUtils.info("生成二维码失败，请重试");
                    }
                });

            }
        });
        //检查交易状态
        model.getPayStateLiveData().observe(this, new Observer<ProceedsData>() {
            @Override
            public void onChanged(@Nullable ProceedsData responseData) {
                if (responseData != null && !responseData.isError()) {
                    mProceedsBodyLayoutBinding.titleLayout.setVisibility(View.VISIBLE);
                    mProceedsAdapter.addData(responseData.getObj());
                }
                mDisposable.add(RxUtils.CountDown(1, null, new Action() {
                    @Override
                    public void run() throws Exception {
                        mProceedsViewModel.getProceedsState();
                    }
                }));
            }
        });
    }

    private View.OnClickListener retryAgainListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mProceedsActivityBinding.dataView.setVisibility(View.GONE);
            mProceedsActivityBinding.emptyView.show(true);
            mProceedsActivityBinding.emptyView.setTitleText("正在加载");
            mProceedsViewModel.getProceedQRCode();
        }
    };

    private class SaveObservable implements ObservableOnSubscribe<String> {

        private Bitmap drawingCache = null;

        public SaveObservable(Bitmap drawingCache) {
            this.drawingCache = drawingCache;
        }

        @Override
        public void subscribe(ObservableEmitter<String> emitter) throws Exception {
            if (drawingCache == null) {
                emitter.onError(new NullPointerException("imageview的bitmap获取为null,请确认imageview显示图片了"));
            } else {
                try {
                    String fileName = "IMG_QRCode" + System.currentTimeMillis() + ".jpg";
                    String filePath = Environment.getExternalStorageDirectory() + "/BMYP/pic/";
                    File file = new File(filePath);
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    File imageFile = new File(filePath, fileName);
                    if (imageFile.exists()) {
                        imageFile.delete();
                    }
                    FileOutputStream outStream;
                    outStream = new FileOutputStream(imageFile);
                    drawingCache.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
                    // 其次把文件插入到系统图库
                    try {
                        MediaStore.Images.Media.insertImage(ProceedsActivity.this.getContentResolver(),
                                imageFile.getAbsolutePath(), fileName, null);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    // 最后通知图库更新
                    ProceedsActivity.this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + filePath)));
                    emitter.onNext(imageFile.getAbsolutePath());
                    emitter.onComplete();
                    outStream.flush();
                    outStream.close();
                } catch (IOException e) {
                    emitter.onError(e);
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDisposable.clear();
    }
}
