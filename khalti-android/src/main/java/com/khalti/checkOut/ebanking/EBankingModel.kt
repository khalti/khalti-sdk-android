package com.khalti.checkOut.ebanking


import java.util.HashMap

import com.khalti.checkOut.ebanking.helper.BankPojo
import com.khalti.checkOut.ebanking.helper.BaseListPojo
import com.khalti.checkOut.api.ApiHelper
import com.khalti.checkOut.api.ApiHelper_
import com.khalti.checkOut.api.KhaltiApi
import com.khalti.checkOut.api.KhaltiApi_
import com.khalti.checkOut.api.Result
import com.khalti.utils.EmptyUtil
import rx.Observable
import rx.Subscriber
import rx.subjects.PublishSubject
import rx.subscriptions.CompositeSubscription

internal class EBankingModel(mockedKhaltiApi: KhaltiApi_? = null) : EBankingContract.Model {
    private var khaltiApi: KhaltiApi_ = if (EmptyUtil.isNotNull(mockedKhaltiApi)) {
        mockedKhaltiApi!!
    } else {
        ApiHelper_.apiBuilder()
    }

    override suspend fun fetchBankList(): Result<BaseListPojo> {
        return ApiHelper_.callApi(khaltiApi.getBanks("/api/bank/",
                object : HashMap<String, Any>() {
                    init {
                        put("page", 1)
                        put("page_size", 200)
                        put("payment_type", "ebanking")
                    }
                }))
    }
}