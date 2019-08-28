package com.yueke.app.bmyp.adapters;

import android.support.annotation.Nullable;

import com.askia.coremodel.datamodel.http.entities.CardsData;
import com.askia.coremodel.datamodel.http.entities.UserCardsData;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.yueke.app.bmyp.R;

import java.util.List;

public class BuyCardHistoryHistoryAdapter extends BaseQuickAdapter<UserCardsData.ListBean, BaseViewHolder> {


    public BuyCardHistoryHistoryAdapter(int layoutResId, @Nullable List<UserCardsData.ListBean> data) {
        super(layoutResId, data);
    }

    public BuyCardHistoryHistoryAdapter(@Nullable List<UserCardsData.ListBean> data) {
        super(data);
    }

    public BuyCardHistoryHistoryAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, UserCardsData.ListBean item) {
        helper.setText(R.id.tv_face_num, item.getFaceValue());
        helper.setText(R.id.tv_face_value, item.getFaceValue());
        helper.setText(R.id.tv_integralProportion, item.getIntegralProportion());
        helper.setText(R.id.tv_integral, item.getIntegral());
        helper.setText(R.id.tv_date, item.getInsertTimeTime());

    }
}
