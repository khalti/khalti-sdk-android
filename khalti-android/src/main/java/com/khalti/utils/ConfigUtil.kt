package com.khalti.utils

import com.khalti.checkOut.api.Config

class ConfigUtil {

    companion object {

        fun validateConfig(config: Config): String {
            if (EmptyUtil.isNull(config.publicKey)) {
                return "Public key cannot be null"
            }
            if (EmptyUtil.isEmpty(config.publicKey)) {
                return "Public key cannot be empty"
            }
            if (EmptyUtil.isNull(config.productId)) {
                return "Product identity cannot be null"
            }
            if (EmptyUtil.isEmpty(config.productId)) {
                return "Product identity cannot be empty"
            }
            if (EmptyUtil.isNull(config.productName)) {
                return "Product name cannot be null"
            }
            if (EmptyUtil.isEmpty(config.productName)) {
                return "Product name cannot be empty"
            }
            if (EmptyUtil.isNull(config.amount)) {
                return "Amount cannot be null"
            }
            if (EmptyUtil.isEmpty(config.amount)) {
                return "Amount cannot be 0"
            }
            return if (EmptyUtil.isNull(config.onCheckOutListener)) {
                "Listener cannot be null"
            } else ""
        }
    }
}