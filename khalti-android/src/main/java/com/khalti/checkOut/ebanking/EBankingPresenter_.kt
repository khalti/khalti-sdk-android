package com.khalti.checkOut.ebanking

import com.khalti.checkOut.api.ErrorAction
import com.khalti.checkOut.ebanking.helper.BankPojo
import com.khalti.checkOut.ebanking.helper.BankingData
import com.khalti.checkOut.helper.Config
import com.khalti.utils.EmptyUtil
import com.khalti.utils.GuavaUtil
import com.khalti.utils.Store

import java.util.HashMap
import java.util.concurrent.TimeUnit

import rx.Observable
import rx.Subscriber
import rx.subscriptions.CompositeSubscription

class EBankingPresenter_(view: EBankingContract.View) : EBankingContract.Presenter {
    private val view: EBankingContract.View = GuavaUtil.checkNotNull(view)
    private var eBankingModel: EBankingModel = EBankingModel()
    private var config: Config? = null

    init {
        view.setPresenter(this)
    }

    override fun onCreate() {

    }

    override fun onDestroy() {

    }
}
