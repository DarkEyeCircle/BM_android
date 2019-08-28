package com.askia.coremodel.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.askia.coremodel.datamodel.http.entities.AddressData;
import com.askia.coremodel.datamodel.http.entities.BandCardData;
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

public class AddressManagerViewModel extends ViewModel {

    private MutableLiveData<AddressData> addrListLiveData = new MutableLiveData<>();
    private MutableLiveData<BaseResponseData> delAddrLiveData = new MutableLiveData<>();
    private MutableLiveData<BaseResponseData> setDefaultAddressLiveData = new MutableLiveData<>();
    private final CompositeDisposable mDisposable = new CompositeDisposable();

    public AddressManagerViewModel() {
        getAddrListData();
    }

    public MutableLiveData<AddressData> getAddrListLiveData() {
        return addrListLiveData;
    }

    public MutableLiveData<BaseResponseData> getDelAddrLiveData() {
        return delAddrLiveData;
    }

    public MutableLiveData<BaseResponseData> getSetDefaultAddressLiveData() {
        return setDefaultAddressLiveData;
    }

    //获取地址信息
    public void getAddrListData() {
        if (!NetUtils.isNetConnected()) {
            addrListLiveData.setValue(null);
            return;
        }
        GankDataRepository.GetAddressData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .safeSubscribe(new Observer<AddressData>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onNext(AddressData responseData) {
                        addrListLiveData.setValue(responseData);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        AddressData addressData = new AddressData();
                        if (e instanceof SocketTimeoutException) {
                            addressData.setCode(ResponseCode.ConnectTimeOut);
                            addressData.setMsg("连接超时，请重试");
                        } else {
                            addressData.setCode(ResponseCode.ServerNotResponding);
                            addressData.setMsg("服务器无响应，请重试");
                        }
                        addrListLiveData.setValue(addressData);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    //删除地址
    public void delAddr(AddressData.ObjBean objBean) {
        if (!NetUtils.isNetConnected()) {
            delAddrLiveData.setValue(null);
            return;
        }
        GankDataRepository.DelUserAddress(objBean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .safeSubscribe(new Observer<BaseResponseData>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onNext(BaseResponseData responseData) {
                        delAddrLiveData.setValue(responseData);
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
                        delAddrLiveData.setValue(responseData);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    //设置为默认地址
    public void setDefaultAddr(AddressData.ObjBean item) {
        if (!NetUtils.isNetConnected()) {
            setDefaultAddressLiveData.setValue(null);
            return;
        }
        GankDataRepository.SetDefaultAddress(item)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .safeSubscribe(new Observer<BaseResponseData>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onNext(BaseResponseData responseData) {
                        setDefaultAddressLiveData.setValue(responseData);
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
                        setDefaultAddressLiveData.setValue(responseData);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mDisposable.clear();
    }
}
