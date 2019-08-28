package com.askia.coremodel.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;

import com.askia.coremodel.datamodel.http.entities.AgencyData;
import com.askia.coremodel.datamodel.http.entities.BaseResponseData;
import com.askia.coremodel.datamodel.http.entities.ResponseCode;
import com.askia.coremodel.datamodel.http.repository.GankDataRepository;
import com.askia.coremodel.util.NetUtils;

import java.net.SocketTimeoutException;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AgencyViewModel extends ViewModel {
    private MutableLiveData<AgencyData> agencyInfoLiveData = new MutableLiveData<>();
    private MutableLiveData<BaseResponseData> awardLiveData = new MutableLiveData<>();
    private MutableLiveData<BaseResponseData> checkSecurityPwdLiveData = new MutableLiveData<>();
    private PageData pageData = new PageData();
    private final CompositeDisposable mDisposable = new CompositeDisposable();

    public MutableLiveData<AgencyData> getAgencyInfoLiveData() {
        return agencyInfoLiveData;
    }

    public MutableLiveData<BaseResponseData> getAwardLiveData() {
        return awardLiveData;
    }

    public MutableLiveData<BaseResponseData> getCheckSecurityPwdLiveData() {
        return checkSecurityPwdLiveData;
    }

    public AgencyViewModel() {
        getAgencyInfo();
    }

    public PageData getPageData() {
        return pageData;
    }

    public void update(AgencyData.ObjBean objBean) {
        pageData.getAreaTotalConsumption().set(objBean.getAreaTotalConsumption());
        pageData.getDictGradeName().set(objBean.getDictGradeName());
        pageData.getIntegral().set(objBean.getIntegral());
        pageData.getTradingProportion().set(String.valueOf((int) (Float.valueOf(objBean.getTradingProportion()) * 100)));
    }

    //领取奖励
    public void getAward() {
        if (!NetUtils.isNetConnected()) {
            awardLiveData.setValue(null);
            return;
        }
        GankDataRepository.GetAward(pageData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .safeSubscribe(new Observer<BaseResponseData>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onNext(BaseResponseData responseData) {
                        awardLiveData.setValue(responseData);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        BaseResponseData bandCardData = new BaseResponseData();
                        if (e instanceof SocketTimeoutException) {
                            bandCardData.setCode(ResponseCode.ConnectTimeOut);
                            bandCardData.setMsg("连接超时，请重试");
                        } else {
                            bandCardData.setCode(ResponseCode.ServerNotResponding);
                            bandCardData.setMsg("服务器无响应，请重试");
                        }
                        awardLiveData.setValue(bandCardData);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    //获取代理信息
    public void getAgencyInfo() {
        if (!NetUtils.isNetConnected()) {
            agencyInfoLiveData.setValue(null);
            return;
        }
        GankDataRepository.GetAgentInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .safeSubscribe(new Observer<AgencyData>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onNext(AgencyData responseData) {
                        agencyInfoLiveData.setValue(responseData);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        AgencyData agencyData = new AgencyData();
                        if (e instanceof SocketTimeoutException) {
                            agencyData.setCode(ResponseCode.ConnectTimeOut);
                            agencyData.setMsg("连接超时，请重试");
                        } else {
                            agencyData.setCode(ResponseCode.ServerNotResponding);
                            agencyData.setMsg("服务器无响应，请重试");
                        }
                        agencyInfoLiveData.setValue(agencyData);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    //验证安全支付密码
    public void checkSecurityPwd(String pwd) {
        if (!NetUtils.isNetConnected()) {
            checkSecurityPwdLiveData.setValue(null);
            return;
        }
        GankDataRepository.VerifySecurityPassword(pwd)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .safeSubscribe(new Observer<BaseResponseData>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onNext(BaseResponseData responseData) {
                        checkSecurityPwdLiveData.setValue(responseData);
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
                        checkSecurityPwdLiveData.setValue(responseData);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    public class PageData {

        private ObservableField<String> areaTotalConsumption = new ObservableField<>();

        private ObservableField<String> integral = new ObservableField<>();

        private ObservableField<String> dictGradeName = new ObservableField<>();

        private ObservableField<String> tradingProportion = new ObservableField<>();

        public ObservableField<String> getAreaTotalConsumption() {
            return areaTotalConsumption;
        }

        public void setAreaTotalConsumption(ObservableField<String> areaTotalConsumption) {
            this.areaTotalConsumption = areaTotalConsumption;
        }

        public ObservableField<String> getIntegral() {
            return integral;
        }

        public void setIntegral(ObservableField<String> integral) {
            this.integral = integral;
        }

        public ObservableField<String> getDictGradeName() {
            return dictGradeName;
        }

        public void setDictGradeName(ObservableField<String> dictGradeName) {
            this.dictGradeName = dictGradeName;
        }

        public ObservableField<String> getTradingProportion() {
            return tradingProportion;
        }

        public void setTradingProportion(ObservableField<String> tradingProportion) {
            this.tradingProportion = tradingProportion;
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mDisposable.clear();
    }
}
