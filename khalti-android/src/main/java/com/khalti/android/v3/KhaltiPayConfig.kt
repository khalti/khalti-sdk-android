/*
 * Copyright (c) 2024. The Khalti Authors. All rights reserved.
 */

package com.khalti.android.v3

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class KhaltiPayConfig(
    val publicKey: String,
    val pidx: String,
    val returnUrl: Uri,
    val openInKhalti: Boolean = true,
    val environment: Environment = Environment.PROD,
) : Parcelable {

    fun isProd(): Boolean = environment == Environment.PROD
}
