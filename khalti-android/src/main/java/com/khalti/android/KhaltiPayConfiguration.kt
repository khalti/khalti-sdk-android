package com.khalti.android

import android.os.Parcel
import android.os.Parcelable

class KhaltiPayConfiguration(val paymentUrl: String, val returnUrl: String) : Parcelable {
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
