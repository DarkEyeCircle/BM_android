package com.yueke.app.bmyp.adapters;

import com.askia.coremodel.datamodel.http.entities.HomeFragmentData;
import com.askia.coremodel.datamodel.http.entities.NearSotreData;
import com.askia.coremodel.datamodel.http.entities.TodayRecommendedData;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.facebook.drawee.view.SimpleDraweeView;
import com.yueke.app.bmyp.R;

import java.util.List;

public class HomeAdapter extends BaseMultiItemQuickAdapter<HomeAdapter.HomeListBean, BaseViewHolder> {


    public HomeAdapter(List<HomeListBean> data) {
        super(data);
        addItemType(HomeListBean.TYPE_RECOMMENDED_TTTLE, R.layout.item_today_recommend_title_layout);
        addItemType(HomeListBean.TYPE_RECOMMENDED, R.layout.item_today_recommende);
        addItemType(HomeListBean.TYPE_TODAY_RECOMMEND_TTTLE, R.layout.item_near_store_title_layout);
        addItemType(HomeListBean.TYPE_NEAR_STORE, R.layout.item_near_store);
    }

    @Override
    protected void convert(BaseViewHolder helper, HomeListBean item) {
        switch (helper.getItemViewType()) {
            case HomeListBean.TYPE_RECOMMENDED_TTTLE:
                helper.setText(R.id.tv_titile, item.getTitle());
                break;
            case HomeListBean.TYPE_RECOMMENDED:
                TodayRecommendedData.ListBean listBean = (TodayRecommendedData.ListBean) item.getHomeFragmentData();
                helper.setText(R.id.tv_desc, listBean.getDesc());
                helper.setImageResource(R.id.imgv_pic, listBean.getImageUrl());
                break;
            case HomeListBean.TYPE_TODAY_RECOMMEND_TTTLE:
                helper.setText(R.id.tv_titile, item.getTitle());
                helper.addOnClickListener(R.id.bt_more);
                break;
            case HomeListBean.TYPE_NEAR_STORE:
                NearSotreData.ListBean storeListBean = (NearSotreData.ListBean) item.getHomeFragmentData();
                SimpleDraweeView cardCover = helper.getView(R.id.imgv_store);
                cardCover.setImageURI(storeListBean.getLogo());
                helper.setText(R.id.tv_store_name, storeListBean.getName());
                helper.setText(R.id.tv_location, storeListBean.getArea());
                helper.setText(R.id.tv_distance, "<" + storeListBean.getDistance());
                break;
        }

    }

    public static class HomeListBean implements MultiItemEntity {
        public static final int TYPE_RECOMMENDED_TTTLE = 1; //今日推荐标题
        public static final int TYPE_RECOMMENDED = 2; //今日推荐
        public static final int TYPE_TODAY_RECOMMEND_TTTLE = 3; //附近门店标题
        public static final int TYPE_NEAR_STORE = 4; //附近门店
        private int itemType;
        private HomeFragmentData homeFragmentData;
        private String title;

        public HomeListBean(int itemType, String title) {
            this.itemType = itemType;
            this.title = title;
        }

        public HomeListBean(int itemType, HomeFragmentData homeFragmentData) {
            this.itemType = itemType;
            this.homeFragmentData = homeFragmentData;
        }

        public HomeFragmentData getHomeFragmentData() {
            return homeFragmentData;
        }

        public void setHomeFragmentData(HomeFragmentData homeFragmentData) {
            this.homeFragmentData = homeFragmentData;
        }

        public void setItemType(int itemType) {
            this.itemType = itemType;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        @Override
        public int getItemType() {
            return itemType;
        }

    }

}
