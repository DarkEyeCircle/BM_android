package com.askia.coremodel.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableField;

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

public class ModifyNickNameViewModel extends ViewModel {

    private final CompositeDisposable mDisposable = new CompositeDisposable();
    private MutableLiveData<User> modifyNickNameResponseData = new MutableLiveData<>();
    private PageData pageData;

    public PageData getPageData() {
        return pageData;
    }

    public ModifyNickNameViewModel() {
        pageData = new PageData();
    }

    public MutableLiveData<User> getModifyNickNameResponseData() {
        return modifyNickNameResponseData;
    }

    public void update(User.ObjBean objBean) {
        GlobalUserDataStore.getInstance().updateUserData(objBean);
    }

    public void modifyHeadImg(String nickName) {
        if (!NetUtils.isNetConnected()) {
            modifyNickNameResponseData.setValue(null);
            return;
        }
        GankDataRepository.ModifyUserInfoForNickName(nickName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .safeSubscribe(new Observer<User>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onNext(User responseData) {
                        modifyNickNameResponseData.setValue(responseData);
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
                        modifyNickNameResponseData.setValue(responseData);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    public class PageData {

        private ObservableField<String> nickName = new ObservableField<>();
        private ObservableField<String> inputConut = new ObservableField<>();


        public PageData() {
            nickName.set("");
            inputConut.set("0");
        }

        public ObservableField<String> getNickName() {
            return nickName;
        }

        public void setNickName(ObservableField<String> nickName) {
            this.nickName = nickName;
        }

        public ObservableField<String> getInputConut() {
            return inputConut;
        }

        public void setInputConut(ObservableField<String> inputConut) {
            this.inputConut = inputConut;
        }
    }


}
