package com.khalti.checkOut.ebanking.helper

import androidx.annotation.Keep

import java.io.Serializable

import com.khalti.checkOut.helper.Config

@Keep
class BankingData(val bankIdx: String, val bankName: String, val bankLogo: String, val bankIcon: String, val config: Config) : Serializable