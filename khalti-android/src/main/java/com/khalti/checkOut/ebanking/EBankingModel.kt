package com.khalti.checkOut.ebanking


import java.util.HashMap

import com.khalti.checkOut.ebanking.helper.BaseListPojo
import com.khalti.checkOut.api.ApiHelper
import com.khalti.checkOut.api.KhaltiApi
import com.khalti.checkOut.api.Result
import com.khalti.utils.EmptyUtil

internal class EBankingModel(mockedKhaltiApi: KhaltiApi? = null) : EBankingContract.Model {
    private var khaltiApi: KhaltiApi = if (EmptyUtil.isNotNull(mockedKhaltiApi)) {
        mockedKhaltiApi!!
    } else {
        ApiHelper.apiBuilder()
    }

    override suspend fun fetchBankList(): Result<BaseListPojo> {
        return ApiHelper.callApi(khaltiApi.getBanks("/api/bank/",
                object : HashMap<String, Any>() {
                    init {
                        put("page", 1)
                        put("page_size", 200)
                        put("payment_type", "ebanking")
                    }
                }))
    }
}