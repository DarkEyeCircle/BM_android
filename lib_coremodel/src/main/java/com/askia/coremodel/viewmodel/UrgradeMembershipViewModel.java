package com.askia.coremodel.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableField;
import android.text.TextUtils;

import com.askia.coremodel.GlobalUserDataStore;
import com.askia.coremodel.datamodel.http.entities.MemberUpdateBagsData;
import com.askia.coremodel.datamodel.http.entities.ResponseCode;
import com.askia.coremodel.datamodel.http.parm.UserDictionary;
import com.askia.coremodel.datamodel.http.repository.GankDataRepository;
import com.askia.coremodel.util.NetUtils;
import com.askia.coremodel.util.Utils;

import java.net.SocketTimeoutException;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class UrgradeMembershipViewModel extends ViewModel {

    private MutableLiveData<MemberUpdateBagsData> memberBagsLiveData = new MutableLiveData<>();
    private PageData pageData = new PageData();
    private final CompositeDisposable mDisposable = new CompositeDisposable();

    public UrgradeMembershipViewModel() {
    }

    public MutableLiveData<MemberUpdateBagsData> getMemberBagsLiveData() {
        return memberBagsLiveData;
    }

    public PageData getPageData() {
        return pageData;
    }

    public void getMemberBags() {
        if (!NetUtils.isNetConnected()) {
            memberBagsLiveData.setValue(null);
            return;
        }
        GankDataRepository.GetMemberBags()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .safeSubscribe(new Observer<MemberUpdateBagsData>() {

                    Disposable disposable;

                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(MemberUpdateBagsData responseData) {
                        memberBagsLiveData.setValue(responseData);
                    }

                    @Override
                    public void onError(Throwable e) {
                        disposable.dispose();
                        e.printStackTrace();
                        MemberUpdateBagsData responseData = new MemberUpdateBagsData();
                        if (e instanceof SocketTimeoutException) {
                            responseData.setCode(ResponseCode.ConnectTimeOut);
                            responseData.setMsg("连接超时，请重试");
                        } else {
                            responseData.setCode(ResponseCode.ServerNotResponding);
                            responseData.setMsg("服务器无响应，请重试");
                        }
                        memberBagsLiveData.setValue(responseData);
                    }

                    @Override
                    public void onComplete() {
                        disposable.dispose();
                    }
                });
    }

    public void init(MemberUpdateBagsData data) {
        MemberUpdateBagsData.ListBean listBean = data.getList().get(0);
        pageData.nickName.set(GlobalUserDataStore.getInstance().getNickName());
        if (data.getObj() == UserDictionary.MEMBER_LEVEL_6) {
            pageData.price.set("最高级别");
        } else {
            pageData.price.set(listBean.getPrice());
        }
        pageData.title.set(listBean.getTitle());
        String commnet = Utils.convertText(listBean.getComment());
        if (TextUtils.isEmpty(commnet)) {
            commnet = "";
        }
        pageData.comment.set(commnet);
        pageData.setImgUrl(listBean.getImgUrl());
        pageData.setId(listBean.getId());
        pageData.setDictLevel(listBean.getDictLevel());
        pageData.setImgvHeadUrl(GlobalUserDataStore.getInstance().getAvatar());
        //更新我的当前会员等级
        updateMemberBG(data.getObj());
        pageData.dictLevelName.set(GlobalUserDataStore.getInstance().getDictLevelName());
    }

    public class PageData {
        private ObservableField<String> nickName = new ObservableField<>();
        private ObservableField<String> dictLevelName = new ObservableField<>();
        private ObservableField<String> price = new ObservableField<>();
        private ObservableField<String> title = new ObservableField<>();
        private ObservableField<String> comment = new ObservableField<>();
        private String imgvHeadUrl;
        private String id;
        private String dictLevel;
        private String imgUrl;

        public ObservableField<String> getDictLevelName() {
            return dictLevelName;
        }

        public void setDictLevelName(ObservableField<String> dictLevelName) {
            this.dictLevelName = dictLevelName;
        }


        public ObservableField<String> getPrice() {
            return price;
        }

        public void setPrice(ObservableField<String> price) {
            this.price = price;
        }

        public ObservableField<String> getTitle() {
            return title;
        }

        public void setTitle(ObservableField<String> title) {
            this.title = title;
        }

        public ObservableField<String> getComment() {
            return comment;
        }

        public void setComment(ObservableField<String> comment) {
            this.comment = comment;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getDictLevel() {
            return dictLevel;
        }

        public void setDictLevel(String dictLevel) {
            this.dictLevel = dictLevel;
        }

        public ObservableField<String> getNickName() {
            return nickName;
        }

        public void setNickName(ObservableField<String> nickName) {
            this.nickName = nickName;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getImgvHeadUrl() {
            return imgvHeadUrl;
        }

        public void setImgvHeadUrl(String imgvHeadUrl) {
            this.imgvHeadUrl = imgvHeadUrl;
        }
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        mDisposable.clear();
    }

    public void updateMemberBG(int level) {
        switch (level) {
            case UserDictionary.MEMBER_LEVEL_0:
                GlobalUserDataStore.getInstance().setDictLevel(String.valueOf(level));
                GlobalUserDataStore.getInstance().setDictLevelName("普通会员");
                break;
            case UserDictionary.MEMBER_LEVEL_1:
                GlobalUserDataStore.getInstance().setDictLevel(String.valueOf(level));
                GlobalUserDataStore.getInstance().setDictLevelName("一级会员");
                break;
            case UserDictionary.MEMBER_LEVEL_2:
                GlobalUserDataStore.getInstance().setDictLevel(String.valueOf(level));
                GlobalUserDataStore.getInstance().setDictLevelName("二级会员");
                break;
            case UserDictionary.MEMBER_LEVEL_3:
                GlobalUserDataStore.getInstance().setDictLevel(String.valueOf(level));
                GlobalUserDataStore.getInstance().setDictLevelName("三级会员");
                break;
            case UserDictionary.MEMBER_LEVEL_4:
                GlobalUserDataStore.getInstance().setDictLevel(String.valueOf(level));
                GlobalUserDataStore.getInstance().setDictLevelName("四级会员");
                break;
            case UserDictionary.MEMBER_LEVEL_5:
                GlobalUserDataStore.getInstance().setDictLevel(String.valueOf(level));
                GlobalUserDataStore.getInstance().setDictLevelName("五级会员");
                break;
            case UserDictionary.MEMBER_LEVEL_6:
                GlobalUserDataStore.getInstance().setDictLevel(String.valueOf(level));
                GlobalUserDataStore.getInstance().setDictLevelName("六级会员");
                break;
        }
    }

}
