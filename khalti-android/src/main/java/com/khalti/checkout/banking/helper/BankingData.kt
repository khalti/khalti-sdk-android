package com.khalti.checkout.banking.helper

import androidx.annotation.Keep

import java.io.Serializable

import com.khalti.checkout.helper.Config

@Keep
class BankingData(val bankIdx: String, val bankName: String, val bankLogo: String, val bankIcon: String, val paymentType: String, val config: Config) : Serializable