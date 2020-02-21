package com.khalti.checkout.helper


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import java.util.ArrayList

class MerchantPreferencePojo {

    @SerializedName("sdk_position")
    @Expose
    val sdkPosition: List<String> = ArrayList()
}