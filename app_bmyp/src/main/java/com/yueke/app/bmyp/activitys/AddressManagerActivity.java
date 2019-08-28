package com.yueke.app.bmyp.activitys;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.apkfuns.logutils.LogUtils;
import com.askia.common.base.ARouterPath;
import com.askia.common.base.BaseActivity;
import com.askia.common.util.ToastUtils;
import com.askia.common.widget.SpacesItemDecoration;
import com.askia.coremodel.datamodel.http.entities.AddressData;
import com.askia.coremodel.datamodel.http.entities.BaseResponseData;
import com.askia.coremodel.viewmodel.AddressManagerViewModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.yueke.app.bmyp.DataFormat;
import com.yueke.app.bmyp.R;
import com.yueke.app.bmyp.adapters.AddressListAdapter;
import com.yueke.app.bmyp.databinding.AddressManagerActivityBinding;

@Route(path = ARouterPath.ActivityAddressManager)
public class AddressManagerActivity extends BaseActivity {

    private AddressManagerActivityBinding mAddrManagerActivityBinding = null;
    private AddressManagerViewModel mAddressManagerViewModel = null;
    private AddressListAdapter mAddressListAdapter = null;
    private static final int RequestCodeAddAddrSuccess = 111;
    private static final int RequestCodeUpdateAddrSuccess = 112;
    private Button btSave = null;
    private int delItemPositon = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDataBinding();
        initTopBar();
        initViews();
        initViewModel();
        initData();
    }

    private void initDataBinding() {
        mAddrManagerActivityBinding = DataBindingUtil.setContentView(this, R.layout.address_manager_activity);
    }

    private void initTopBar() {
        mAddrManagerActivityBinding.topBar.setTitle(R.string.shipping_addr_manager).getPaint().setFakeBoldText(true);
        mAddrManagerActivityBinding.topBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btSave = mAddrManagerActivityBinding.topBar.addRightTextButton(getResources().getString(R.string.add), R.id.bt_save);
        btSave.setTextColor(getResources().getColor(R.color.black));
        btSave.setOnClickListener(view -> {
            ARouter.getInstance().build(ARouterPath.ActivityAddAddress).navigation(this, RequestCodeAddAddrSuccess);
        });
        //解决返回键不居中问题
        mAddrManagerActivityBinding.topBar.setGravity(Gravity.CENTER_VERTICAL);
    }

    private void initViewModel() {
        mAddressManagerViewModel = ViewModelProviders.of(AddressManagerActivity.this).get(AddressManagerViewModel.class);
        subscribeToModel(mAddressManagerViewModel);
    }

    private void initData() {
        mAddrManagerActivityBinding.setHandlers(this);
    }

    private void initViews() {
        btSave.setVisibility(View.GONE);
        mAddrManagerActivityBinding.noCardLayout.setVisibility(View.GONE);
        mAddrManagerActivityBinding.addrList.setVisibility(View.GONE);

        mAddressListAdapter = new AddressListAdapter(R.layout.item_address_layout);
        //开启动画(默认为渐显效果)
        mAddressListAdapter.openLoadAnimation();
        //设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(getBaseContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mAddrManagerActivityBinding.addrList.setLayoutManager(layoutManager);
        //设置Item间距
        mAddrManagerActivityBinding.addrList.addItemDecoration(new SpacesItemDecoration(15));
        // 设置适配器
        mAddrManagerActivityBinding.addrList.setAdapter(mAddressListAdapter);
        mAddressListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                final AddressData.ObjBean objBean = (AddressData.ObjBean) adapter.getItem(position);
                if (view.getId() == R.id.bt_editor) { //编辑
                    ARouter.getInstance().build(ARouterPath.ActivityUpdateAddress)
                            .withParcelable("objBean", objBean)
                            .navigation(AddressManagerActivity.this, RequestCodeUpdateAddrSuccess);
                } else if (view.getId() == R.id.bt_del) { //删除
                    new QMUIDialog.MessageDialogBuilder(AddressManagerActivity.this)
                            .setTitle("提示")
                            .setMessage("确定删除该地址？")
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
                                    delItemPositon = position;
                                    showLogadingDialog();
                                    mAddressManagerViewModel.delAddr(objBean);
                                }
                            })
                            .create(com.qmuiteam.qmui.R.style.QMUI_Dialog).show();
                } else if (view.getId() == R.id.bt_set_default) {
                    if (objBean.getIsDefault() == 1) {
                        return;
                    }
                    showLogadingDialog();
                    mAddressManagerViewModel.setDefaultAddr(objBean);
                }
            }
        });
    }

    private void subscribeToModel(final AddressManagerViewModel model) {
        //获取地址列表
        model.getAddrListLiveData().observe(this, new Observer<AddressData>() {
            @Override
            public void onChanged(@Nullable AddressData responseData) {
                closeLogadingDialog();
                if (responseData == null) {
                    mAddrManagerActivityBinding.emptyView.show(false, getResources().getString(R.string.emptyView_mode_desc_fail_desc), getResources().getString(R.string.emptyView_mode_desc_fail_desc), getResources().getString(R.string.emptyView_mode_desc_retry), retryAgainListener);
                    return;
                }
                if (responseData.isError()) {
                    mAddrManagerActivityBinding.emptyView.show(false, getResources().getString(R.string.emptyView_mode_desc_fail_title), null, getResources().getString(R.string.emptyView_mode_desc_retry), retryAgainListener);
                    resolveResponseErrorCode(responseData.getCode(), responseData.getMsg());
                    return;
                }
                if (null == responseData.getObj() || responseData.getObj().size() <= 0) {
                    mAddrManagerActivityBinding.emptyView.show(false);
                    mAddrManagerActivityBinding.noCardLayout.setVisibility(View.VISIBLE);
                    return;
                }
                btSave.setVisibility(View.VISIBLE);
                mAddrManagerActivityBinding.emptyView.show(false);
                mAddrManagerActivityBinding.addrList.setVisibility(View.VISIBLE);
                mAddressListAdapter.setNewData(responseData.getObj());
            }
        });

        //删除地址
        model.getDelAddrLiveData().observe(this, new Observer<BaseResponseData>() {
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
                ToastUtils.info("删除地址成功");
                mAddressListAdapter.remove(delItemPositon);
                if (mAddressListAdapter.getData().size() <= 0) {
                    btSave.setVisibility(View.GONE);
                    mAddrManagerActivityBinding.addrList.setVisibility(View.GONE);
                    mAddrManagerActivityBinding.noCardLayout.setVisibility(View.VISIBLE);
                    return;
                }
                //重新拉取数据
                btSave.setVisibility(View.GONE);
                mAddrManagerActivityBinding.addrList.setVisibility(View.GONE);
                mAddrManagerActivityBinding.noCardLayout.setVisibility(View.GONE);
                mAddrManagerActivityBinding.emptyView.show(true);
                mAddrManagerActivityBinding.emptyView.setTitleText("正在加载");
                mAddressManagerViewModel.getAddrListData();

            }
        });

        //设置为默认地址
        model.getSetDefaultAddressLiveData().observe(this, new Observer<BaseResponseData>() {
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
                //重新拉取数据
                btSave.setVisibility(View.GONE);
                mAddrManagerActivityBinding.addrList.setVisibility(View.GONE);
                mAddrManagerActivityBinding.noCardLayout.setVisibility(View.GONE);
                mAddrManagerActivityBinding.emptyView.show(true);
                mAddrManagerActivityBinding.emptyView.setTitleText("正在加载");
                mAddressManagerViewModel.getAddrListData();
            }
        });
    }

    //添加地址
    public void jumpToAddAddr(View view) {
        if (mAddressListAdapter != null && mAddressListAdapter.getItemCount() >= DataFormat.AddAddressMaxCount) {
            ToastUtils.info("最多只能添加" + DataFormat.AddAddressMaxCount + "条地址");
            return;
        }
        ARouter.getInstance().build(ARouterPath.ActivityAddAddress)
                .withBoolean(AddAddressActivity.IntentSetDefalut, true)
                .navigation(this, RequestCodeAddAddrSuccess);
    }

    private View.OnClickListener retryAgainListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            btSave.setVisibility(View.GONE);
            mAddrManagerActivityBinding.emptyView.show(true);
            mAddrManagerActivityBinding.emptyView.setTitleText("正在加载");
            mAddressManagerViewModel.getAddrListData();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RequestCodeAddAddrSuccess || requestCode == RequestCodeUpdateAddrSuccess) {
            if (resultCode == RESULT_OK) {
                LogUtils.d("onActivityResult");
                btSave.setVisibility(View.GONE);
                mAddrManagerActivityBinding.addrList.setVisibility(View.GONE);
                mAddrManagerActivityBinding.noCardLayout.setVisibility(View.GONE);
                mAddrManagerActivityBinding.emptyView.show(true);
                mAddrManagerActivityBinding.emptyView.setTitleText("正在加载");
                mAddressManagerViewModel.getAddrListData();
            }
        }
    }


}
