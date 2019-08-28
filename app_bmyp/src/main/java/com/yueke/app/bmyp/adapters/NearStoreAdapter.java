package com.yueke.app.bmyp.adapters;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yueke.app.bmyp.R;

import java.util.List;

public class NearStoreAdapter extends BaseQuickAdapter<NearStoreAdapter.TodayRecommendedData, BaseViewHolder> {

    public TodayRecommendedData getTodayRecommendedData() {
        return new TodayRecommendedData();
    }

    public NearStoreAdapter(int layoutResId, @Nullable List<TodayRecommendedData> data) {
        super(layoutResId, data);
    }

    public NearStoreAdapter(@Nullable List<TodayRecommendedData> data) {
        super(data);
    }

    public NearStoreAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, TodayRecommendedData item) {
        helper.setBackgroundRes(R.id.imgv_pic, item.getImageUrl());
        helper.setText(R.id.tv_desc, item.getDesc());
    }

    //今日推荐
    public class TodayRecommendedData {

        private int imageUrl;

        private String desc;

        public int getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(int imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }
}
