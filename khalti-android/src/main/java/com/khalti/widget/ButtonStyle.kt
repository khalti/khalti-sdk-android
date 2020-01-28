package com.khalti.widget


import androidx.annotation.Keep
import com.khalti.R

@Keep
enum class ButtonStyle(val value: Int) {
    BASIC(-1),
    KHALTI(0),
    EBANKING(1),
    MOBILE_BANKING(2),
    SCT(3),
    CONNECT_IPS(4),
}