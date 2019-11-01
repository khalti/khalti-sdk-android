package com.khalti.checkOut.api


import com.khalti.checkOut.EBanking.helper.BaseListPojo
import com.khalti.checkOut.Wallet.helper.WalletConfirmPojo
import com.khalti.checkOut.Wallet.helper.WalletInitPojo
import com.khalti.checkOut.helper.MerchantPreferencePojo
import retrofit2.Response
import retrofit2.http.*

interface KhaltiApi_ {

    @GET
    suspend fun getBanks(@Url url: String, @QueryMap queryMap: Map<String, Any>): Response<BaseListPojo>

    @GET
    suspend fun getPreference(@Url url: String, @Header("Authorization") publicKey: String): Response<MerchantPreferencePojo>

    @POST
    @FormUrlEncoded
    suspend fun initiatePayment(@Url url: String, @FieldMap dataMap: Map<String, Any>): Response<WalletInitPojo>

    @POST
    @FormUrlEncoded
    suspend fun confirmPayment(@Url url: String, @FieldMap dataMap: Map<String, Any>): Response<WalletConfirmPojo>
}