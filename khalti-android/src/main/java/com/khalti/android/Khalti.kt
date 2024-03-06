/*
 * Copyright (c) 2024. The Khalti Authors. All rights reserved.
 */

package com.khalti.android

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager.NameNotFoundException
import com.khalti.android.service.VerificationRepository
import com.khalti.android.data.KhaltiPayConfig
import com.khalti.android.callbacks.OnMessage
import com.khalti.android.callbacks.OnPaymentResult
import com.khalti.android.callbacks.OnReturn
import com.khalti.android.cache.Store
import com.khalti.android.utils.PackageUtil

// Though kotlin provides named and optional parameters
// method overloading was required for Java developers
class Khalti private constructor(
    private val context: Context,
    val config: KhaltiPayConfig,
    val onPaymentResult: OnPaymentResult,
    val onMessage: OnMessage,
    val onReturn: OnReturn?,
) {
    companion object {
        fun init(
            context: Context,
            config: KhaltiPayConfig,
            onPaymentResult: OnPaymentResult,
            onMessage: OnMessage,
            onReturn: OnReturn,
        ): Khalti {
            val khalti = Khalti(
                context,
                config,
                onPaymentResult,
                onMessage,
                onReturn,
            )

            Store.instance().put("khalti", khalti)
            return khalti
        }

        fun init(
            context: Context,
            config: KhaltiPayConfig,
            onPaymentResult: OnPaymentResult,
            onMessage: OnMessage,
        ): Khalti {
            val khalti = Khalti(
                context,
                config,
                onPaymentResult,
                onMessage,
                null,
            )

            Store.instance().put("khalti", khalti)

            return khalti
        }
    }

    fun open() {
        val packageName = context.packageName
        val store = Store.instance()
        val packageInfo = PackageUtil.getPackageInfo(context, packageName)

        store.put("merchant_package_name", packageName)
        store.put("merchant_package_version", packageInfo?.versionName ?: "")

        val intent = Intent(context, PaymentV3Activity::class.java)
        context.startActivity(intent)
    }

    fun verify() {
        val verificationRepo = VerificationRepository()
        verificationRepo.verify(config.pidx, this)
    }

    fun close() {
        val intent = Intent("close_khalti_payment_portal")
        context.sendBroadcast(intent)
    }
}