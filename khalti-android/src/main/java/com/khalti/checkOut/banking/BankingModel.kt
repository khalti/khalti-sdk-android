package com.khalti.checkOut.banking


import java.util.HashMap

import com.khalti.checkOut.banking.helper.BaseListPojo
import com.khalti.checkOut.api.ApiHelper
import com.khalti.checkOut.api.KhaltiApi
import com.khalti.checkOut.api.Result
import com.khalti.utils.EmptyUtil

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