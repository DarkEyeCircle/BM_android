package com.askia.coremodel.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableField;

import com.askia.coremodel.datamodel.http.entities.NearSotreData;

public class StoreDetilsViewModel extends ViewModel {
    private PageData pageData = new PageData();

    public StoreDetilsViewModel() {
    }

    public PageData getPageData() {
        return pageData;
    }

    public void init(NearSotreData.ListBean listBean) {
        pageData.name.set(listBean.getName());
        pageData.area.set(listBean.getArea());
        pageData.distance.set("<" + listBean.getDistance());
        pageData.businessTime.set("每周" + listBean.getBusinessTime());
        pageData.startTime.set(listBean.getStartTime());
        pageData.endTime.set(listBean.getEndTime());
        pageData.businessScope.set(listBean.getBusinessScope());
        pageData.comment.set(listBean.getComment());
        pageData.setLon(listBean.getLongitude());
        pageData.setLat(listBean.getLatitude());
        pageData.setLogoUrl(listBean.getLogo());
    }

    public class PageData {
        private ObservableField<String> name = new ObservableField<>();
        private ObservableField<String> area = new ObservableField<>();
        private ObservableField<String> distance = new ObservableField<>();
        private ObservableField<String> businessTime = new ObservableField<>();
        private ObservableField<String> startTime = new ObservableField<>();
        private ObservableField<String> endTime = new ObservableField<>();
        private ObservableField<String> businessScope = new ObservableField<>();
        private ObservableField<String> comment = new ObservableField<>();
        private String lon;
        private String lat;
        private String logoUrl;

        public ObservableField<String> getName() {
            return name;
        }

        public void setName(ObservableField<String> name) {
            this.name = name;
        }

        public ObservableField<String> getArea() {
            return area;
        }

        public void setArea(ObservableField<String> area) {
            this.area = area;
        }

        public ObservableField<String> getDistance() {
            return distance;
        }

        public void setDistance(ObservableField<String> distance) {
            this.distance = distance;
        }

        public ObservableField<String> getBusinessTime() {
            return businessTime;
        }

        public void setBusinessTime(ObservableField<String> businessTime) {
            this.businessTime = businessTime;
        }

        public ObservableField<String> getStartTime() {
            return startTime;
        }

        public void setStartTime(ObservableField<String> startTime) {
            this.startTime = startTime;
        }

        public ObservableField<String> getEndTime() {
            return endTime;
        }

        public void setEndTime(ObservableField<String> endTime) {
            this.endTime = endTime;
        }

        public ObservableField<String> getBusinessScope() {
            return businessScope;
        }

        public void setBusinessScope(ObservableField<String> businessScope) {
            this.businessScope = businessScope;
        }

        public ObservableField<String> getComment() {
            return comment;
        }

        public void setComment(ObservableField<String> comment) {
            this.comment = comment;
        }

        public String getLon() {
            return lon;
        }

        public void setLon(String lon) {
            this.lon = lon;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLogoUrl() {
            return logoUrl;
        }

        public void setLogoUrl(String logoUrl) {
            this.logoUrl = logoUrl;
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
