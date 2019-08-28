package com.askia.coremodel.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableField;

import com.apkfuns.logutils.LogUtils;
import com.askia.coremodel.datamodel.database.repository.DBRepository;
import com.askia.coremodel.datamodel.http.entities.BaseResponseData;
import com.askia.coremodel.datamodel.http.entities.ResponseCode;
import com.askia.coremodel.datamodel.http.repository.GankDataRepository;
import com.askia.coremodel.util.NetUtils;

import java.net.SocketTimeoutException;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ModifySecurityPwdViewModel extends ViewModel {

    private MutableLiveData<BaseResponseData> baseResponseData = new MutableLiveData<>();
    private final CompositeDisposable mDisposable = new CompositeDisposable();
    private PageData pageData = new PageData();

    public MutableLiveData<BaseResponseData> getBaseResponseData() {
        return baseResponseData;
    }

    public PageData getPageData() {
        return pageData;
    }

    //修改密码
    public void modifyPwd() {
        if (!NetUtils.isNetConnected()) {
            baseResponseData.setValue(null);
            return;
        }
        GankDataRepository.ModifySecurityPwd(pageData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .safeSubscribe(new Observer<BaseResponseData>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onNext(BaseResponseData value) {
                        baseResponseData.setValue(value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        BaseResponseData responseData = new BaseResponseData();
                        if (e instanceof SocketTimeoutException) {
                            responseData.setCode(ResponseCode.ConnectTimeOut);
                            responseData.setMsg("连接超时，请重试");
                        } else {
                            responseData.setCode(ResponseCode.ServerNotResponding);
                            responseData.setMsg("服务器无响应，请重试");
                        }
                        baseResponseData.setValue(responseData);
                    }

                    @Override
                    public void onComplete() {
                        LogUtils.d("onComplete");
                    }
                });
    }

    public void storeUserPwd() {
        //保存用户密码
        String newPassWord = pageData.getNewPassWord().get();
        DBRepository.UpdateUserPwd(newPassWord);
    }

    public class PageData {

        public PageData() {
            oldPassWord.set("");
            newPassWord.set("");
            confirmPwd.set("");
        }

        private ObservableField<String> oldPassWord = new ObservableField<>();

        private ObservableField<String> newPassWord = new ObservableField<>();

        private ObservableField<String> confirmPwd = new ObservableField<>();

        public ObservableField<String> getOldPassWord() {
            return oldPassWord;
        }

        public void setOldPassWord(ObservableField<String> oldPassWord) {
            this.oldPassWord = oldPassWord;
        }

        public ObservableField<String> getNewPassWord() {
            return newPassWord;
        }

        public void setNewPassWord(ObservableField<String> newPassWord) {
            this.newPassWord = newPassWord;
        }

        public ObservableField<String> getConfirmPwd() {
            return confirmPwd;
        }

        public void setConfirmPwd(ObservableField<String> confirmPwd) {
            this.confirmPwd = confirmPwd;
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mDisposable.clear();
    }
}
