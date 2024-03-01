/*
 * Copyright (c) 2024. The Khalti Authors. All rights reserved.
 */

package com.khalti.android.v3

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager.NameNotFoundException
import android.util.Log
import com.khalti.android.PaymentActivity
import com.khalti.android.service.VerificationRepository

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
        store.put("merchant_package_name", packageName)

        try {
            val packageInfo = context.packageManager.getPackageInfo(packageName, 0)
            store.put("merchant_package_version", packageInfo.versionName)
        } catch (e: NameNotFoundException) {
            //no-op
        }

        val intent = Intent(context, PaymentActivity::class.java)
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