package com.khalti.checkout.banking

import com.khalti.checkout.api.ErrorAction
import com.khalti.checkout.api.Result
import com.khalti.checkout.banking.helper.BankingData
import com.khalti.checkout.helper.Config
import com.khalti.signal.CompositeSignal
import com.khalti.utils.EmptyUtil
import com.khalti.utils.GuavaUtil
import com.khalti.utils.JsonUtil
import com.khalti.utils.Store
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class BankingPresenter(view: BankingContract.View) : BankingContract.Presenter {

    private val view: BankingContract.View = GuavaUtil.checkNotNull(view)
    private var model: BankingModel = BankingModel()
    private lateinit var config: Config
    private val compositeSignal = CompositeSignal()

    private val parentJob = Job()
    private val scope = CoroutineScope(Dispatchers.Main + parentJob)

    private var map: Map<String, Any>? = null
    private var paymentType = "ebanking"

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
                        val banks = result.data.records
                        val distinctBanks = banks.distinctBy { it.name }

                        compositeSignal.add(view.setupList(distinctBanks.toMutableList()).connect {
                            view.openMobileForm(BankingData(it.getValue("idx"), it.getValue("name"), it.getValue("logo"), it.getValue("icon"), paymentType, config))
                        })
                        compositeSignal.add(view.setupSearch(paymentType)
                                .connect {
                                    view.filterList(it)
                                })
                    }
                    is Result.Error -> {
                        val message = result.throwable.message
                        if (EmptyUtil.isNotNull(message) && EmptyUtil.isNotNull(config.onCheckOutListener)) {
                            val errorMap = JsonUtil.convertJsonStringToMap(message!!)
                            view.showIndentedError(errorMap.getValue("detail"))
                            config.onCheckOutListener.onError(ErrorAction.FETCH_BANK_LIST.action, errorMap)
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