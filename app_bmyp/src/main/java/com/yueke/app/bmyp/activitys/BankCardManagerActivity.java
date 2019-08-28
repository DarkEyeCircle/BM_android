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
import com.askia.coremodel.datamodel.http.entities.BandCardData;
import com.askia.coremodel.datamodel.http.entities.BaseResponseData;
import com.askia.coremodel.viewmodel.BankCardManagerViewModel;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.yueke.app.bmyp.R;
import com.yueke.app.bmyp.adapters.BankCardListAdapter;
import com.yueke.app.bmyp.databinding.BankCardManagerActivityBinding;

@Route(path = ARouterPath.ActivityBankCardManager)
public class BankCardManagerActivity extends BaseActivity {

    private BankCardManagerActivityBinding mBankCardManagerActivityBinding = null;
    private BankCardManagerViewModel mBankCardManagerViewModel = null;
    private BankCardListAdapter mBankCardListAdapter = null;
    private static final int RequestCodeAddCardSuccess = 110;
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
        mBankCardManagerActivityBinding = DataBindingUtil.setContentView(this, R.layout.bank_card_manager_activity);
    }

    private void initTopBar() {
        mBankCardManagerActivityBinding.topBar.setTitle(R.string.bank_card_manager).getPaint().setFakeBoldText(true);
        mBankCardManagerActivityBinding.topBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btSave = mBankCardManagerActivityBinding.topBar.addRightTextButton(getResources().getString(R.string.add), R.id.bt_save);
        btSave.setTextColor(getResources().getColor(R.color.black));
        btSave.setOnClickListener(view -> {
            if (mBankCardListAdapter.getItemCount() >= 1) {
                ToastUtils.info("最多只能绑定一张银行卡");
                return;
            }
            ARouter.getInstance().build(ARouterPath.ActivityAddBankCard).navigation(this, RequestCodeAddCardSuccess);
        });
        //解决返回键不居中问题
        mBankCardManagerActivityBinding.topBar.setGravity(Gravity.CENTER_VERTICAL);
    }

    private void initViewModel() {
        mBankCardManagerViewModel = ViewModelProviders.of(BankCardManagerActivity.this).get(BankCardManagerViewModel.class);
        subscribeToModel(mBankCardManagerViewModel);
    }

    private void initData() {
        mBankCardManagerActivityBinding.setHandlers(this);
    }

    private void initViews() {
        btSave.setVisibility(View.GONE);
        mBankCardManagerActivityBinding.noCardLayout.setVisibility(View.GONE);
        mBankCardManagerActivityBinding.bankCardList.setVisibility(View.GONE);

        mBankCardListAdapter = new BankCardListAdapter(R.layout.item_bank_card_layout);
        //开启动画(默认为渐显效果)
        mBankCardListAdapter.openLoadAnimation();
        //设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(getBaseContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mBankCardManagerActivityBinding.bankCardList.setLayoutManager(layoutManager);
        //设置Item间距
        mBankCardManagerActivityBinding.bankCardList.addItemDecoration(new SpacesItemDecoration(15));
        // 设置适配器
        mBankCardManagerActivityBinding.bankCardList.setAdapter(mBankCardListAdapter);
        mBankCardListAdapter.setOnItemLongClickListener((adapter, view, position) -> {
            final BandCardData.ListBean listBean = (BandCardData.ListBean) adapter.getItem(position);
            new QMUIDialog.MessageDialogBuilder(BankCardManagerActivity.this)
                    .setTitle("提示")
                    .setMessage("确定删除该银行卡？")
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
                            mBankCardManagerViewModel.unbindBankCard(listBean);
                        }
                    })
                    .create(com.qmuiteam.qmui.R.style.QMUI_Dialog).show();
            return false;
        });
    }

    //添加地址
    public void jumpToAddAddr(View view) {
        ARouter.getInstance().build(ARouterPath.ActivityAddBankCard).navigation(this, RequestCodeAddCardSuccess);
    }

    private void subscribeToModel(final BankCardManagerViewModel model) {
        //获取银行卡列表
        model.getBankCardListLiveData().observe(this, new Observer<BandCardData>() {
            @Override
            public void onChanged(@Nullable BandCardData responseData) {
                closeLogadingDialog();
                if (responseData == null) {
                    mBankCardManagerActivityBinding.emptyView.show(false, getResources().getString(R.string.emptyView_mode_desc_fail_desc), getResources().getString(R.string.emptyView_mode_desc_fail_desc), getResources().getString(R.string.emptyView_mode_desc_retry), retryAgainListener);
                    return;
                }
                if (responseData.isError()) {
                    mBankCardManagerActivityBinding.emptyView.show(false, getResources().getString(R.string.emptyView_mode_desc_fail_title), null, getResources().getString(R.string.emptyView_mode_desc_retry), retryAgainListener);
                    resolveResponseErrorCode(responseData.getCode(), responseData.getMsg());
                    return;
                }
                if (null == responseData.getList() || responseData.getList().size() <= 0) {
                    mBankCardManagerActivityBinding.emptyView.show(false);
                    mBankCardManagerActivityBinding.noCardLayout.setVisibility(View.VISIBLE);
                    return;
                }
                btSave.setVisibility(View.VISIBLE);
                mBankCardManagerActivityBinding.emptyView.show(false);
                mBankCardManagerActivityBinding.bankCardList.setVisibility(View.VISIBLE);
                mBankCardListAdapter.setNewData(responseData.getList());
            }
        });

        //解绑银行卡
        model.getUnbindCardLiveData().observe(this, new Observer<BaseResponseData>() {
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
                com.askia.common.util.ToastUtils.info("解绑银行卡成功");
                mBankCardListAdapter.remove(delItemPositon);
                if (mBankCardListAdapter.getData().size() <= 0) {
                    btSave.setVisibility(View.GONE);
                    mBankCardManagerActivityBinding.bankCardList.setVisibility(View.GONE);
                    mBankCardManagerActivityBinding.noCardLayout.setVisibility(View.VISIBLE);
                }

            }
        });

    }


    private View.OnClickListener retryAgainListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            btSave.setVisibility(View.GONE);
            mBankCardManagerActivityBinding.emptyView.show(true);
            mBankCardManagerActivityBinding.emptyView.setTitleText("正在加载");
            mBankCardManagerViewModel.getBankCardListData();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RequestCodeAddCardSuccess && resultCode == RESULT_OK) {
            LogUtils.d("onActivityResult");
            btSave.setVisibility(View.GONE);
            mBankCardManagerActivityBinding.bankCardList.setVisibility(View.GONE);
            mBankCardManagerActivityBinding.noCardLayout.setVisibility(View.GONE);
            mBankCardManagerActivityBinding.emptyView.show(true);
            mBankCardManagerActivityBinding.emptyView.setTitleText("正在加载");
            mBankCardManagerViewModel.getBankCardListData();
        }
    }

}
