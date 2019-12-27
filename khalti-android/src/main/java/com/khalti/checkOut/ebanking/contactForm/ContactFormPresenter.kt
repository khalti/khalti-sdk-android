package com.khalti.checkOut.ebanking.contactForm

import com.google.gson.Gson

import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.util.HashMap

import com.khalti.BuildConfig
import com.khalti.checkOut.ebanking.helper.BankingData
import com.khalti.checkOut.helper.Config
import com.khalti.signal.CompositeSignal
import com.khalti.utils.*
import com.khalti.utils.ErrorUtil.EMPTY_ERROR
import com.khalti.utils.ErrorUtil.MOBILE_ERROR
import rx.subscriptions.CompositeSubscription

internal class ContactFormPresenter(view: ContactFormContract.View) : ContactFormContract.Presenter {
    private val view: ContactFormContract.View = GuavaUtil.checkNotNull<ContactFormContract.View>(view)
    private val compositeSignal = CompositeSignal()

    init {
        view.setPresenter(this)
    }

    override fun onCreate() {
        val bankingData = view.receiveData()
        if (EmptyUtil.isNotNull(bankingData)) {
            val mobile = bankingData!!.config.mobile
            if (EmptyUtil.isNotNull(mobile) && EmptyUtil.isNotEmpty(mobile) && ValidationUtil.isMobileNumberValid(mobile)) {
                view.setMobile(bankingData.config.mobile!!)
            }
            view.setBankData(bankingData.bankLogo, bankingData.bankName, bankingData.bankIcon)
            view.setButtonText("Pay Rs " + StringUtil.formatNumber(NumberUtil.convertToRupees(bankingData.config.amount)))

            val clickMap = view.setOnClickListener()
            if (EmptyUtil.isNotNull(clickMap["pay"])) {
                compositeSignal.add(clickMap.getValue("pay")
                        .connect {
                            onFormSubmitted(view.contactNumber, bankingData.bankIdx,
                                    bankingData.bankName, bankingData.config)
                        })
            }
            compositeSignal.add(view.setEditTextListener()
                    .connect {
                        view.setEditTextError(null)
                    })
        }
    }

    override fun onDestroy() {
        compositeSignal.clear()
    }

    override fun onFormSubmitted(mobile: String, bankId: String, bankName: String, config: Config) {
        if (EmptyUtil.isNotEmpty(mobile) && ValidationUtil.isMobileNumberValid(mobile)) {
            if (view.isNetworkAvailable) {
                val map = HashMap<String, String>()
                map["mobile"] = mobile
                map["bankId"] = bankId
                map["bankName"] = bankName
                map["checkout_version"] = BuildConfig.VERSION_NAME
                map["checkout_android_version"] = AppUtil.getOsVersion()
                map["checkout_android_api_level"] = AppUtil.getApiLevel().toString()
                map["public_key"] = config.publicKey
                map["product_identity"] = config.productId
                map["product_name"] = config.productName
                map["amount"] = config.amount.toString()
                map["bank"] = map["bankId"].orEmpty()
                map["source"] = "android"
                map["return_url"] = view.packageName

                if (EmptyUtil.isNotNull(config.productUrl) && EmptyUtil.isNotEmpty(config.productUrl)) {
                    map["product_url"] = config.productUrl!!
                }

                val data = EncodeUtil.urlEncode(map)

//                view.saveConfigInFile(config)
                view.openConfigService()
                view.openEBanking(Constant.url + "ebanking/initiate/?" + data)
                LogUtil.log("data", data)

            } else {
                view.showNetworkError()
            }
        } else {
            if (EmptyUtil.isEmpty(mobile)) {
                view.setEditTextError(EMPTY_ERROR)
            } else {
                view.setEditTextError(MOBILE_ERROR)
            }
        }
    }
}