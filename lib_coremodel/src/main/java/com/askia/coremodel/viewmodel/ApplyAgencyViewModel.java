package com.askia.coremodel.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableField;

import com.askia.coremodel.GlobalUserDataStore;
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

public class ApplyAgencyViewModel extends ViewModel {

    public enum AgencyLevel {
        AGENCY_GRADE_ONE("全国级"),
        AGENCY_GRADE_TWO("省级"),
        AGENCY_GRADE_THREE("市级"),
        AGENCY_GRADE_FOUR("区级"),
        AGENCY_GRADE_Five("经纪人");


        private final String type;

        AgencyLevel(String type) {
            this.type = type;
        }

        public static AgencyLevel getAgencyType(String type) {
            switch (type) {
                case "全国级":
                    return AGENCY_GRADE_ONE;
                case "省级":
                    return AGENCY_GRADE_TWO;
                case "市级":
                    return AGENCY_GRADE_THREE;
                case "区级":
                    return AGENCY_GRADE_FOUR;
                case "经纪人":
                    return AGENCY_GRADE_Five;
                default:
                    return null;
            }
        }

        public String getType() {
            return type;
        }
    }

    private MutableLiveData<BaseResponseData> applyAgencyLiveData = new MutableLiveData<>();
    private final CompositeDisposable mDisposable = new CompositeDisposable();
    private PageData pageData;

    public ApplyAgencyViewModel() {
        this.pageData = new PageData();
    }

    public MutableLiveData<BaseResponseData> getApplyAgencyLiveData() {
        return applyAgencyLiveData;
    }

    public PageData getPageData() {
        return pageData;
    }

    public void applyAgency() {
        if (!NetUtils.isNetConnected()) {
            applyAgencyLiveData.setValue(null);
            return;
        }
        GankDataRepository.ApplyAgency(pageData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .safeSubscribe(new Observer<BaseResponseData>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onNext(BaseResponseData responseData) {
                        applyAgencyLiveData.setValue(responseData);
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
                        applyAgencyLiveData.setValue(responseData);
                    }

                    @Override
                    public void onComplete() {
                    }
                });

    }


    public class PageData {

        public PageData() {
            phoneNum.set(GlobalUserDataStore.getInstance().getMobile());
            realName.set(GlobalUserDataStore.getInstance().getRealName());
            IDCardNum.set(GlobalUserDataStore.getInstance().getIdCard());
            curCity.set(GlobalUserDataStore.getInstance().getArea());
            agencyLevel.set("");
            agencyArea.set("");
        }

        private ObservableField<String> realName = new ObservableField<>();

        private ObservableField<String> phoneNum = new ObservableField<>();

        private ObservableField<String> IDCardNum = new ObservableField<>();

        private ObservableField<String> curCity = new ObservableField<>();

        private ObservableField<String> agencyLevel = new ObservableField<>();

        private ObservableField<String> agencyArea = new ObservableField<>();

        public ObservableField<String> getRealName() {
            return realName;
        }

        public void setRealName(ObservableField<String> realName) {
            this.realName = realName;
        }

        public ObservableField<String> getPhoneNum() {
            return phoneNum;
        }

        public void setPhoneNum(ObservableField<String> phoneNum) {
            this.phoneNum = phoneNum;
        }

        public ObservableField<String> getIDCardNum() {
            return IDCardNum;
        }

        public void setIDCardNum(ObservableField<String> IDCardNum) {
            this.IDCardNum = IDCardNum;
        }

        public ObservableField<String> getCurCity() {
            return curCity;
        }

        public void setCurCity(ObservableField<String> curCity) {
            this.curCity = curCity;
        }

        public ObservableField<String> getAgencyLevel() {
            return agencyLevel;
        }

        public void setAgencyLevel(ObservableField<String> agencyLevel) {
            this.agencyLevel = agencyLevel;
        }

        public ObservableField<String> getAgencyArea() {
            return agencyArea;
        }

        public void setAgencyArea(ObservableField<String> agencyArea) {
            this.agencyArea = agencyArea;
        }

    }
}
