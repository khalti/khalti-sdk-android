package com.khalti.widget


import androidx.annotation.Keep
import android.view.View

import com.khalti.checkOut.helper.Config

@Keep
interface KhaltiButtonInterface {
    fun setText(buttonText: String)

    fun setCheckOutConfig(config: Config)

    fun setCustomView(view: View)

    fun setButtonStyle(style: ButtonStyle)

    fun showCheckOut()

    fun showCheckOut(config: Config)

    fun destroyCheckOut()
}
