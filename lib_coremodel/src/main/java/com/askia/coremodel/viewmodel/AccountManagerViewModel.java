package com.askia.coremodel.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableField;
import android.text.TextUtils;

import com.askia.coremodel.GlobalUserDataStore;
import com.askia.coremodel.util.RegexUtils;

public class AccountManagerViewModel extends ViewModel {

    private PageData pageData;

    public AccountManagerViewModel() {
        pageData = new PageData();
    }

    public PageData getPageData() {
        return pageData;
    }

    public void setPageData(PageData pageData) {
        this.pageData = pageData;
    }

    public void update() {
        pageData.update();
    }

    public class PageData {

        public PageData() {
            phoneNum.set(RegexUtils.formatPhoneNumToHide(GlobalUserDataStore.getInstance().getMobile()));
            wechatNickName.set("");
            alipayNickName.set(GlobalUserDataStore.getInstance().getAlipayAccount());
        }

        public void update() {
            phoneNum.set(RegexUtils.formatPhoneNumToHide(GlobalUserDataStore.getInstance().getMobile()));
            wechatNickName.set("");
            //默认值
            String alipayAccount = GlobalUserDataStore.getInstance().getAlipayAccount();
            if (TextUtils.isEmpty(alipayAccount)) {
                alipayAccount = "未绑定";
            }
            alipayNickName.set(alipayAccount);
        }

        private ObservableField<String> phoneNum = new ObservableField<>();
        private ObservableField<String> wechatNickName = new ObservableField<>();
        private ObservableField<String> alipayNickName = new ObservableField<>();

        public ObservableField<String> getPhoneNum() {
            return phoneNum;
        }

        public void setPhoneNum(ObservableField<String> phoneNum) {
            this.phoneNum = phoneNum;
        }

        public ObservableField<String> getWechatNickName() {
            return wechatNickName;
        }

        public void setWechatNickName(ObservableField<String> wechatNickName) {
            this.wechatNickName = wechatNickName;
        }

        public ObservableField<String> getAlipayNickName() {
            return alipayNickName;
        }

        public void setAlipayNickName(ObservableField<String> alipayNickName) {
            this.alipayNickName = alipayNickName;
        }
    }

}
