package com.khalti.checkOut.banking.deepLinkReceiver

import com.khalti.base.LifeCycle
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
