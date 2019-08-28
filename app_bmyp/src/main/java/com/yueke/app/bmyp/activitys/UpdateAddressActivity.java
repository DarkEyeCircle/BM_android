package com.yueke.app.bmyp.activitys;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.askia.common.base.ARouterPath;
import com.askia.common.base.BaseActivity;
import com.askia.common.util.ToastUtils;
import com.askia.coremodel.datamodel.http.entities.BaseResponseData;
import com.askia.coremodel.viewmodel.UpdateAddressViewModel;
import com.lljjcoder.Interface.OnCityItemClickListener;
import com.lljjcoder.bean.CityBean;
import com.lljjcoder.bean.DistrictBean;
import com.lljjcoder.bean.ProvinceBean;
import com.lljjcoder.citywheel.CityConfig;
import com.lljjcoder.style.citypickerview.CityPickerView;
import com.yueke.app.bmyp.R;
import com.yueke.app.bmyp.databinding.UpdateAddrActivityBinding;

import static com.askia.common.util.InputTools.HideKeyboard;

@Route(path = ARouterPath.ActivityUpdateAddress)
public class UpdateAddressActivity extends BaseActivity {

    private UpdateAddrActivityBinding mUpdateAddrActivityBinding = null;
    private UpdateAddressViewModel mUpdateAddressViewModel = null;
    private CityPickerView mCityPickerView = null;
    private Button btSave = null;

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
        mUpdateAddrActivityBinding = DataBindingUtil.setContentView(this, R.layout.update_addr_activity);
    }

    private void initTopBar() {
        mUpdateAddrActivityBinding.topBar.setTitle(R.string.add_address).getPaint().setFakeBoldText(true);
        btSave = mUpdateAddrActivityBinding.topBar.addRightTextButton(getResources().getString(R.string.save), R.id.bt_save);
        btSave.setTextColor(getResources().getColor(R.color.line_color));
        btSave.setEnabled(false);
        btSave.setOnClickListener(view -> storShippingAddress());
        mUpdateAddrActivityBinding.topBar.addLeftBackImageButton().setOnClickListener(v -> finish());
        mUpdateAddrActivityBinding.topBar.setGravity(Gravity.CENTER_VERTICAL);
    }

    private void initViewModel() {
        mUpdateAddressViewModel = ViewModelProviders.of(UpdateAddressActivity.this).get(UpdateAddressViewModel.class);
        subscribeToModel(mUpdateAddressViewModel);
    }

    private void initData() {
        mUpdateAddrActivityBinding.setHandlers(this);
        mUpdateAddrActivityBinding.setPageData(mUpdateAddressViewModel.getPageData());
        mUpdateAddressViewModel.setPageData(getIntent().getParcelableExtra("objBean"));
    }

    //保存收货地址
    public void storShippingAddress() {
        showLogadingDialog();
        mUpdateAddressViewModel.updateUserAddress();
    }


    //姓名输入框内容变化
    public void userNameEdittextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() < 2) {
            btSave.setTextColor(getResources().getColor(R.color.line_color));
            btSave.setEnabled(false);
            return;
        }
        //手机号码
        String phoneNum = mUpdateAddressViewModel.getPageData().getPhoneNum().get();
        //省份地区
        String area = mUpdateAddressViewModel.getPageData().getArea().get();
        //详细地址
        String detailsArea = mUpdateAddressViewModel.getPageData().getDetailAddr().get();
        if (phoneNum.length() == 13 && area.length() > 2 && detailsArea.length() > 2) {
            btSave.setTextColor(getResources().getColor(R.color.app_color_blue_2));
            btSave.setEnabled(true);
            return;
        }
        btSave.setTextColor(getResources().getColor(R.color.line_color));
        btSave.setEnabled(false);
    }

    //手机号码输入框内容变化
    public void userPhoneNumEdittextChanged(CharSequence s, int start, int before, int count) {
        if (s == null || s.length() == 0) {
            btSave.setTextColor(getResources().getColor(R.color.line_color));
            btSave.setEnabled(false);
            return;
        }
        if (s.length() != 13) {
            btSave.setTextColor(getResources().getColor(R.color.line_color));
            btSave.setEnabled(false);
            return;
        }
        //姓名
        String userName = mUpdateAddressViewModel.getPageData().getName().get();
        //省份地区
        String area = mUpdateAddressViewModel.getPageData().getArea().get();
        //详细地址
        String detailsArea = mUpdateAddressViewModel.getPageData().getDetailAddr().get();

        if (userName.length() >= 2 && area.length() > 2 && detailsArea.length() > 2) {
            btSave.setTextColor(getResources().getColor(R.color.app_color_blue_2));
            btSave.setEnabled(true);
            return;
        }
        btSave.setTextColor(getResources().getColor(R.color.line_color));
        btSave.setEnabled(false);

    }

    //省份城市地区输入框内容变化
    public void userAreaEdittextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() <= 2) {
            btSave.setTextColor(getResources().getColor(R.color.line_color));
            btSave.setEnabled(false);
            return;
        }
        //姓名
        String userName = mUpdateAddressViewModel.getPageData().getName().get();
        //手机号码
        String phoneNum = mUpdateAddressViewModel.getPageData().getPhoneNum().get();
        //详细地址
        String detailsArea = mUpdateAddressViewModel.getPageData().getDetailAddr().get();
        if (userName.length() >= 2 && phoneNum.length() == 13 && detailsArea.length() > 2) {
            btSave.setTextColor(getResources().getColor(R.color.app_color_blue_2));
            btSave.setEnabled(true);
            return;
        }
        btSave.setTextColor(getResources().getColor(R.color.line_color));
        btSave.setEnabled(false);

    }

    //详细地址输入框内容变化
    public void userDetailAreaEdittextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() <= 2) {
            btSave.setTextColor(getResources().getColor(R.color.line_color));
            btSave.setEnabled(false);
            return;
        }
        //姓名
        String userName = mUpdateAddressViewModel.getPageData().getName().get();
        //手机号码
        String phoneNum = mUpdateAddressViewModel.getPageData().getPhoneNum().get();
        //省份地区
        String area = mUpdateAddressViewModel.getPageData().getArea().get();

        if (userName.length() >= 2 && phoneNum.length() == 13 && area.length() > 2) {
            btSave.setTextColor(getResources().getColor(R.color.app_color_blue_2));
            btSave.setEnabled(true);
            return;
        }
        btSave.setTextColor(getResources().getColor(R.color.line_color));
        btSave.setEnabled(false);
    }


    //设置为默认地址
    public void setDefaultAddr(View view) {
        if (mUpdateAddressViewModel.getPageData().getDefaultAddr().get()) {
            mUpdateAddressViewModel.getPageData().getDefaultAddr().set(false);
            return;
        }
        mUpdateAddressViewModel.getPageData().getDefaultAddr().set(true);
    }

    //选择地区
    public void doSelectArea(View view) {
        HideKeyboard(view);
        mCityPickerView.showCityPicker();
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
                mUpdateAddressViewModel.getPageData().getArea().set(cityStr);
            }

            @Override
            public void onCancel() {
            }
        });
    }

    private void subscribeToModel(final UpdateAddressViewModel model) {

        model.getUpdateAddressLiveData().observe(this, new Observer<BaseResponseData>() {
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
                ToastUtils.info("更改成功");
                setResult(RESULT_OK);
                finish();
            }
        });
    }

}
