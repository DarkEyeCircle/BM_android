package com.askia.coremodel.datamodel.http.service;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Url;


public interface DynamicApiService {

    @GET
    Observable<ResponseBody> getDynamicData(@Url String fullUrl);

}
