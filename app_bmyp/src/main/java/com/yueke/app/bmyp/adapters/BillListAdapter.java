package com.yueke.app.bmyp.adapters;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.askia.coremodel.datamodel.http.entities.BillDetailsData;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yueke.app.bmyp.R;

import java.util.List;

public class BillListAdapter extends BaseQuickAdapter<BillDetailsData.ListBean, BaseViewHolder> {


    public BillListAdapter(int layoutResId, @Nullable List<BillDetailsData.ListBean> data) {
        super(layoutResId, data);
    }

    public BillListAdapter(@Nullable List<BillDetailsData.ListBean> data) {
        super(data);
    }

    public BillListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, BillDetailsData.ListBean item) {
        if (TextUtils.isEmpty(item.getComment())) {
            helper.setText(R.id.tv_type, "");
        } else {
            helper.setText(R.id.tv_type, item.getComment());
        }
        helper.setText(R.id.tv_amount, item.getSymbol() + item.getRealityAmount());
        if (TextUtils.equals("+", item.getSymbol().trim())) {
            helper.setTextColor(R.id.tv_amount, Color.parseColor("#3377ff"));
        } else {
            helper.setTextColor(R.id.tv_amount, Color.parseColor("#ff4c4c"));
        }
        helper.setText(R.id.tv_date, item.getInsertTimeTime());
    }
}
