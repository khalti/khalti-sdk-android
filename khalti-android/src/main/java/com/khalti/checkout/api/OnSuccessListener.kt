package com.khalti.checkout.api

import java.io.Serializable

interface OnSuccessListener : Serializable {
    fun onSuccess(data: Map<String, Any>)
}