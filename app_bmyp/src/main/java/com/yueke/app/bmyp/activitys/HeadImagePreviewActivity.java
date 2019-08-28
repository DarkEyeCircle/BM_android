package com.yueke.app.bmyp.activitys;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.askia.common.base.ARouterPath;
import com.askia.common.base.BaseActivity;
import com.askia.common.util.ToastUtils;
import com.askia.common.util.Utils;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.yueke.app.bmyp.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@Route(path = ARouterPath.ActivityHeadImagePreview)
public class HeadImagePreviewActivity extends BaseActivity {

    public static final String INTENT_DATA_IMAGE_URL = "INTENT_DATA_IMAGE_URL";
    private SimpleDraweeView headImage;
    private String headUrl = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.head_image_preview_activity);
        QMUITopBar topBar = (QMUITopBar) findViewById(R.id.topBar);
        topBar.setTitle("头像").getPaint().setFakeBoldText(true);
        topBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Button bt_save = topBar.addRightTextButton("保存图片", R.id.bt_save);
        bt_save.setTextColor(Color.BLACK);
        bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });
        //解决返回键不居中问题
        topBar.setGravity(Gravity.CENTER_VERTICAL);
        headImage = findViewById(R.id.imgv_head);
        headImage.setImageURI(headUrl = getIntent().getStringExtra(INTENT_DATA_IMAGE_URL));
    }

    public void save() {
        showLogadingDialog();
        Observable.create(new SaveObservable(headUrl))
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

    private class SaveObservable implements ObservableOnSubscribe<String> {

        private String url = null;

        public SaveObservable(String url) {
            this.url = url;
        }

        @Override
        public void subscribe(ObservableEmitter<String> emitter) throws Exception {
            if (TextUtils.isEmpty(url)) {
                emitter.onError(new NullPointerException("url未null,请检查"));
            } else {
                try {
                    Bitmap drawingCache = Utils.ReturnBitmap(Uri.parse(url));
                    String fileName = "IMG_head" + System.currentTimeMillis() + ".jpg";
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
                        MediaStore.Images.Media.insertImage(HeadImagePreviewActivity.this.getContentResolver(),
                                imageFile.getAbsolutePath(), fileName, null);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    // 最后通知图库更新
                    HeadImagePreviewActivity.this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + filePath)));
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
}
