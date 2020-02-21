package com.khalti.checkout.helper

import androidx.annotation.Keep

@Keep
interface KhaltiCheckOutInterface {

    fun show()

    fun show(config: Config?)

    fun destroy()
}
