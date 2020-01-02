package com.khalti.checkOut.helper

import android.content.Context
import com.khalti.checkOut.CheckOutActivity
import com.khalti.utils.ActivityUtil
import com.khalti.utils.ConfigUtil
import com.khalti.utils.EmptyUtil
import com.khalti.utils.Store

class KhaltiCheckOut : KhaltiCheckOutInterface {

    private var context: Context

    constructor(context: Context) {
        this.context = context
    }

    constructor(context: Context, config: Config?) {
        this.context = context
        if (EmptyUtil.isNotNull(config)) {
            Store.setConfig(config)
        }
    }

    override fun show() {
        require(EmptyUtil.isNotNull(Store.getConfig())) { "Config not set" }
        val message = ConfigUtil.validateConfig(Store.getConfig())

        if (EmptyUtil.isEmpty(message)) {
            val message2 = ConfigUtil.validateIfConfigIsSerializable(context, Store.getConfig())

            require(EmptyUtil.isEmpty(message2)) { message2 }
        } else {
            throw IllegalArgumentException(message)
        }

        ActivityUtil.openActivity(CheckOutActivity::class.java, context, null, true)
    }

    override fun show(config: Config?) {
        if (EmptyUtil.isNotNull(config)) {
            Store.setConfig(config)
        }
        require(EmptyUtil.isNotNull(Store.getConfig())) { "Config not set" }
        val message = ConfigUtil.validateConfig(Store.getConfig())

        if (EmptyUtil.isEmpty(message)) {
            val message2 = ConfigUtil.validateIfConfigIsSerializable(context, Store.getConfig())

            require(EmptyUtil.isEmpty(message2)) { message2 }
        } else {
            throw IllegalArgumentException(message)
        }

        ActivityUtil.openActivity(CheckOutActivity::class.java, context, null, true)
    }

    override fun destroy() {
        Store.getCheckoutEventListener().closeCheckout()
    }
}