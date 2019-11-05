package com.khalti.checkOut.wallet

import com.khalti.checkOut.wallet.helper.WalletConfirmPojo
import com.khalti.checkOut.wallet.helper.WalletInitPojo
import com.khalti.checkOut.api.ApiHelper_
import com.khalti.checkOut.api.KhaltiApi_
import com.khalti.checkOut.api.Result
import com.khalti.checkOut.api.Urls
import com.khalti.checkOut.helper.Config
import com.khalti.utils.EmptyUtil
import com.khalti.utils.Store

class WalletModel(mockedKhaltiApi: KhaltiApi_? = null) : WalletContract.Model {

    private var khaltiApi: KhaltiApi_ = if (EmptyUtil.isNotNull(mockedKhaltiApi)) {
        mockedKhaltiApi!!
    } else {
        ApiHelper_.apiBuilder()
    }
    private var apiHelper: ApiHelper_ = ApiHelper_()

    override suspend fun initiatePayment(mobile: String, config: Config): Result<WalletInitPojo> {

        val dataMap = HashMap<String, Any>()
        dataMap["public_key"] = config.publicKey
        dataMap["product_identity"] = config.productId
        dataMap["product_name"] = config.productName
        if (EmptyUtil.isNotNull(config.productUrl) && EmptyUtil.isNotEmpty(config.productUrl)) {
            dataMap["product_url"] = config.productUrl!!
        }
        dataMap["amount"] = config.amount
        dataMap["mobile"] = mobile
        if (EmptyUtil.isNotNull(config.additionalData)) {
            dataMap.putAll(config.additionalData!!)
        }

        return apiHelper.callApi(khaltiApi.initiatePayment(Urls.WALLET_INITIATE.value, dataMap))
    }

    override suspend fun confirmPayment(confirmationCode: String, transactionPIN: String, token: String): Result<WalletConfirmPojo> {

        val dataMap = HashMap<String, Any>()
        dataMap["token"] = token
        dataMap["confirmation_code"] = confirmationCode
        dataMap["transaction_pin"] = transactionPIN
        dataMap["public_key"] = Store.getConfig().publicKey

        return apiHelper.callApi(khaltiApi.confirmPayment(Urls.WALLET_CONFIRM.value, dataMap))
    }
}