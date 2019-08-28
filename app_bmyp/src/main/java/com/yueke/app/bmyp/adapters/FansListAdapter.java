package com.yueke.app.bmyp.adapters;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.TextView;

import com.askia.coremodel.datamodel.http.entities.FansData;
import com.askia.coremodel.datamodel.http.entities.ResponseCode;
import com.askia.coremodel.datamodel.http.parm.UserDictionary;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.yueke.app.bmyp.R;

import java.util.List;

public class FansListAdapter extends BaseQuickAdapter<FansData.ListBean, BaseViewHolder> {


    public FansListAdapter(int layoutResId, @Nullable List<FansData.ListBean> data) {
        super(layoutResId, data);
    }

    public FansListAdapter(@Nullable List<FansData.ListBean> data) {
        super(data);
    }

    public FansListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, FansData.ListBean item) {
        SimpleDraweeView cardCover = helper.getView(R.id.imgv_head);
        cardCover.setImageURI(item.getAvatar());
        helper.setText(R.id.tv_nick_name, item.getRealName());
        if (TextUtils.equals(item.getDictSex(), String.valueOf(ResponseCode.SEX_MAN))) {
            helper.setVisible(R.id.imgv_sex, true);
            helper.setBackgroundRes(R.id.imgv_sex, R.drawable.icon_boy);
        } else if (TextUtils.equals(item.getDictSex(), String.valueOf(ResponseCode.SEX_WOMAN))) {
            helper.setVisible(R.id.imgv_sex, true);
            helper.setBackgroundRes(R.id.imgv_sex, R.drawable.icon_girl);
        } else {
            helper.setVisible(R.id.imgv_sex, false);
        }
        helper.setText(R.id.tv_contribution, item.getContribution());
        helper.addOnClickListener(R.id.bt_chat);
    }

}
