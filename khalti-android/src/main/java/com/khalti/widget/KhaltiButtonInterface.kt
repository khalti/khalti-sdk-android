package com.khalti.widget


import androidx.annotation.Keep
import android.view.View

import com.khalti.checkout.helper.Config

@Keep
interface KhaltiButtonInterface {
    fun setText(buttonText: String)

    fun setCheckOutConfig(config: Config)

    fun setCustomView(view: View)

    fun setButtonStyle(style: ButtonStyle)
}
