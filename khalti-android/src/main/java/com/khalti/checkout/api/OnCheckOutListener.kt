package com.khalti.checkout.api


import androidx.annotation.Keep
import java.io.Serializable

@Keep
interface OnCheckOutListener : Serializable{
    fun onSuccess(data: Map<String, Any>)

//    fun onError(action: String, message: String)
}
