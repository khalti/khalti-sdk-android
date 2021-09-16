package com.khalti.utils

import com.khalti.BuildConfig
import com.khalti.checkout.helper.Config
import java.util.HashMap

class PayloadUtil {

    companion object {

        fun buildPayload(mobile: String, bankId: String, bankName: String, paymentType: String, packageName: String, config: Config): String {

            val map = HashMap<String, Any>()
            map["mobile"] = mobile
            map["bankId"] = bankId
            map["bankName"] = bankName
            map["checkout_version"] = BuildConfig.VERSION_NAME
            map["checkout_android_version"] = AppUtil.getOsVersion()
            map["checkout_android_api_level"] = AppUtil.getApiLevel().toString()
            map["public_key"] = config.publicKey
            map["product_identity"] = config.productId
            map["product_name"] = config.productName
            map["amount"] = config.amount.toString()
            map["bank"] = bankId
            map["source"] = "android"
            map["return_url"] = packageName
            map["payment_type"] = paymentType

            if (EmptyUtil.isNotNull(config.productUrl) && EmptyUtil.isNotEmpty(config.productUrl)) {
                map["product_url"] = config.productUrl!!
            }

            if (EmptyUtil.isNotNull(config.additionalData)) {
                map.putAll(config.additionalData!!)
            }

            val data = EncodeUtil.urlEncode(map)

            return Constant.url + "ebanking/initiate/?" + data
        }
    }
}