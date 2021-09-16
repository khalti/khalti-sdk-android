package com.khalti.checkout.banking.contactForm

import com.khalti.checkout.helper.Config
import com.khalti.signal.CompositeSignal
import com.khalti.utils.*
import com.khalti.utils.ErrorUtil.EMPTY_ERROR
import com.khalti.utils.ErrorUtil.MOBILE_ERROR

internal class ContactFormPresenter(view: ContactFormContract.View) : ContactFormContract.Presenter {
    private val view: ContactFormContract.View = GuavaUtil.checkNotNull(view)
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
                                    bankingData.bankName, bankingData.paymentType, bankingData.config)
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

    override fun onFormSubmitted(mobile: String, bankId: String, bankName: String, paymentType: String, config: Config) {
        if (EmptyUtil.isNotEmpty(mobile) && ValidationUtil.isMobileNumberValid(mobile)) {
            if (view.isNetworkAvailable) {
                view.openBanking(PayloadUtil.buildPayload(mobile, bankId, bankName, paymentType, view.packageName, config))
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