package com.askia.coremodel.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

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

public class AddAddressViewModel extends ViewModel {

    private MutableLiveData<BaseResponseData> addAddressLiveData = new MutableLiveData<>();
    private PageData pageData = null;
    private final CompositeDisposable mDisposable = new CompositeDisposable();

    public PageData getPageData() {
        return pageData;
    }

    public MutableLiveData<BaseResponseData> getAddAddressLiveData() {
        return addAddressLiveData;
    }

    public AddAddressViewModel() {
        pageData = new PageData();
    }

    public void addUserAddress() {
        if (!NetUtils.isNetConnected()) {
            addAddressLiveData.setValue(null);
            return;
        }
        GankDataRepository.AddUserAddress(pageData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .safeSubscribe(new Observer<BaseResponseData>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onNext(BaseResponseData responseData) {
                        addAddressLiveData.setValue(responseData);
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
                        addAddressLiveData.setValue(responseData);
                    }

                    @Override
                    public void onComplete() {
                    }
                });

    }

    public class PageData {

        public PageData() {
            name.set("");
            phoneNum.set("");
            area.set("");
            detailAddr.set("");
            defaultAddr.set(false);
        }

        private ObservableField<String> name = new ObservableField<>();
        private ObservableField<String> phoneNum = new ObservableField<>();
        private ObservableField<String> area = new ObservableField<>();
        private ObservableField<String> detailAddr = new ObservableField<>();
        private ObservableBoolean defaultAddr = new ObservableBoolean();

        public ObservableField<String> getName() {
            return name;
        }

        public void setName(ObservableField<String> name) {
            this.name = name;
        }

        public ObservableField<String> getPhoneNum() {
            return phoneNum;
        }

        public void setPhoneNum(ObservableField<String> phoneNum) {
            this.phoneNum = phoneNum;
        }

        public ObservableField<String> getArea() {
            return area;
        }

        public void setArea(ObservableField<String> area) {
            this.area = area;
        }

        public ObservableField<String> getDetailAddr() {
            return detailAddr;
        }

        public void setDetailAddr(ObservableField<String> detailAddr) {
            this.detailAddr = detailAddr;
        }

        public ObservableBoolean getDefaultAddr() {
            return defaultAddr;
        }

        public void setDefaultAddr(ObservableBoolean defaultAddr) {
            this.defaultAddr = defaultAddr;
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mDisposable.clear();
    }
}
