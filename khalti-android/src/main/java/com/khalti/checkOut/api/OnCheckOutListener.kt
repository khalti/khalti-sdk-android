package com.khalti.checkOut.api


import androidx.annotation.Keep

import java.util.HashMap

@Keep
interface OnCheckOutListener {
    fun onSuccess(data: Map<String, Any>)

    fun onError(action: String, message: String)
}
