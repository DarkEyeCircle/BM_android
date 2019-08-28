package com.yueke.app.bmyp.adapters;

import android.support.annotation.Nullable;

import com.askia.coremodel.datamodel.http.entities.ProceedsData;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.yueke.app.bmyp.R;

import java.util.List;

public class ProceedsAdapter extends BaseQuickAdapter<ProceedsData.ObjBean, BaseViewHolder> {


    public ProceedsAdapter(int layoutResId, @Nullable List<ProceedsData.ObjBean> data) {
        super(layoutResId, data);
    }

    public ProceedsAdapter(@Nullable List<ProceedsData.ObjBean> data) {
        super(data);
    }

    public ProceedsAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, ProceedsData.ObjBean item) {
        SimpleDraweeView cardCover = helper.getView(R.id.imgv_head);
        cardCover.setImageURI(item.getAvatar());
        helper.setText(R.id.tv_nick_name, item.getName());
        helper.setText(R.id.tv_money, item.getMoney());
    }

}
