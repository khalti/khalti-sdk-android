package com.khalti.utils

import com.khalti.checkout.helper.Config

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
            if (EmptyUtil.isNull(config.onCheckOutListener)) {
                return "CheckOut listener should be set and cannot be null"
            }

            return ""
        }
    }
}