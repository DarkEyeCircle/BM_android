package com.yueke.app.bmyp.adapters;

import android.support.annotation.Nullable;

import com.askia.coremodel.datamodel.http.entities.BandCardData;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yueke.app.bmyp.R;

import java.util.List;

public class BankCardListAdapter extends BaseQuickAdapter<BandCardData.ListBean, BaseViewHolder> {


    public BankCardListAdapter(int layoutResId, @Nullable List<BandCardData.ListBean> data) {
        super(layoutResId, data);
    }

    public BankCardListAdapter(@Nullable List<BandCardData.ListBean> data) {
        super(data);
    }

    public BankCardListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, BandCardData.ListBean item) {
        helper.setText(R.id.tv_bank_name, item.getOpeningBank());
        helper.setText(R.id.tv_bank_type, "储蓄卡");
        helper.setText(R.id.tv_bank_num, item.getCardNo());
    }

}
