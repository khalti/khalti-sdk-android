package com.khalti.checkOut.api


import androidx.annotation.Keep
import java.io.Serializable

import java.util.HashMap

@Keep
interface OnCheckOutListener : Serializable{
    fun onSuccess(data: Map<String, Any>)

//    fun onError(action: String, message: String)
}
