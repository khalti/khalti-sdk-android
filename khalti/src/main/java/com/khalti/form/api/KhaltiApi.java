package com.khalti.form.api;


import com.khalti.form.EBanking.BaseListPojo;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

public interface KhaltiApi {
    @GET
    Observable<Response<BaseListPojo>> getBanks(@Url String url, @Query("page") Integer page, @Query("page_size") Integer pageSize, @Query("has_ebanking") boolean hasEBanking);
}
