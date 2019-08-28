package com.yueke.app.bmyp.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.askia.common.base.BaseFragment;
import com.askia.common.util.ToastUtils;
import com.askia.coremodel.datamodel.http.entities.AgencyData;
import com.askia.coremodel.datamodel.http.entities.BaseResponseData;
import com.askia.coremodel.viewmodel.ApplyAgencyStateViewModel;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.yueke.app.bmyp.R;
import com.yueke.app.bmyp.activitys.AgencyActivity;
import com.yueke.app.bmyp.databinding.ApplyAgencyStateFragmentBinding;

public class ApplyAgencyStateFragment extends BaseFragment {

    public static final String INTENT_DATA = "INTENT_DATA";
    private ApplyAgencyStateFragmentBinding mApplyAgencyStateFragmentBinding;
    private ApplyAgencyStateViewModel mApplyAgencyStateViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mApplyAgencyStateFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.apply_agency_state_fragment, container, false);
        initData();
        return mApplyAgencyStateFragmentBinding.getRoot();
    }

    private void initData() {
        mApplyAgencyStateViewModel = ViewModelProviders.of(this).get(ApplyAgencyStateViewModel.class);
        mApplyAgencyStateFragmentBinding.setHandlers(this);
        mApplyAgencyStateFragmentBinding.setPageData(mApplyAgencyStateViewModel.getPageData());
        AgencyData.ObjBean objBean = getArguments().getParcelable(INTENT_DATA);
        mApplyAgencyStateViewModel.init(objBean);
        subscribeToModel(mApplyAgencyStateViewModel);
    }

    //拨打电话
    public void callPhone(View view) {
        final String phoneNum = "10086";
        new QMUIDialog.MessageDialogBuilder(getActivity())
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

    //重新提交
    public void submitAgain(View view) {
        showLogadingDialog();
        mApplyAgencyStateViewModel.applyAgencyAgain();
    }

    private void subscribeToModel(final ApplyAgencyStateViewModel model) {

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
                ((AgencyActivity) getActivity()).applyAgencyAgain();
            }
        });
    }

}
