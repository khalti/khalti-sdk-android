package com.khalti.checkout.form.helper


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class WalletInitPojo {
    @SerializedName("token")
    @Expose
    val token: String? = null
    @SerializedName("pin_created")
    @Expose
    val isPinCreated: Boolean = false
    @SerializedName("pin_created_message")
    @Expose
    val pinCreatedMessage: String? = null
}