package com.khalti.checkout.banking.deepLinkReceiver

import com.khalti.checkout.helper.Config
import com.khalti.utils.EmptyUtil
import com.khalti.utils.GuavaUtil
import com.khalti.utils.Store

internal class DeepLinkPresenter(view: DeepLinkContract.View) : DeepLinkContract.Presenter {

    private val view: DeepLinkContract.View = GuavaUtil.checkNotNull(view)

    init {
        view.setPresenter(this)
    }

    override fun onCreate() {
        val map = view.receiveEBankingData()
        val config: Config? = Store.getConfig()

        if (EmptyUtil.isNotNull(map) && EmptyUtil.isNotNull(config)) {
            val onCheckOutListener = config!!.onCheckOutListener
            if (EmptyUtil.isNotNull(onCheckOutListener)) {
                onCheckOutListener.onSuccess(map!!)
            }
        }
        view.closeDeepLink()
        Store.getCheckoutEventListener().closeCheckout()
    }

    override fun onDestroy() {
    }
}