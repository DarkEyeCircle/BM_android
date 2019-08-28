package com.yueke.app.bmyp.adapters;

import android.support.annotation.Nullable;

import com.askia.coremodel.datamodel.http.entities.BillDetailsData;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yueke.app.bmyp.R;

import java.util.List;

public class TransferHistoryListAdapter extends BaseQuickAdapter<BillDetailsData.ListBean, BaseViewHolder> {


    public TransferHistoryListAdapter(int layoutResId, @Nullable List<BillDetailsData.ListBean> data) {
        super(layoutResId, data);
    }

    public TransferHistoryListAdapter(@Nullable List<BillDetailsData.ListBean> data) {
        super(data);
    }

    public TransferHistoryListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, BillDetailsData.ListBean item) {
        helper.setText(R.id.tv_name, item.getTradingObject());
        helper.setText(R.id.tv_desc, item.getDictChannelTypeName());
        helper.setText(R.id.tv_money, item.getRealityAmount());
        helper.setText(R.id.tv_time, item.getInsertTimeTime());
    }
}
