package com.khalti.checkOut.ebanking

import com.khalti.checkOut.api.ErrorAction
import com.khalti.checkOut.api.Result
import com.khalti.checkOut.ebanking.helper.BankingData
import com.khalti.checkOut.helper.Config
import com.khalti.signal.CompositeSignal
import com.khalti.utils.EmptyUtil
import com.khalti.utils.GuavaUtil
import com.khalti.utils.LogUtil
import com.khalti.utils.Store
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class EBankingPresenter(view: EBankingContract.View) : EBankingContract.Presenter {

    private val view: EBankingContract.View = GuavaUtil.checkNotNull(view)
    private var model: EBankingModel = EBankingModel()
    private lateinit var config: Config
    private val compositeSignal = CompositeSignal()

    private val parentJob = Job()
    private val scope = CoroutineScope(Dispatchers.Main + parentJob)

    init {
        view.setPresenter(this)
    }

    override fun onCreate() {
        this.config = Store.getConfig()

        val clickMap = view.setOnClickListener()
        if (EmptyUtil.isNotNull(clickMap["try_again"])) {
            onFetch()
        }
    }

    override fun onFetch() {
        if (view.hasNetwork()) {
            view.toggleIndented(true)
            scope.launch {

                when (val result = model.fetchBankList()) {
                    is Result.Success -> {
                        view.toggleIndented(false)
                        compositeSignal.add(view.setupList(result.data.records)
                                .connect {
                                    view.openMobileForm(BankingData(it.getValue("idx"), it.getValue("name"), it.getValue("logo"), it.getValue("icon"), config))
                                })
                        compositeSignal.add(view.setSearchListener()
                                .connect { t ->
                                    val count = view.filterList(t)
                                    LogUtil.log("i", count)
                                    view.toggleSearchError(count == 0)
                                })
                        view.toggleSearch(result.data.records.size > 3)
                    }
                    is Result.Error -> {
                        val message = result.throwable.message
                        if (EmptyUtil.isNotNull(message)) {
                            view.showIndentedError(message!!)
//                            config.onCheckOutListener.onError(ErrorAction.FETCH_BANK_LIST.action, message)//TODO
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
