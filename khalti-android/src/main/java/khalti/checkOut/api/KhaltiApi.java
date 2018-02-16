package khalti.checkOut.api;


import java.util.HashMap;
import java.util.Map;

import khalti.checkOut.EBanking.helper.BaseListPojo;
import khalti.checkOut.Wallet.helper.WalletConfirmPojo;
import khalti.checkOut.Wallet.helper.WalletInitPojo;
import khalti.checkOut.helper.MerchantPreferencePojo;
import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import rx.Observable;

public interface KhaltiApi {
    @GET
    Observable<Response<BaseListPojo>> getBanks(@Url String url, @QueryMap Map<String, Object> queryMap);

    @GET
    Observable<Response<MerchantPreferencePojo>> getPreference(@Url String url, @Header("Authorization") String publicKey);

    @POST
    @FormUrlEncoded
    Observable<Response<WalletInitPojo>> initiatePayment(@Url String url, @FieldMap HashMap<String, Object> dataMap);

    @POST
    @FormUrlEncoded
    Observable<Response<WalletConfirmPojo>> confirmPayment(@Url String url, @FieldMap HashMap<String, Object> dataMap);
}
