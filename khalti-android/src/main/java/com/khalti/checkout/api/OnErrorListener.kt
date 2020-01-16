package com.khalti.checkout.api

import java.io.Serializable

interface OnErrorListener : Serializable {
    fun onError(action: String, message: String)
}