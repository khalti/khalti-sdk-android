package com.khalti.checkOut.helper

import com.khalti.checkOut.api.Config

interface KhaltiCheckOutInterface {

    fun show()

    fun show(config: Config?)

    fun destroy()
}
