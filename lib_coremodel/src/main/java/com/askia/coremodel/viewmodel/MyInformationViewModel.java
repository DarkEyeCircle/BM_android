package com.askia.coremodel.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableField;
import android.text.TextUtils;

import com.askia.coremodel.GlobalUserDataStore;
import com.askia.coremodel.datamodel.http.entities.ResponseCode;
import com.askia.coremodel.datamodel.http.entities.User;
import com.askia.coremodel.datamodel.http.repository.GankDataRepository;
import com.askia.coremodel.util.NetUtils;

import java.net.SocketTimeoutException;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MyInformationViewModel extends ViewModel {

    private MutableLiveData<User> modifyHeadImgResponseData = new MutableLiveData<>();
    private MutableLiveData<User> modifySexResponseData = new MutableLiveData<>();
    private MutableLiveData<User> modifyBornDataResponseData = new MutableLiveData<>();

    private PageData pageData = new PageData();
    private final CompositeDisposable mDisposable = new CompositeDisposable();

    public MutableLiveData<User> getModifyHeadImgResponseData() {
        return modifyHeadImgResponseData;
    }

    public MutableLiveData<User> getModifySexResponseData() {
        return modifySexResponseData;
    }

    public MutableLiveData<User> getModifyBornDataResponseData() {
        return modifyBornDataResponseData;
    }


    public PageData getPageData() {
        return pageData;
    }

    public void modifyHeadImg() {
        if (!NetUtils.isNetConnected()) {
            modifyHeadImgResponseData.setValue(null);
            return;
        }
        GankDataRepository.ModifyUserInfoForHeadImg(pageData.getUrl())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .safeSubscribe(new Observer<User>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onNext(User responseData) {
                        modifyHeadImgResponseData.setValue(responseData);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        User responseData = new User();
                        if (e instanceof SocketTimeoutException) {
                            responseData.setCode(ResponseCode.ConnectTimeOut);
                            responseData.setMsg("连接超时，请重试");
                        } else {
                            responseData.setCode(ResponseCode.ServerNotResponding);
                            responseData.setMsg("服务器无响应，请重试");
                        }
                        modifyHeadImgResponseData.setValue(responseData);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }


    public void modifyBornData(String bornDate) {
        if (!NetUtils.isNetConnected()) {
            modifyBornDataResponseData.setValue(null);
            return;
        }
        GankDataRepository.ModifyUserInfoForBornData(bornDate)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .safeSubscribe(new Observer<User>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onNext(User responseData) {
                        modifyBornDataResponseData.setValue(responseData);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        User responseData = new User();
                        if (e instanceof SocketTimeoutException) {
                            responseData.setCode(ResponseCode.ConnectTimeOut);
                            responseData.setMsg("连接超时，请重试");
                        } else {
                            responseData.setCode(ResponseCode.ServerNotResponding);
                            responseData.setMsg("服务器无响应，请重试");
                        }
                        modifyBornDataResponseData.setValue(responseData);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }


    public void modifySex(int sexCode) {
        if (!NetUtils.isNetConnected()) {
            modifySexResponseData.setValue(null);
            return;
        }
        GankDataRepository.ModifyUserInfoForSex(sexCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .safeSubscribe(new Observer<User>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onNext(User responseData) {
                        modifySexResponseData.setValue(responseData);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        User responseData = new User();
                        if (e instanceof SocketTimeoutException) {
                            responseData.setCode(ResponseCode.ConnectTimeOut);
                            responseData.setMsg("连接超时，请重试");
                        } else {
                            responseData.setCode(ResponseCode.ServerNotResponding);
                            responseData.setMsg("服务器无响应，请重试");
                        }
                        modifySexResponseData.setValue(responseData);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    public void update(User.ObjBean objBean) {
        GlobalUserDataStore.getInstance().updateUserData(objBean);
        pageData.update();
    }

    public class PageData {

        public PageData() {
            update();
        }

        public void update() {
            GlobalUserDataStore globalUserDataStore = GlobalUserDataStore.getInstance();
            headImagUrl.set(globalUserDataStore.getAuthCode());
            nickName.set(globalUserDataStore.getNickName());
            memberId.set(globalUserDataStore.getIdentifier());
            sexStr.set(globalUserDataStore.getDictSexName());
            bornDate.set(globalUserDataStore.getBirthday());
            if (TextUtils.isEmpty(globalUserDataStore.getIntroducerName())) {
                introducerName.set("无");
            } else {
                introducerName.set(globalUserDataStore.getIntroducerName());
            }
        }

        //头像
        private ObservableField<String> headImagUrl = new ObservableField<>();
        //昵称
        private ObservableField<String> nickName = new ObservableField<>();
        //会员号
        private ObservableField<String> memberId = new ObservableField<>();
        //性别
        private ObservableField<String> sexStr = new ObservableField<>();
        //出生日期
        private ObservableField<String> bornDate = new ObservableField<>();
        //推荐人
        private ObservableField<String> introducerName = new ObservableField<>();

        private String url = null;

        public ObservableField<String> getHeadImagUrl() {
            return headImagUrl;
        }

        public void setHeadImagUrl(ObservableField<String> headImagUrl) {
            this.headImagUrl = headImagUrl;
        }

        public ObservableField<String> getNickName() {
            return nickName;
        }

        public void setNickName(ObservableField<String> nickName) {
            this.nickName = nickName;
        }

        public ObservableField<String> getMemberId() {
            return memberId;
        }

        public void setMemberId(ObservableField<String> memberId) {
            this.memberId = memberId;
        }

        public ObservableField<String> getSexStr() {
            return sexStr;
        }

        public void setSexStr(ObservableField<String> sexStr) {
            this.sexStr = sexStr;
        }

        public ObservableField<String> getBornDate() {
            return bornDate;
        }

        public void setBornDate(ObservableField<String> bornDate) {
            this.bornDate = bornDate;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public ObservableField<String> getIntroducerName() {
            return introducerName;
        }

        public void setIntroducerName(ObservableField<String> introducerName) {
            this.introducerName = introducerName;
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mDisposable.clear();
    }


}
