package com.askia.coremodel.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

import com.askia.coremodel.GlobalUserDataStore;
import com.askia.coremodel.datamodel.http.entities.AgencyData;
import com.askia.coremodel.datamodel.http.entities.BaseResponseData;
import com.askia.coremodel.datamodel.http.entities.ResponseCode;
import com.askia.coremodel.datamodel.http.parm.UserDictionary;
import com.askia.coremodel.datamodel.http.repository.GankDataRepository;
import com.askia.coremodel.util.NetUtils;

import java.net.SocketTimeoutException;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.http.PATCH;

public class ApplyAgencyStateViewModel extends ViewModel {

    private MutableLiveData<BaseResponseData> applyAgencyLiveData = new MutableLiveData<>();
    private final CompositeDisposable mDisposable = new CompositeDisposable();
    private PageData pageData;

    public ApplyAgencyStateViewModel() {
        this.pageData = new PageData();
    }

    public MutableLiveData<BaseResponseData> getApplyAgencyLiveData() {
        return applyAgencyLiveData;
    }

    public PageData getPageData() {
        return pageData;
    }


    public void init(AgencyData.ObjBean objBean) {
        pageData.getPhoneNum().set(objBean.getMobile());
        pageData.getRealName().set(objBean.getRealName());
        pageData.getIDCardNum().set(objBean.getIdCard());
        pageData.getCurCity().set(objBean.getUserArea());
        pageData.getAgencyArea().set(objBean.getArea());
        pageData.getAgencyLevel().set(objBean.getDictGradeName());
        pageData.setAgencyLevelCode(objBean.getDictGrade());
        pageData.getAuditState().set(objBean.getDictStatusName());
        if (Integer.valueOf(objBean.getDictStatus()) == UserDictionary.APPLY_AGENCY_FAILER) {
            pageData.getAuditFailer().set(true);
        } else {
            pageData.getAuditFailer().set(false);
        }
        pageData.getMsg().set(objBean.getComment());
    }

    public void applyAgencyAgain() {
        if (!NetUtils.isNetConnected()) {
            applyAgencyLiveData.setValue(null);
            return;
        }
        GankDataRepository.ApplyAgencyAgain(pageData)
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

        private String agencyLevelCode; //代理等级Code
        private ObservableField<String> realName = new ObservableField<>();
        //审核状态
        private ObservableField<String> auditState = new ObservableField<>();
        //是否审核失败
        private ObservableBoolean auditFailer = new ObservableBoolean();
        private ObservableField<String> msg = new ObservableField<>(); //审核失败原因
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

        public String getAgencyLevelCode() {
            return agencyLevelCode;
        }

        public void setAgencyLevelCode(String agencyLevelCode) {
            this.agencyLevelCode = agencyLevelCode;
        }

        public ObservableField<String> getAuditState() {
            return auditState;
        }

        public void setAuditState(ObservableField<String> auditState) {
            this.auditState = auditState;
        }

        public ObservableBoolean getAuditFailer() {
            return auditFailer;
        }

        public void setAuditFailer(ObservableBoolean auditFailer) {
            this.auditFailer = auditFailer;
        }

        public ObservableField<String> getMsg() {
            return msg;
        }

        public void setMsg(ObservableField<String> msg) {
            this.msg = msg;
        }
    }
}
