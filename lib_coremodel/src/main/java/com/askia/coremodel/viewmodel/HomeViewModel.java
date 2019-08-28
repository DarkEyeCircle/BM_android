package com.askia.coremodel.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.askia.coremodel.datamodel.http.entities.NearSotreData;
import com.askia.coremodel.datamodel.http.entities.ResponseCode;
import com.askia.coremodel.datamodel.http.repository.GankDataRepository;
import com.askia.coremodel.datamodel.loaction.LocationData;
import com.askia.coremodel.util.NetUtils;
import com.baidu.location.BDLocation;

import java.net.SocketTimeoutException;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class HomeViewModel extends AndroidViewModel {
    private final CompositeDisposable mDisposable = new CompositeDisposable();
    private LocationData locationLiveData = null;
    private MutableLiveData<NearSotreData> nearStoreLiveData = new MutableLiveData<>();
    private PageData pageData = new PageData();

    public HomeViewModel(@NonNull Application application) {
        super(application);
    }

    public LocationData getLocationLiveData() {
        if (locationLiveData == null) {
            locationLiveData = LocationData.init(getApplication());
        }
        return locationLiveData;
    }

    public MutableLiveData<NearSotreData> getNearStoreLiveData() {
        return nearStoreLiveData;
    }

    public void getNearStoreList() {
        if (!NetUtils.isNetConnected()) {
            nearStoreLiveData.setValue(null);
            return;
        }
        if (LocationData.getLocatonData() == null) {
            return;
        }
        GankDataRepository.GetNearStores(-1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .safeSubscribe(new io.reactivex.Observer<NearSotreData>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onNext(NearSotreData responseData) {
                        nearStoreLiveData.setValue(responseData);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        NearSotreData responseData = new NearSotreData();
                        if (e instanceof SocketTimeoutException) {
                            responseData.setCode(ResponseCode.ConnectTimeOut);
                            responseData.setMsg("连接超时，请重试");
                        } else {
                            responseData.setCode(ResponseCode.ServerNotResponding);
                            responseData.setMsg("服务器无响应，请重试");
                        }
                        nearStoreLiveData.setValue(responseData);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    public PageData getPageData() {
        return pageData;
    }

    public class PageData {

        public PageData() {
            location.set("定位中...");
        }

        public void updateLocation(BDLocation bdLocation) {
            this.bdLocation = bdLocation;
            if (bdLocation != null) {
                String province = bdLocation.getProvince();
                String city = bdLocation.getCity();
                if (TextUtils.isEmpty(province) || TextUtils.isEmpty(city)) {
                    location.set("定位失败");
                } else {
                    location.set(province + city);
                }
            } else {
                location.set("定位失败");
            }
        }

        private ObservableField<String> location = new ObservableField<>();
        private BDLocation bdLocation;

        public BDLocation getBdLocation() {
            return bdLocation;
        }

        public void setBdLocation(BDLocation bdLocation) {
            this.bdLocation = bdLocation;
        }

        public ObservableField<String> getLocation() {
            return location;
        }

        public void setLocation(ObservableField<String> location) {
            this.location = location;
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mDisposable.clear();
    }
}
