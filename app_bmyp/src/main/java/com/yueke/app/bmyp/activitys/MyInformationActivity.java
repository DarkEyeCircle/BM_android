package com.yueke.app.bmyp.activitys;

import android.app.Application;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.apkfuns.logutils.LogUtils;
import com.askia.common.base.ARouterPath;
import com.askia.common.base.BaseActivity;
import com.askia.common.util.ToastUtils;
import com.askia.common.util.Utils;
import com.askia.coremodel.GlobalUserDataStore;
import com.askia.coremodel.datamodel.http.entities.ResponseCode;
import com.askia.coremodel.datamodel.http.entities.User;
import com.askia.coremodel.datamodel.http.repository.GankDataRepository;
import com.askia.coremodel.viewmodel.MyInformationViewModel;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.yueke.app.bmyp.R;
import com.yueke.app.bmyp.databinding.MyInformationActivityBinding;

import java.io.File;
import java.util.Calendar;
import java.util.List;

import io.reactivex.MaybeObserver;
import io.reactivex.disposables.Disposable;

@Route(path = ARouterPath.ActivityMyInformation)
public class MyInformationActivity extends BaseActivity implements DatePickerDialog.OnDateSetListener {

    private MyInformationActivityBinding mMyInformationActivityBinding = null;
    private MyInformationViewModel mMyInformationViewModel = null;
    private DatePickerDialog datePickerDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDataBinding();
        initTopBar();
        initViewModel();
        initData();
        initDataPickerDialog();
    }

    private void initDataPickerDialog() {
        Calendar now = Calendar.getInstance();
        datePickerDialog = DatePickerDialog.newInstance(
                MyInformationActivity.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.setThemeDark(true);
        datePickerDialog.setAccentColor(Color.parseColor("#00A8E1"));
    }

    private void initDataBinding() {
        mMyInformationActivityBinding = DataBindingUtil.setContentView(MyInformationActivity.this, R.layout.my_information_activity);
    }

    private void initTopBar() {
        mMyInformationActivityBinding.topBar.setTitle(R.string.person_info).getPaint().setFakeBoldText(true);
        mMyInformationActivityBinding.topBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //解决返回键不居中问题
        mMyInformationActivityBinding.topBar.setGravity(Gravity.CENTER_VERTICAL);
    }

    private void initViewModel() {
        mMyInformationViewModel = ViewModelProviders.of(MyInformationActivity.this).get(MyInformationViewModel.class);
        subscribeToModel(mMyInformationViewModel);
    }

    private void initData() {
        mMyInformationActivityBinding.setHandlers(MyInformationActivity.this);
        mMyInformationActivityBinding.setPageData(mMyInformationViewModel.getPageData());
        mMyInformationActivityBinding.imgvHead.setImageURI(GlobalUserDataStore.getInstance().getAvatar());
    }

    //修改头像
    public void jumpToModifyHeadImg(View view) {

        new QMUIBottomSheet.BottomListSheetBuilder(MyInformationActivity.this)
                .addItem("拍照")
                .addItem("相册")
                .setOnSheetItemClickListener(new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                    @Override
                    public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                        dialog.dismiss();
                        if (position == 0) {
                            Utils.jumpToCameraAndCut(MyInformationActivity.this, 1, 1);
                        } else if (position == 1) {
                            Utils.jumpToGalleryAndCut(MyInformationActivity.this, 1, 1);
                        }
                    }
                })
                .build()
                .show();
    }

    //修改昵称
    public void jumpToModifyNickName(View view) {
        ARouter.getInstance().build(ARouterPath.ActivityModifyNickName).navigation();
    }

    //查看头像
    public void lookHead(View view) {
        ARouter.getInstance().build(ARouterPath.ActivityHeadImagePreview)
                .withString(HeadImagePreviewActivity.INTENT_DATA_IMAGE_URL, GlobalUserDataStore.getInstance().getAvatar()).navigation();
    }

    //修改性别
    public void jumpToModifySex(View view) {
        new QMUIBottomSheet.BottomListSheetBuilder(MyInformationActivity.this)
                .addItem("男")
                .addItem("女")
                .setOnSheetItemClickListener(new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                    @Override
                    public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                        dialog.dismiss();
                        if (position == 0) {
                            //选择男
                            if (GlobalUserDataStore.getInstance().getDictSex() == ResponseCode.SEX_MAN) {
                                ToastUtils.info("修改成功");
                                return;
                            }
                            showLogadingDialog();
                            mMyInformationViewModel.modifySex(ResponseCode.SEX_MAN);
                        } else if (position == 1) {
                            if (GlobalUserDataStore.getInstance().getDictSex() == ResponseCode.SEX_WOMAN) {
                                ToastUtils.info("修改成功");
                                return;
                            }
                            showLogadingDialog();
                            //选择女
                            mMyInformationViewModel.modifySex(ResponseCode.SEX_WOMAN);
                        }
                    }
                })
                .build()
                .show();
    }

    //修改出生日期
    public void jumpToModifyBornDate(View view) {
        datePickerDialog.show(getFragmentManager(), "Datepickerdialog");
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMyInformationViewModel.getPageData().update();
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String dateStr = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
        if (GlobalUserDataStore.getInstance().getBirthday().equals(dateStr)) {
            ToastUtils.info("修改成功");
            return;
        }
        showLogadingDialog();
        mMyInformationViewModel.modifyBornData(dateStr);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void subscribeToModel(final MyInformationViewModel model) {
        //修改头像
        model.getModifyHeadImgResponseData().observe(this, responseData -> {
            closeLogadingDialog();
            if (responseData == null) {
                ToastUtils.showNetNoConnected();
                return;
            }
            if (responseData.isError()) {
                resolveResponseErrorCode(responseData.getCode(), responseData.getMsg());
                return;
            }
            model.update(responseData.getObj());
            mMyInformationActivityBinding.imgvHead.setImageURI(GlobalUserDataStore.getInstance().getAvatar());
            /*  mMyInformationActivityBinding.imgvHead.setImageURI(mMyInformationViewModel.getPageData().getUrl());*/
            ToastUtils.info("头像修改成功");
        });

        //修改性别
        model.getModifySexResponseData().observe(this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable User responseData) {
                closeLogadingDialog();
                if (responseData == null) {
                    ToastUtils.showNetNoConnected();
                    return;
                }
                if (responseData.isError()) {
                    resolveResponseErrorCode(responseData.getCode(), responseData.getMsg());
                    return;
                }
                model.update(responseData.getObj());
                ToastUtils.info("修改成功");
            }
        });

        //修改出生日期
        model.getModifyBornDataResponseData().observe(this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable User responseData) {
                closeLogadingDialog();
                if (responseData == null) {
                    ToastUtils.showNetNoConnected();
                    return;
                }
                if (responseData.isError()) {
                    resolveResponseErrorCode(responseData.getCode(), responseData.getMsg());
                    return;
                }
                model.update(responseData.getObj());
                ToastUtils.info("修改成功");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    LogUtils.d("cutPath:" + selectList.get(0).getCompressPath());
                    upLoadHead(selectList.get(0).getCutPath());
            }
        }
    }

    public void upLoadHead(String filePath) {
        showLogadingDialog();
//        if (!TextUtils.isEmpty(mMyInformationViewModel.getPageData().getUrl())) { //已在阿里云上传
//            mMyInformationViewModel.modifyHeadImg();
//            return;
//        }
        File file = new File(filePath);
        if (file.length() > 1024 * 1024 * 1) { //不允许超过500K
            closeLogadingDialog();
            ToastUtils.info("图片过大，请重新选择");
            return;
        }
        GankDataRepository.UploadHead(filePath, (Application) Utils.getContext(), new MaybeObserver<String>() {

            private Disposable disposable;

            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
            }

            @Override
            public void onSuccess(String str) {
                mMyInformationViewModel.getPageData().setUrl(str);
                mMyInformationViewModel.modifyHeadImg();
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                disposable.dispose();
                closeLogadingDialog();
                ToastUtils.info("头像上传失败");
            }

            @Override
            public void onComplete() {
                disposable.dispose();
            }
        });
    }

}
