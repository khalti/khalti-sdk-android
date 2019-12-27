package com.khalti.checkOut.ebanking.deepLinkReceiver

import com.khalti.base.LifeCycle
import java.util.HashMap

import com.khalti.checkOut.helper.Config
import com.khalti.checkOut.service.ConfigServiceComm

interface DeepLinkContract {
    interface View {

        val configFromFile: Config

        val configFromService: Config?

        fun receiveEBankingData(): Map<String, Any>?

        fun closeDeepLink()

        fun setPresenter(presenter: Presenter)
    }

    interface Presenter : LifeCycle
}
