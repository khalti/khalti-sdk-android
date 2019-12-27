package com.khalti.checkOut.api

import java.io.Serializable

interface OnSuccessListener : Serializable {
    fun onSuccess(data: Map<String, Any>)
}