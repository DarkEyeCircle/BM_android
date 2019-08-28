package com.askia.coremodel.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

import com.askia.coremodel.GlobalUserDataStore;

public class MineFragmentViewModel extends ViewModel {

    private PageData pageData = new PageData();

    public PageData getPageData() {
        return pageData;
    }

    public class PageData {

        public PageData() {
            update();
        }

        private ObservableField<String> nickName = new ObservableField<>();
        private ObservableField<String> dictLevelName = new ObservableField<>();
        private ObservableBoolean isMan = new ObservableBoolean();
        private ObservableBoolean isNeutralPeople = new ObservableBoolean();
        private String imgHeadUrl;

        public void update() {
            nickName.set(GlobalUserDataStore.getInstance().getNickName());
            dictLevelName.set(GlobalUserDataStore.getInstance().getDictLevelName());
            isMan.set(GlobalUserDataStore.getInstance().isMan());
            isNeutralPeople.set(GlobalUserDataStore.getInstance().isNeutralPeople());
            imgHeadUrl = GlobalUserDataStore.getInstance().getAvatar();
        }

        public ObservableField<String> getNickName() {
            return nickName;
        }

        public void setNickName(ObservableField<String> nickName) {
            this.nickName = nickName;
        }

        public ObservableField<String> getDictLevelName() {
            return dictLevelName;
        }

        public void setDictLevelName(ObservableField<String> dictLevelName) {
            this.dictLevelName = dictLevelName;
        }

        public ObservableBoolean getIsMan() {
            return isMan;
        }

        public void setIsMan(ObservableBoolean isMan) {
            this.isMan = isMan;
        }

        public ObservableBoolean getIsNeutralPeople() {
            return isNeutralPeople;
        }

        public void setIsNeutralPeople(ObservableBoolean isNeutralPeople) {
            this.isNeutralPeople = isNeutralPeople;
        }

        public String getImgHeadUrl() {
            return imgHeadUrl;
        }

        public void setImgHeadUrl(String imgHeadUrl) {
            this.imgHeadUrl = imgHeadUrl;
        }
    }

}
