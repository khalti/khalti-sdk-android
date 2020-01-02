package com.khalti.checkOut.form


import com.khalti.base.LifeCycle

import com.khalti.checkOut.form.helper.WalletConfirmPojo
import com.khalti.checkOut.form.helper.WalletInitPojo
import com.khalti.checkOut.api.Result
import com.khalti.checkOut.helper.Config
import com.khalti.signal.Signal

interface FormContract {
    interface View {

        val packageName: String

        fun receiveData(): Map<String, Any>?

        val payButtonText: String

        val formData: Map<String, String>

        fun toggleProgressDialog(action: String, show: Boolean): Signal<Boolean>?

        fun toggleConfirmationLayout(show: Boolean)

        fun togglePinLayout(show: Boolean)

        fun togglePinMessage(show: Boolean)

        fun toggleMobileLabel(paymentType: String)

        fun setPinMessage(message: String)

        fun setMobile(mobile: String)

        fun setEditTextError(view: String, error: String?)

        fun setButtonText(text: String)

        fun setConfirmationLayoutHeight()

        fun setEditTextListener(): Map<String, Signal<CharSequence>>

        fun setClickListener(): Map<String, Signal<Any>>

        fun showNetworkError()

        fun showMessageDialog(title: String, message: String)

        fun showPINDialog(title: String, message: String): Signal<Boolean>

        fun showSlogan()

        fun showBranding(paymentType: String)

        fun openKhaltiSettings()

        fun openLinkInBrowser(link: String)

        fun closeWidget()

        fun updateConfirmationHeight()

        fun openBanking(url: String)

        fun getMessage(action: String): String

        fun hasNetwork(): Boolean

        fun doesPackageExist(): Boolean

        fun setPresenter(presenter: Presenter)
    }

    interface Presenter : LifeCycle {

        fun isInitialFormValid(mobile: String, pin: String?): Boolean

        fun isFinalFormValid(confirmationCode: String): Boolean

        fun onInitiateWalletPayment(isNetwork: Boolean, mobile: String, pin: String)

        fun onConfirmWalletPayment(isNetwork: Boolean, confirmationCode: String, transactionPin: String)

        fun onConnectIpsPayment(isNetwork: Boolean, mobile:String)
    }

    interface Model {

        suspend fun initiatePayment(mobile: String, pin: String, config: Config): Result<WalletInitPojo>

        suspend fun confirmPayment(confirmationCode: String, pin: String, token: String): Result<WalletConfirmPojo>
    }
}
