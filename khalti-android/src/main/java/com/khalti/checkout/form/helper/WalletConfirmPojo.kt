package com.khalti.checkout.form.helper


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class WalletConfirmPojo {
    @SerializedName("idx")
    @Expose
    val idx: String? = null
    @SerializedName("product_identity")
    @Expose
    val productIdentity: String? = null
    @SerializedName("token")
    @Expose
    val token: String? = null
    @SerializedName("amount")
    @Expose
    val amount: Int? = null
    @SerializedName("product_name")
    @Expose
    val productName: String? = null
    @SerializedName("product_url")
    @Expose
    val productUrl: String? = null
    @SerializedName("mobile")
    @Expose
    val mobile: String? = null
}
