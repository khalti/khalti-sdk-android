package com.khalti.checkout.helper

import android.content.Context
import androidx.annotation.Keep
import com.khalti.checkout.CheckOutActivity
import com.khalti.utils.*

@Keep
class KhaltiCheckOut : KhaltiCheckOutInterface {

    private var context: Context
    private var config: Config? = null

    constructor(context: Context) {
        this.context = context
    }

    constructor(context: Context, config: Config?) {
        this.context = context
        this.config = config
    }

    override fun show() {
        if (!CheckOutActivity.isActive) {
            require(EmptyUtil.isNotNull(config)) { "Config not set" }

            if (EmptyUtil.isNotNull(config)) {
                val message = ConfigUtil.validateConfig(config!!)

                require(EmptyUtil.isEmpty(message)) { message }

                Store.setConfig(config)
                ActivityUtil.openActivity(CheckOutActivity::class.java, context, null, true)
            }
        }
    }

    override fun show(config: Config?) {
        if (!CheckOutActivity.isActive) {
            this.config = config
            require(EmptyUtil.isNotNull(config)) { "Config not set" }

            if (EmptyUtil.isNotNull(config)) {
                val message = ConfigUtil.validateConfig(config!!)
                require(EmptyUtil.isEmpty(message)) { message }

                Store.setConfig(config)
                ActivityUtil.openActivity(CheckOutActivity::class.java, context, null, true)
            }
        }
    }

    override fun destroy() {
        Store.getCheckoutEventListener().closeCheckout()
    }
}