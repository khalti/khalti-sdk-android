package com.khalti.utils

import android.content.Context
import com.khalti.checkOut.helper.Config

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
            if (EmptyUtil.isNull(config.onSuccessListener)) {
                return "Success listener should be set and cannot be null"
            }
            if (EmptyUtil.isNull(config.onErrorListener)) {
                return "Error listener should be set and cannot be null"
            }

            return ""
        }

        fun validateIfConfigIsSerializable(context: Context, config: Config): String {
            if (FileStorageUtil.writeIntoFile(context, "Config", config)) {
                return "Success Listener, Error Listener and Additional data needs to be serializable. Please make sure the values you pass can be serialized"
            }
            return ""
        }
    }
}