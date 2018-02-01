package khalti.checkOut.api;


import khalti.checkOut.EBanking.helper.BaseListPojo;
import khalti.checkOut.Wallet.WalletConfirmPojo;
import khalti.checkOut.Wallet.WalletInitPojo;

import java.util.HashMap;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

public interface KhaltiApi {
    @GET
    Observable<Response<BaseListPojo>> getBanks(@Url String url, @Query("page") Integer page, @Query("page_size") Integer pageSize, @Query("has_ebanking") boolean hasEBanking);

    @POST
    @FormUrlEncoded
    Observable<Response<WalletInitPojo>> initiatePayment(@Url String url, @FieldMap HashMap<String, Object> dataMap);

    @POST
    @FormUrlEncoded
    Observable<Response<WalletConfirmPojo>> confirmPayment(@Url  String url, @FieldMap HashMap<String, Object> dataMap);
}
