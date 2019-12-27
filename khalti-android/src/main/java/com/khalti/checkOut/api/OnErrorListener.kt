package com.khalti.checkOut.api

import java.io.Serializable

interface OnErrorListener : Serializable {
    fun onError(action: String, message: String)
}