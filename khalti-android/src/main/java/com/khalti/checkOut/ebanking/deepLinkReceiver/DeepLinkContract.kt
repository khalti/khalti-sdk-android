package com.khalti.checkOut.ebanking.deepLinkReceiver

import com.khalti.base.LifeCycle
import java.util.HashMap

import com.khalti.checkOut.helper.Config

interface DeepLinkContract {
    interface View {

        val configFromFile: Config

        fun receiveEBankingData(): Map<String, Any>?

        fun closeDeepLink()

        fun setPresenter(presenter: Presenter)
    }

    interface Presenter : LifeCycle
}
