// Copyright (c) 2022. The Khalti Authors. All rights reserved.

package com.khalti.android

import android.os.Parcel
import android.os.Parcelable

class KhaltiPayConfiguration(val paymentUrl: String, val returnUrl: String) : Parcelable {
    init {
        require(paymentUrl.isNotBlank()) { "Payment URL cannot be blank" }
        require(returnUrl.isNotBlank()) { "Return URL cannot be blank" }
        check(paymentUrl != returnUrl) { "Payment URL and Return URL cannot be same" }

        val validPaymentUrlRegex = Regex("^https://.*pay.khalti.com/?\\?pidx=.+")
        require(validPaymentUrlRegex.matches(paymentUrl)) { "Invalid Payment URL" }
    }

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(paymentUrl)
        parcel.writeString(returnUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<KhaltiPayConfiguration> {
        override fun createFromParcel(parcel: Parcel): KhaltiPayConfiguration {
            return KhaltiPayConfiguration(parcel)
        }

        override fun newArray(size: Int): Array<KhaltiPayConfiguration?> {
            return arrayOfNulls(size)
        }
    }
}
