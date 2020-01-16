package com.khalti.checkout.banking


import com.khalti.checkout.api.ApiHelper
import com.khalti.checkout.api.KhaltiApi
import com.khalti.checkout.api.Result
import com.khalti.checkout.banking.helper.BaseListPojo
import com.khalti.utils.EmptyUtil
import java.util.*

internal class BankingModel(mockedKhaltiApi: KhaltiApi? = null) : BankingContract.Model {
    private var khaltiApi: KhaltiApi = if (EmptyUtil.isNotNull(mockedKhaltiApi)) {
        mockedKhaltiApi!!
    } else {
        ApiHelper.apiBuilder()
    }

    override suspend fun fetchBankList(paymentType: String): Result<BaseListPojo> {
        return ApiHelper.callApi(khaltiApi.getBanks("/api/bank/",
                object : HashMap<String, Any>() {
                    init {
                        put("page", 1)
                        put("page_size", 200)
                        put("payment_type", paymentType)
                    }
                }))
    }
}