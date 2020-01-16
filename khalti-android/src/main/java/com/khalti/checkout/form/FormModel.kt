package com.khalti.checkout.form

import com.khalti.checkout.form.helper.WalletConfirmPojo
import com.khalti.checkout.form.helper.WalletInitPojo
import com.khalti.checkout.api.ApiHelper
import com.khalti.checkout.api.KhaltiApi
import com.khalti.checkout.api.Result
import com.khalti.checkout.api.Urls
import com.khalti.checkout.helper.Config
import com.khalti.utils.EmptyUtil
import com.khalti.utils.Store
import java.util.*

class FormModel(mockedKhaltiApi: KhaltiApi? = null) : FormContract.Model {

    private var khaltiApi: KhaltiApi = if (EmptyUtil.isNotNull(mockedKhaltiApi)) {
        mockedKhaltiApi!!
    } else {
        ApiHelper.apiBuilder()
    }

    override suspend fun initiatePayment(mobile: String, pin: String, config: Config): Result<WalletInitPojo> {

        val dataMap = HashMap<String, Any>()
        dataMap["public_key"] = config.publicKey
        dataMap["product_identity"] = config.productId
        dataMap["product_name"] = config.productName
        if (EmptyUtil.isNotNull(config.productUrl) && EmptyUtil.isNotEmpty(config.productUrl)) {
            dataMap["product_url"] = config.productUrl!!
        }
        dataMap["amount"] = config.amount
        dataMap["mobile"] = mobile
        dataMap["transaction_pin"] = pin
        if (EmptyUtil.isNotNull(config.additionalData)) {
            dataMap.putAll(config.additionalData!!)
        }

        return ApiHelper.callApi(khaltiApi.initiatePayment(Urls.WALLET_INITIATE.value, dataMap))
    }

    override suspend fun confirmPayment(confirmationCode: String, pin: String, token: String): Result<WalletConfirmPojo> {

        val dataMap = HashMap<String, Any>()
        dataMap["token"] = token
        dataMap["confirmation_code"] = confirmationCode
        dataMap["transaction_pin"] = pin
        dataMap["public_key"] = Store.getConfig().publicKey

        return ApiHelper.callApi(khaltiApi.confirmPayment(Urls.WALLET_CONFIRM.value, dataMap))
    }
}