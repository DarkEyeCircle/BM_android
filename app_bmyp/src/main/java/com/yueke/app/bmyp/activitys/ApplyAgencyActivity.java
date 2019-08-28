package com.yueke.app.bmyp.activitys;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.askia.common.base.ARouterPath;
import com.askia.common.base.BaseActivity;
import com.askia.common.util.ToastUtils;
import com.askia.coremodel.datamodel.http.entities.BaseResponseData;
import com.askia.coremodel.viewmodel.ApplyAgencyViewModel;
import com.lljjcoder.Interface.OnCityItemClickListener;
import com.lljjcoder.bean.CityBean;
import com.lljjcoder.bean.DistrictBean;
import com.lljjcoder.bean.ProvinceBean;
import com.lljjcoder.citywheel.CityConfig;
import com.lljjcoder.style.citypickerview.CityPickerView;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.yueke.app.bmyp.R;
import com.yueke.app.bmyp.databinding.ApplyAgencyActivityBinding;

import static com.askia.common.util.InputTools.HideKeyboard;

@Route(path = ARouterPath.ActivityApplyAgency)
public class ApplyAgencyActivity extends BaseActivity {

    private ApplyAgencyActivityBinding mApplyAgencyActivityBinding = null;
    private ApplyAgencyViewModel mApplyAgencyViewModel;
    private CityPickerView mCityPickerView = null;

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
        mApplyAgencyActivityBinding = DataBindingUtil.setContentView(this, R.layout.apply_agency_activity);
    }

    private void initTopBar() {
        mApplyAgencyActivityBinding.topBar.setTitle(R.string.apply_to_join).getPaint().setFakeBoldText(true);
        mApplyAgencyActivityBinding.topBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //解决返回键不居中问题
        mApplyAgencyActivityBinding.topBar.setGravity(Gravity.CENTER_VERTICAL);
    }

    private void initViewModel() {
        mApplyAgencyViewModel = ViewModelProviders.of(ApplyAgencyActivity.this).get(ApplyAgencyViewModel.class);
        subscribeToModel(mApplyAgencyViewModel);
    }

    private void initData() {
        mApplyAgencyActivityBinding.setHandlers(this);
        mApplyAgencyActivityBinding.setPageData(mApplyAgencyViewModel.getPageData());
    }

    //拨打电话
    public void callPhone(View view) {
        final String phoneNum = mApplyAgencyActivityBinding.linkTextView.getText().toString().replaceAll(" ", "");
        new QMUIDialog.MessageDialogBuilder(ApplyAgencyActivity.this)
                .setTitle("提示")
                .setMessage("确定拨打电话" + phoneNum + "?")
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction(0, "确定", QMUIDialogAction.ACTION_PROP_NEGATIVE, new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        Uri data = Uri.parse("tel:" + phoneNum);
                        intent.setData(data);
                        startActivity(intent);
                    }
                })
                .create(com.qmuiteam.qmui.R.style.QMUI_Dialog).show();


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
                mApplyAgencyViewModel.getPageData().getAgencyArea().set(cityStr);
                if (TextUtils.isEmpty(mApplyAgencyViewModel.getPageData().getAgencyLevel().get())) {
                    mApplyAgencyActivityBinding.btApply.setEnabled(false);
                } else {
                    mApplyAgencyActivityBinding.btApply.setEnabled(true);
                }
            }

            @Override
            public void onCancel() {
            }
        });
    }

    //选择城市
    public void doSelectCity(View view) {
        HideKeyboard(view);
        if (TextUtils.isEmpty(mApplyAgencyViewModel.getPageData().getAgencyLevel().get())) {
            ToastUtils.info("请选择代理级别");
            return;
        }
        mCityPickerView.showCityPicker();
    }

    //立即申请
    public void doApplyNow(View view) {
        showLogadingDialog();
        mApplyAgencyViewModel.applyAgency();
    }

    //选择代理级别
    public void selectAgencyLevel(View view) {
        new QMUIBottomSheet.BottomListSheetBuilder(ApplyAgencyActivity.this)
                .addItem(ApplyAgencyViewModel.AgencyLevel.AGENCY_GRADE_Five.getType())
                .addItem(ApplyAgencyViewModel.AgencyLevel.AGENCY_GRADE_FOUR.getType())
                .addItem(ApplyAgencyViewModel.AgencyLevel.AGENCY_GRADE_THREE.getType())
                .addItem(ApplyAgencyViewModel.AgencyLevel.AGENCY_GRADE_TWO.getType())
                .addItem(ApplyAgencyViewModel.AgencyLevel.AGENCY_GRADE_ONE.getType())
                .setOnSheetItemClickListener(new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                    @Override
                    public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                        dialog.dismiss();
                        CityConfig cityConfig = null;
                        switch (position) {
                            case 4: //全国级
                                mApplyAgencyViewModel.getPageData().getAgencyLevel().set(ApplyAgencyViewModel.AgencyLevel.AGENCY_GRADE_ONE.getType());
                                mApplyAgencyViewModel.getPageData().getAgencyArea().set("全国");
                                mApplyAgencyActivityBinding.btSelectCity.setEnabled(false);
                                break;
                            case 3: //省级
                                mApplyAgencyViewModel.getPageData().getAgencyLevel().set(ApplyAgencyViewModel.AgencyLevel.AGENCY_GRADE_TWO.getType());
                                mApplyAgencyActivityBinding.btSelectCity.setEnabled(true);
                                mApplyAgencyViewModel.getPageData().getAgencyArea().set("");
                                cityConfig = new CityConfig.Builder().title("选择代理省份")//标题
                                        .setCityWheelType(CityConfig.WheelType.PRO).build();
                                mCityPickerView.setConfig(cityConfig);
                                break;
                            case 2: //市级
                                mApplyAgencyViewModel.getPageData().getAgencyLevel().set(ApplyAgencyViewModel.AgencyLevel.AGENCY_GRADE_THREE.getType());
                                mApplyAgencyActivityBinding.btSelectCity.setEnabled(true);
                                mApplyAgencyViewModel.getPageData().getAgencyArea().set("");
                                cityConfig = new CityConfig.Builder().title("选择代理城市")//标题
                                        .setCityWheelType(CityConfig.WheelType.PRO_CITY).build();
                                mCityPickerView.setConfig(cityConfig);
                                break;
                            case 1: //区级
                                mApplyAgencyViewModel.getPageData().getAgencyLevel().set(ApplyAgencyViewModel.AgencyLevel.AGENCY_GRADE_FOUR.getType());
                                mApplyAgencyActivityBinding.btSelectCity.setEnabled(true);
                                mApplyAgencyViewModel.getPageData().getAgencyArea().set("");
                                cityConfig = new CityConfig.Builder().title("选择代理区域")//标题
                                        .setCityWheelType(CityConfig.WheelType.PRO_CITY_DIS).build();
                                mCityPickerView.setConfig(cityConfig);
                                break;
                            case 0: //经纪人
                                mApplyAgencyViewModel.getPageData().getAgencyLevel().set(ApplyAgencyViewModel.AgencyLevel.AGENCY_GRADE_Five.getType());
                                mApplyAgencyViewModel.getPageData().getAgencyArea().set("仅自己");
                                mApplyAgencyActivityBinding.btSelectCity.setEnabled(false);
                                break;
                        }
                        if (TextUtils.isEmpty(mApplyAgencyViewModel.getPageData().getAgencyArea().get())) {
                            mApplyAgencyActivityBinding.btApply.setEnabled(false);
                        } else {
                            mApplyAgencyActivityBinding.btApply.setEnabled(true);
                        }
                    }
                })
                .build()
                .show();
    }


    private void subscribeToModel(final ApplyAgencyViewModel model) {

        model.getApplyAgencyLiveData().observe(this, new Observer<BaseResponseData>() {
            @Override
            public void onChanged(@Nullable BaseResponseData responseData) {
                closeLogadingDialog();
                if (responseData == null) {
                    ToastUtils.showNetNoConnected();
                    return;
                }
                if (responseData.isError()) {
                    resolveResponseErrorCode(responseData.getCode(), responseData.getMsg());
                    return;
                }
                ToastUtils.info("添加成功");
                setResult(RESULT_OK);
                finish();
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
