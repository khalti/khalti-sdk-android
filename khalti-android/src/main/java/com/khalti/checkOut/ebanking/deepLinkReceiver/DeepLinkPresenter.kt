package com.khalti.checkOut.ebanking.deepLinkReceiver

import com.khalti.utils.EmptyUtil
import com.khalti.utils.GuavaUtil
import com.khalti.utils.Store
import java.util.*

internal class DeepLinkPresenter(view: DeepLinkContract.View) : DeepLinkContract.Presenter {

    private val view: DeepLinkContract.View = GuavaUtil.checkNotNull(view)

    init {
        view.setPresenter(this)
    }

    override fun onCreate() {
//        val map = view.receiveEBankingData()
        val map = object : HashMap<String, Any>() {
        }
        val config = if (EmptyUtil.isNotNull(Store.getConfig())) Store.getConfig() else view.configFromFile

        if (/*EmptyUtil.isNotNull(map) &&*/ EmptyUtil.isNotNull(config)) {
            val onSuccessListener = config!!.onSuccessListener
            if (EmptyUtil.isNotNull(onSuccessListener)) {
                onSuccessListener!!.onSuccess(map)
            }
        }
        view.closeDeepLink()
        Store.getCheckoutEventListener().closeCheckout()
    }

    override fun onDestroy() {
    }
}