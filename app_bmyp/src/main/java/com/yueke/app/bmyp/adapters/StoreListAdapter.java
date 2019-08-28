package com.yueke.app.bmyp.adapters;

import android.support.annotation.Nullable;

import com.askia.coremodel.datamodel.http.entities.NearSotreData;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.yueke.app.bmyp.R;

import java.util.List;

public class StoreListAdapter extends BaseQuickAdapter<NearSotreData.ListBean, BaseViewHolder> {


    public StoreListAdapter(int layoutResId, @Nullable List<NearSotreData.ListBean> data) {
        super(layoutResId, data);
    }

    public StoreListAdapter(@Nullable List<NearSotreData.ListBean> data) {
        super(data);
    }

    public StoreListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, NearSotreData.ListBean item) {
        SimpleDraweeView cardCover = helper.getView(R.id.imgv_store);
        cardCover.setImageURI(item.getLogo());
        helper.setText(R.id.tv_store_name, item.getName());
        helper.setText(R.id.tv_location, item.getArea());
        helper.setText(R.id.tv_distance, "<" + item.getDistance());
    }
}
