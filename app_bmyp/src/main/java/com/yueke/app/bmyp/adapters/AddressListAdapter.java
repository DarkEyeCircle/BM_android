package com.yueke.app.bmyp.adapters;

import android.support.annotation.Nullable;

import com.askia.coremodel.datamodel.http.entities.AddressData;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yueke.app.bmyp.R;

import java.util.List;

public class AddressListAdapter extends BaseQuickAdapter<AddressData.ObjBean, BaseViewHolder> {


    public AddressListAdapter(int layoutResId, @Nullable List<AddressData.ObjBean> data) {
        super(layoutResId, data);
    }

    public AddressListAdapter(@Nullable List<AddressData.ObjBean> data) {
        super(data);
    }

    public AddressListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, AddressData.ObjBean item) {
        helper.setText(R.id.tv_name, item.getName());
        helper.setText(R.id.tv_phone_num, item.getMobile());
        helper.setText(R.id.tv_addr, item.getArea() + item.getAddress());
        helper.setChecked(R.id.bt_default, item.isDefault());
        helper.addOnClickListener(R.id.bt_editor);
        helper.addOnClickListener(R.id.bt_del);
        helper.addOnClickListener(R.id.bt_set_default);
    }

}
