package com.yueke.app.bmyp.adapters;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.askia.coremodel.datamodel.http.entities.StatementBillDetailsData;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yueke.app.bmyp.R;

import java.util.List;

public class BillStatementAdapter extends BaseQuickAdapter<StatementBillDetailsData.ListBean, BaseViewHolder> {


    public BillStatementAdapter(int layoutResId, @Nullable List<StatementBillDetailsData.ListBean> data) {
        super(layoutResId, data);
    }

    public BillStatementAdapter(@Nullable List<StatementBillDetailsData.ListBean> data) {
        super(data);
    }

    public BillStatementAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, StatementBillDetailsData.ListBean item) {
        if (TextUtils.isEmpty(item.getComment())) {
            helper.setText(R.id.tv_type, "");
        } else {
            helper.setText(R.id.tv_type, item.getCommodityName());
        }
        helper.setText(R.id.tv_amount, "-" + item.getAmount());
        helper.setTextColor(R.id.tv_amount, Color.parseColor("#ff4c4c"));
        helper.setText(R.id.tv_date, item.getInsertTimeTime());
    }
}
