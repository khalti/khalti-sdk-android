package com.khalti.checkOut.api


import com.khalti.checkOut.EBanking.helper.BaseListPojo
import com.khalti.checkOut.Wallet.helper.WalletConfirmPojo
import com.khalti.checkOut.Wallet.helper.WalletInitPojo
import com.khalti.checkOut.helper.MerchantPreferencePojo
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.*
import rx.Observable

interface KhaltiApi {

    @GET
    fun getBanks(@Url url: String, @QueryMap queryMap: Map<String, Any>): Observable<Response<BaseListPojo>>

    @GET
    fun getPreference(@Url url: String, @Header("Authorization") publicKey: String): Observable<Response<MerchantPreferencePojo>>

    @POST
    @FormUrlEncoded
    fun initiatePayment(@Url url: String, @FieldMap dataMap: Map<String, Any>): Observable<Response<WalletInitPojo>>

    @POST
    @FormUrlEncoded
    fun confirmPayment(@Url url: String, @FieldMap dataMap: Map<String, Any>): Observable<Response<WalletConfirmPojo>>
}