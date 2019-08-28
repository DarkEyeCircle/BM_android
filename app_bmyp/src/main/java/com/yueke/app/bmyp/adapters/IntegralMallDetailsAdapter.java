package com.yueke.app.bmyp.adapters;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.askia.coremodel.datamodel.http.entities.IntegralMallDetailsData;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yueke.app.bmyp.R;

import java.util.List;

public class IntegralMallDetailsAdapter extends BaseQuickAdapter<IntegralMallDetailsData.ListBean, BaseViewHolder> {


    public IntegralMallDetailsAdapter(int layoutResId, @Nullable List<IntegralMallDetailsData.ListBean> data) {
        super(layoutResId, data);
    }

    public IntegralMallDetailsAdapter(@Nullable List<IntegralMallDetailsData.ListBean> data) {
        super(data);
    }

    public IntegralMallDetailsAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, IntegralMallDetailsData.ListBean item) {
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
