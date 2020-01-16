package com.khalti.checkout.banking.deepLinkReceiver

import com.khalti.base.LifeCycle
import com.khalti.checkout.helper.Config

interface DeepLinkContract {
    interface View {

        val configFromFile: Config

        fun receiveEBankingData(): Map<String, Any>?

        fun closeDeepLink()

        fun setPresenter(presenter: Presenter)
    }

    interface Presenter : LifeCycle
}
