package com.yueke.app.bmyp.activitys;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.askia.common.base.ARouterPath;
import com.askia.common.base.BaseActivity;
import com.askia.coremodel.datamodel.http.entities.MoneyStatistics;
import com.askia.coremodel.viewmodel.BillViewModel;
import com.askia.coremodel.viewmodel.BillsDetilsViewModel;
import com.yueke.app.bmyp.R;
import com.yueke.app.bmyp.databinding.BillActivityBinding;

@Route(path = ARouterPath.ActivityBill)
public class BillActivity extends BaseActivity {

    private BillActivityBinding mBillActivityBinding;
    private BillViewModel mBillViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDataBinding();
        initDataBinding();
        initTopBar();
        initViewModel();
        initData();
        initViews();
    }

    private void initDataBinding() {
        mBillActivityBinding = DataBindingUtil.setContentView(this, R.layout.bill_activity);
    }

    private void initTopBar() {
        mBillActivityBinding.topBar.setTitle(R.string.bill).getPaint().setFakeBoldText(true);
        mBillActivityBinding.topBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //解决返回键不居中问题
        mBillActivityBinding.topBar.setGravity(Gravity.CENTER_VERTICAL);
    }

    private void initViewModel() {
        mBillViewModel = ViewModelProviders.of(this).get(BillViewModel.class);
        subscribeToModel(mBillViewModel);
    }

    private void initData() {
        mBillActivityBinding.setHandlers(this);
        mBillActivityBinding.setPageData(mBillViewModel.getPageData());
    }

    private void initViews() {
        mBillActivityBinding.mainLayout.setVisibility(View.GONE);
    }

    public void itemOnclick(View view) {
        switch (view.getId()) {
            //消费明细
            case R.id.expense_detail:
                ARouter.getInstance().build(ARouterPath.ActivityStatementBillsDetails)
                        .navigation();
                break;
            //积分商城明细
            case R.id.points_mall_details:
                ARouter.getInstance().build(ARouterPath.ActivityIntegralMallDetails)
                        .navigation();
                break;
            //钱包明细
            case R.id.wallet_details:
                ARouter.getInstance().build(ARouterPath.ActivityBillsDetails)
                        .withInt("BillType", BillsDetilsViewModel.BillType.WalletDetails.getType())
                        .withString("title", "钱包明细")
                        .navigation();
                break;
            //提现明细
            case R.id.draw_money_details:
                ARouter.getInstance().build(ARouterPath.ActivityBillsDetails)
                        .withInt("BillType", BillsDetilsViewModel.BillType.DrawMoneyDetails.getType())
                        .withString("title", "提现明细")
                        .navigation();
                break;
            //商户明细
            case R.id.merchants_details:
                ARouter.getInstance().build(ARouterPath.ActivityBillsDetails)
                        .withInt("BillType", BillsDetilsViewModel.BillType.MerchantsDetails.getType())
                        .withString("title", "商户明细")
                        .navigation();
                break;
            //收款明细
            case R.id.collection_details:
                ARouter.getInstance().build(ARouterPath.ActivityBillsDetails)
                        .withInt("BillType", BillsDetilsViewModel.BillType.CollectionDetails.getType())
                        .withString("title", "收款明细")
                        .navigation();
                break;
            //充值明细
            case R.id.recharge_details:
                ARouter.getInstance().build(ARouterPath.ActivityBillsDetails)
                        .withInt("BillType", BillsDetilsViewModel.BillType.RechargeDetails.getType())
                        .withString("title", "充值明细")
                        .navigation();
                break;
        }
    }

    private void subscribeToModel(final BillViewModel model) {
        //获取银行卡列表
        model.getMoneyBalance().observe(this, new Observer<MoneyStatistics>() {
            @Override
            public void onChanged(@Nullable MoneyStatistics responseData) {
                if (responseData == null) {
                    mBillActivityBinding.emptyView.show(false, getResources().getString(R.string.emptyView_mode_desc_fail_desc), getResources().getString(R.string.emptyView_mode_desc_fail_desc), getResources().getString(R.string.emptyView_mode_desc_retry), retryAgainListener);
                    return;
                }
                if (responseData.isError() || null == responseData.getObj()) {
                    mBillActivityBinding.emptyView.show(false, getResources().getString(R.string.emptyView_mode_desc_fail_title), null, getResources().getString(R.string.emptyView_mode_desc_retry), retryAgainListener);
                    resolveResponseErrorCode(responseData.getCode(), responseData.getMsg());
                    return;
                }
                mBillActivityBinding.emptyView.show(false);
                mBillViewModel.update(responseData.getObj());
                mBillActivityBinding.mainLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    private View.OnClickListener retryAgainListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mBillActivityBinding.mainLayout.setVisibility(View.GONE);
            mBillActivityBinding.emptyView.show(true);
            mBillActivityBinding.emptyView.setTitleText("正在加载");
            mBillViewModel.getMoneyStatistics();
        }
    };
}
