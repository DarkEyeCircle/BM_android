package com.yueke.app.bmyp.activitys;

import android.app.Application;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.askia.common.base.ARouterPath;
import com.askia.common.base.BaseActivity;
import com.askia.common.util.ToastUtils;
import com.askia.common.util.Utils;
import com.askia.coremodel.GlobalUserDataStore;
import com.askia.coremodel.datamodel.http.entities.User;
import com.askia.coremodel.datamodel.http.repository.GankDataRepository;
import com.askia.coremodel.util.RxUtils;
import com.askia.coremodel.viewmodel.IdentityApproveViewModel;
import com.lljjcoder.Interface.OnCityItemClickListener;
import com.lljjcoder.bean.CityBean;
import com.lljjcoder.bean.DistrictBean;
import com.lljjcoder.bean.ProvinceBean;
import com.lljjcoder.citywheel.CityConfig;
import com.lljjcoder.style.citypickerview.CityPickerView;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.yueke.app.bmyp.DataFormat;
import com.yueke.app.bmyp.R;
import com.yueke.app.bmyp.databinding.IdentityApproveActivityBinding;

import java.io.File;
import java.util.List;

import io.reactivex.MaybeObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

import static com.askia.common.util.InputTools.HideKeyboard;

/**
 * 实名认证
 */
@Route(path = ARouterPath.ActivityIdentityApprove)
public class IdentityApproveActivity extends BaseActivity {

    private static final int Request_Code_IDCard_Front = 0x110;
    private static final int Request_Code_IDCard_Behind = 0x111;

    private IdentityApproveActivityBinding mIdentityApproveActivityBinding = null;
    private IdentityApproveViewModel mIdentityApproveViewModel;
    private CityPickerView mCityPickerView = null;
    private Disposable mDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDataBinding();
        initTopBar();
        initViewModel();
        initData();
        initCityPickerView();
    }

    private void initDataBinding() {
        mIdentityApproveActivityBinding = DataBindingUtil.setContentView(this, R.layout.identity_approve_activity);
    }

    private void initTopBar() {
        mIdentityApproveActivityBinding.topBar.setTitle(R.string.binding_information).getPaint().setFakeBoldText(true);
        mIdentityApproveActivityBinding.topBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //解决返回键不居中问题
        mIdentityApproveActivityBinding.topBar.setGravity(Gravity.CENTER_VERTICAL);
    }

    private void initViewModel() {
        mIdentityApproveViewModel = ViewModelProviders.of(IdentityApproveActivity.this).get(IdentityApproveViewModel.class);
        subscribeToModel(mIdentityApproveViewModel);
    }

    private void initData() {
        mIdentityApproveActivityBinding.setHandlers(this);
        mIdentityApproveActivityBinding.setPageData(mIdentityApproveViewModel.getPageData());

    }

    private void initCityPickerView() {
        mCityPickerView = new CityPickerView();
        mCityPickerView.init(this);
        CityConfig cityConfig = new CityConfig.Builder().build();
        mCityPickerView.setConfig(cityConfig);

        //监听选择点击事件及返回结果
        mCityPickerView.setOnCityItemClickListener(new OnCityItemClickListener() {
            @Override
            public void onSelected(ProvinceBean province, CityBean city, DistrictBean district) {
                String cityStr = "";
                //省份
                if (province != null && !TextUtils.isEmpty(province.getName())) {
                    cityStr = cityStr + province.getName();
                }
                //城市
                if (city != null && !TextUtils.isEmpty(city.getName())) {
                    cityStr = cityStr + city.getName();
                }
                //地区
                if (district != null && !TextUtils.isEmpty(district.getName())) {
                    cityStr = cityStr + district.getName();
                }
                mIdentityApproveViewModel.getPageData().getCurCity().set(cityStr);
            }

            @Override
            public void onCancel() {
            }
        });
    }

    //获取验证码
    public void getVerificationCode(View view) {
        mIdentityApproveViewModel.senSmsCode();
    }

    //选择城市
    public void doSelectCity(View view) {
        HideKeyboard(view);
        mCityPickerView.showCityPicker();
    }

    //开始认证
    public void doCertification(View view) {
        if (TextUtils.isEmpty(mIdentityApproveViewModel.getPageData().getIdCardImg_front())
                || TextUtils.isEmpty(mIdentityApproveViewModel.getPageData().getIdCardImg_front())) {
            ToastUtils.info("请上传证件");
            return;
        }
        String idNum = mIdentityApproveViewModel.getPageData().getIDCardNum().get().replaceAll(" ", "");
        if (!Utils.PersonIdValidation(idNum)) {
            ToastUtils.info("身份证号码不正确，请重新输入");
            return;
        }
        uploadIDCardFrontImg();
    }

    //验证码输入框内容变化
    public void verificationCodeEdittextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() < DataFormat.VerificaCodeLeastLength) {
            mIdentityApproveActivityBinding.btCertification.setEnabled(false);
            return;
        }
        //电话号码
        String phoneNum = mIdentityApproveViewModel.getPageData().getPhoneNum().get();
        //所在城市
        String curCity = mIdentityApproveViewModel.getPageData().getCurCity().get();
        //当前详细住址
        String detailLocation = mIdentityApproveViewModel.getPageData().getDetailLocation().get();
        //真实姓名
        String realName = mIdentityApproveViewModel.getPageData().getRealName().get();
        //身份证号码
        String IDCardNum = mIdentityApproveViewModel.getPageData().getIDCardNum().get();
        if (phoneNum.length() == DataFormat.MobileNum &&
                curCity.length() >= DataFormat.CityAddressLeastLength &&
                detailLocation.length() >= DataFormat.DetailAddressLeastLength &&
                realName.length() >= DataFormat.RealNameLeastLength && IDCardNum.length() >= DataFormat.IDCardNumLeastLength) {
            mIdentityApproveActivityBinding.btCertification.setEnabled(true);
        } else {
            mIdentityApproveActivityBinding.btCertification.setEnabled(false);
        }
    }

    //身份证号码输入框监听
    public void userIDCardEdittextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() < DataFormat.IDCardNumLeastLength) {
            mIdentityApproveActivityBinding.btCertification.setEnabled(false);
            return;
        }
        //验证码
        String verifiCode = mIdentityApproveViewModel.getPageData().getVerificationCode().get();
        //电话号码
        String phoneNum = mIdentityApproveViewModel.getPageData().getPhoneNum().get();
        //所在城市
        String curCity = mIdentityApproveViewModel.getPageData().getCurCity().get();
        //当前详细住址
        String detailLocation = mIdentityApproveViewModel.getPageData().getDetailLocation().get();
        //真实姓名
        String realName = mIdentityApproveViewModel.getPageData().getRealName().get();
        if (phoneNum.length() == DataFormat.MobileNum &&
                curCity.length() >= DataFormat.CityAddressLeastLength &&
                detailLocation.length() >= DataFormat.DetailAddressLeastLength &&
                realName.length() >= DataFormat.RealNameLeastLength &&
                verifiCode.length() >= DataFormat.VerificaCodeLeastLength) {
            mIdentityApproveActivityBinding.btCertification.setEnabled(true);
        } else {
            mIdentityApproveActivityBinding.btCertification.setEnabled(false);
        }
    }

    //真实姓名输入框监听
    public void userRealNameEdittextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() < DataFormat.RealNameLeastLength) {
            mIdentityApproveActivityBinding.btCertification.setEnabled(false);
            return;
        }
        //验证码
        String verifiCode = mIdentityApproveViewModel.getPageData().getVerificationCode().get();
        //电话号码
        String phoneNum = mIdentityApproveViewModel.getPageData().getPhoneNum().get();
        //所在城市
        String curCity = mIdentityApproveViewModel.getPageData().getCurCity().get();
        //当前详细住址
        String detailLocation = mIdentityApproveViewModel.getPageData().getDetailLocation().get();
        //身份证号码
        String IDCardNum = mIdentityApproveViewModel.getPageData().getIDCardNum().get();
        if (phoneNum.length() == DataFormat.MobileNum &&
                curCity.length() >= DataFormat.CityAddressLeastLength &&
                detailLocation.length() >= DataFormat.DetailAddressLeastLength &&
                IDCardNum.length() >= DataFormat.IDCardNumLeastLength &&
                verifiCode.length() >= DataFormat.VerificaCodeLeastLength) {
            mIdentityApproveActivityBinding.btCertification.setEnabled(true);
        } else {
            mIdentityApproveActivityBinding.btCertification.setEnabled(false);
        }
    }

    //详细地址输入框监听
    public void userDetailLocEdittextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() < DataFormat.DetailAddressLeastLength) {
            mIdentityApproveActivityBinding.btCertification.setEnabled(false);
            return;
        }
        //验证码
        String verifiCode = mIdentityApproveViewModel.getPageData().getVerificationCode().get();
        //电话号码
        String phoneNum = mIdentityApproveViewModel.getPageData().getPhoneNum().get();
        //所在城市
        String curCity = mIdentityApproveViewModel.getPageData().getCurCity().get();
        //身份证号码
        String IDCardNum = mIdentityApproveViewModel.getPageData().getIDCardNum().get();
        //真实姓名
        String realName = mIdentityApproveViewModel.getPageData().getRealName().get();
        if (phoneNum.length() == DataFormat.MobileNum &&
                curCity.length() >= DataFormat.CityAddressLeastLength &&
                realName.length() >= DataFormat.RealNameLeastLength &&
                IDCardNum.length() >= DataFormat.IDCardNumLeastLength &&
                verifiCode.length() >= DataFormat.VerificaCodeLeastLength) {
            mIdentityApproveActivityBinding.btCertification.setEnabled(true);
        } else {
            mIdentityApproveActivityBinding.btCertification.setEnabled(false);
        }
    }


    public void selectIDCardFrontImg(View view) {
        new QMUIBottomSheet.BottomListSheetBuilder(IdentityApproveActivity.this)
                .addItem("拍照")
                .addItem("相册")
                .setOnSheetItemClickListener(new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                    @Override
                    public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                        dialog.dismiss();
                        if (position == 0) {
                            Utils.jumpToCameraAndCut(IdentityApproveActivity.this, 3, 2);
                        } else if (position == 1) {
                            Utils.jumpToGalleryAndCut(IdentityApproveActivity.this, 3, 2);
                        }
                    }
                })
                .build()
                .show();
    }

    public void selectIDCardBehindImg(View view) {
        new QMUIBottomSheet.BottomListSheetBuilder(IdentityApproveActivity.this)
                .addItem("拍照")
                .addItem("相册")
                .setOnSheetItemClickListener(new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                    @Override
                    public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                        dialog.dismiss();
                        if (position == 0) {
                            Utils.jumpToCameraAndCut(IdentityApproveActivity.this, 3, 2);
                        } else if (position == 1) {
                            Utils.jumpToGalleryAndCut(IdentityApproveActivity.this, 3, 2);
                        }
                    }
                })
                .build()
                .show();

    }

    private void subscribeToModel(final IdentityApproveViewModel model) {
        //获取验证码
        model.getSmsLiveData().observe(this, responseData -> {
            closeLogadingDialog();
            if (responseData == null) {
                ToastUtils.showNetNoConnected();
                return;
            }
            if (responseData.isError()) {
                resolveResponseErrorCode(responseData.getCode(), responseData.getMsg());
                return;
            }
            mIdentityApproveActivityBinding.btGetVerificationCode.setVisibility(View.GONE);
            mIdentityApproveActivityBinding.tvCountdown.setVisibility(View.VISIBLE);
            mDisposable = RxUtils.CountDown(DataFormat.VerificaCodeValidTime, new Consumer<Long>() {
                @Override
                public void accept(Long aLong) throws Exception {
                    mIdentityApproveActivityBinding.tvCountdown.setText(aLong + "秒");
                }
            }, new Action() {
                @Override
                public void run() throws Exception {
                    mIdentityApproveActivityBinding.btGetVerificationCode.setText("重新获取验证码");
                    mIdentityApproveActivityBinding.btGetVerificationCode.setVisibility(View.VISIBLE);
                    mIdentityApproveActivityBinding.tvCountdown.setVisibility(View.GONE);
                }
            });
        });

        //身份绑定
        model.getBindingLiveData().observe(this, new Observer<User>() {
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
                GlobalUserDataStore.getInstance().updateUserData(responseData);
                ToastUtils.info("信息绑定成功");
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
            File file = new File(selectList.get(0).getPath());
            if (!file.exists()) {
                ToastUtils.info("文件不存在，请重试");
            }
            switch (requestCode) {
                case Request_Code_IDCard_Front:
                    Utils.loadFile(mIdentityApproveActivityBinding.imgvIdcardFront, selectList.get(0).getCompressPath());
                    mIdentityApproveViewModel.getPageData().setIdCardImg_front(selectList.get(0).getCompressPath());
                    break;
                case Request_Code_IDCard_Behind:
                    Utils.loadFile(mIdentityApproveActivityBinding.imgvIdcardBehind, selectList.get(0).getCompressPath());
                    mIdentityApproveViewModel.getPageData().setIdCardImg_behind(selectList.get(0).getCompressPath());
                    break;

            }
        }
    }

    private void uploadIDCardFrontImg() {
        showLogadingDialog();
        if (!TextUtils.isEmpty(mIdentityApproveViewModel.getPageData().getIdCardImg_front_url())) { //已在阿里云上传
            uploadIDCardBehindImg(); //上传身份证背面的图片
            return;
        }
        String filePath = mIdentityApproveViewModel.getPageData().getIdCardImg_front();
        File file = new File(filePath);
        if (file.length() > 1024 * 1024 * 2) { //不允许超过4M
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
                mIdentityApproveViewModel.getPageData().setIdCardImg_front_url(str);
                uploadIDCardBehindImg(); //上传身份证背面的图片
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                disposable.dispose();
                closeLogadingDialog();
                ToastUtils.info("证件信息上传失败");
            }

            @Override
            public void onComplete() {
                disposable.dispose();
            }
        });
    }

    private void uploadIDCardBehindImg() {
        if (!TextUtils.isEmpty(mIdentityApproveViewModel.getPageData().getIdCardImg_behind_url())) { //已在阿里云上传
            mIdentityApproveViewModel.bindUserInfo();
            return;
        }
        String filePath = mIdentityApproveViewModel.getPageData().getIdCardImg_behind();
        File file = new File(filePath);
        if (file.length() > 1024 * 1024 * 4) { //不允许超过4M
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
                mIdentityApproveViewModel.getPageData().setIdCardImg_behind_url(str);
                mIdentityApproveViewModel.bindUserInfo();
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                disposable.dispose();
                closeLogadingDialog();
                ToastUtils.info("证件信息上传失败");
            }

            @Override
            public void onComplete() {
                disposable.dispose();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDisposable != null) {
            mDisposable.dispose();
        }
    }
}
