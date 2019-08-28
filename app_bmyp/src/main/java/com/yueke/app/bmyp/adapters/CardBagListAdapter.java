package com.yueke.app.bmyp.adapters;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.askia.coremodel.datamodel.http.entities.CardsData;
import com.askia.coremodel.datamodel.http.entities.CardsData;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.yueke.app.bmyp.R;

import java.util.List;

public class CardBagListAdapter extends BaseQuickAdapter<CardsData.ListBean, BaseViewHolder> {


    public CardBagListAdapter(int layoutResId, @Nullable List<CardsData.ListBean> data) {
        super(layoutResId, data);
    }

    public CardBagListAdapter(@Nullable List<CardsData.ListBean> data) {
        super(data);
    }

    public CardBagListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, CardsData.ListBean item) {
        helper.setText(R.id.tv_face_num, item.getFaceValue());
        helper.setText(R.id.tv_face_value, item.getFaceValue());
        SimpleDraweeView cardCover = helper.getView(R.id.imgv_card_cover);
        cardCover.setImageURI(item.getCoverUrl());
        helper.addOnClickListener(R.id.bt_buy);
    }
}
