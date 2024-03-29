package com.askia.coremodel.datamodel.http.repository;

import com.askia.coremodel.datamodel.http.ApiClient;
import com.askia.coremodel.util.JsonUtil;
import com.askia.coremodel.util.SwitchSchedulers;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import okhttp3.ResponseBody;

/**
 * 动态url数据获取
 */

public class DynamicDataRepository {

    public static <T> Observable getDynamicData(String pullUrl, final Class<T> clazz) {

        return ApiClient
                .getDynamicDataService()
                .getDynamicData(pullUrl)
                .compose(SwitchSchedulers.applySchedulers())
                .map(new Function<ResponseBody, T>() {
                    @Override
                    public T apply(ResponseBody responseBody) throws Exception {
                        return JsonUtil.Str2JsonBean(responseBody.string(), clazz);
                    }
                });
    }

}