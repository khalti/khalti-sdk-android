package com.khalti.checkOut.banking

import com.khalti.checkOut.api.ErrorAction
import com.khalti.checkOut.api.Result
import com.khalti.checkOut.banking.helper.BankingData
import com.khalti.checkOut.helper.Config
import com.khalti.signal.CompositeSignal
import com.khalti.utils.EmptyUtil
import com.khalti.utils.GuavaUtil
import com.khalti.utils.LogUtil
import com.khalti.utils.Store
import kotlinx.coroutines.*

class BankingPresenter(view: BankingContract.View) : BankingContract.Presenter {

    private val view: BankingContract.View = GuavaUtil.checkNotNull(view)
    private var model: BankingModel = BankingModel()
    private lateinit var config: Config
    private val compositeSignal = CompositeSignal()

    private val parentJob = Job()
    private val scope = CoroutineScope(Dispatchers.Main + parentJob)

    private var map: Map<String, Any>? = null
    private var paymentType = "ebanking"
    private var paymentTypeInit = "ebanking"

    init {
        view.setPresenter(this)
    }

    override fun onCreate() {
        this.config = Store.getConfig()

        map = view.receiveData()

        if (EmptyUtil.isNotNull(map)) {
            if (EmptyUtil.isNotNull(map!!["payment_type"])) {
                val type = map!!["payment_type"]
                paymentType = type!!.toString()
                paymentTypeInit = if ("mobile" == paymentType) {
                    "mobilecheckout"
                } else {
                    paymentType
                }
            }
            val clickMap = view.setOnClickListener()
            if (EmptyUtil.isNotNull(clickMap["try_again"])) {
                compositeSignal.add(clickMap.getValue("try_again")
                        .connect { onFetch() })
            }
            onFetch()
        }
    }

    override fun onFetch() {
        if (view.hasNetwork()) {
            view.toggleIndented(true)
            scope.launch {
                when (val result = model.fetchBankList(paymentType)) {
                    is Result.Success -> {
                        view.toggleIndented(false)
                        compositeSignal.add(view.setupList(result.data.records)
                                .connect {
                                    view.openMobileForm(BankingData(it.getValue("idx"), it.getValue("name"), it.getValue("logo"), it.getValue("icon"), paymentTypeInit, config))
                                })
                        compositeSignal.add(view.setSearchListener()
                                .connect { t ->
                                    if (t.first == paymentType) {
                                        val count = view.filterList(t.second)
//                                    view.toggleSearchError(count == 0)
                                    }
                                })
                        view.toggleSearch(result.data.records.size > 3)
                    }
                    is Result.Error -> {
                        val message = result.throwable.message
                        if (EmptyUtil.isNotNull(message) && EmptyUtil.isNotNull(config.onErrorListener)) {
                            view.showIndentedError(message!!)
                            config.onErrorListener!!.onError(ErrorAction.FETCH_BANK_LIST.action, message)
                        }
                    }
                }
            }

        } else {
            view.showIndentedNetworkError()
        }
    }

    override fun onDestroy() {
        compositeSignal.clear()
        parentJob.cancel()
    }
}