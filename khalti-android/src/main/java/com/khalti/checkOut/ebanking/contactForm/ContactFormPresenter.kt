package com.khalti.checkOut.ebanking.contactForm

import com.google.gson.Gson

import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.util.HashMap

import com.khalti.BuildConfig
import com.khalti.checkOut.ebanking.helper.BankingData
import com.khalti.checkOut.helper.Config
import com.khalti.signal.CompositeSignal
import com.khalti.utils.ApiUtil
import com.khalti.utils.AppUtil
import com.khalti.utils.Constant
import com.khalti.utils.EmptyUtil
import com.khalti.utils.GuavaUtil
import com.khalti.utils.LogUtil
import com.khalti.utils.NumberUtil
import com.khalti.utils.StringUtil
import com.khalti.utils.ValidationUtil
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

                val checkOutLog = HashMap<String, String>()
                checkOutLog["checkout_version"] = BuildConfig.VERSION_NAME
                checkOutLog["checkout_android_version"] = AppUtil.getOsVersion()
                checkOutLog["checkout_android_api_level"] = AppUtil.getApiLevel()!!.toString() + ""

                try {
                    var data = "public_key=" + URLEncoder.encode(config.publicKey, "UTF-8") + "&" +
                            "product_identity=" + URLEncoder.encode(config.productId, "UTF-8") + "&" +
                            "product_name=" + URLEncoder.encode(config.productName, "UTF-8") + "&" +
                            "amount=" + URLEncoder.encode(config.amount.toString() + "", "UTF-8") + "&" +
                            "mobile=" + URLEncoder.encode(map["mobile"]!! + "", "UTF-8") + "&" +
                            "bank=" + URLEncoder.encode(map["bankId"]!! + "", "UTF-8") + "&" +
                            "source=android" + "&" +
                            "checkout_details=" + URLEncoder.encode(Gson().toJson(checkOutLog), "UTF-8") + "&" +
                            "return_url=" + URLEncoder.encode(view.packageName, "UTF-8")

                    if (EmptyUtil.isNotNull(config.productUrl) && EmptyUtil.isNotEmpty(config.productUrl)) {
                        data += "&" + "product_url=" + URLEncoder.encode(config.productUrl, "UTF-8")
                    }

                    data += ApiUtil.getPostData(config.additionalData)

                    LogUtil.log("data", data)

                    view.saveConfigInFile(config)
                    view.openEBanking(Constant.url + "ebanking/initiate/?" + data)
                } catch (e: UnsupportedEncodingException) {
                    e.printStackTrace()
                    view.showMessageDialog("Error", "Something went wrong")
                }

            } else {
                view.showNetworkError()
            }
        } else {
            if (EmptyUtil.isEmpty(mobile)) {
                view.setEditTextError("This field is required")
            } else {
                view.setEditTextError("Enter a valid mobile number")
            }
        }
    }
}
