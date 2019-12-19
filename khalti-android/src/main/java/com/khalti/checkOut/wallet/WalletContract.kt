package com.khalti.checkOut.wallet


import com.khalti.base.LifeCycle

import com.khalti.checkOut.wallet.helper.WalletConfirmPojo
import com.khalti.checkOut.wallet.helper.WalletInitPojo
import com.khalti.checkOut.api.Result
import com.khalti.checkOut.helper.Config
import com.khalti.signal.Signal

interface WalletContract {
    interface View {

        val payButtonText: String

        val formData: Map<String, String>

        fun toggleProgressDialog(action: String, show: Boolean): Signal<Boolean>?

        fun toggleConfirmationLayout(show: Boolean)

        fun togglePinMessage(show: Boolean)

        fun setPinMessage(message: String)

        fun setMobile(mobile: String)

        fun setEditTextError(view: String, error: String?)

        fun setButtonText(text: String)

        fun setConfirmationLayoutHeight(view: String)

        fun setEditTextListener(): Map<String, Signal<CharSequence>>

        fun setClickListener(): Map<String, Signal<Any>>

        fun showNetworkError()

        fun showMessageDialog(title: String, message: String)

        fun showPINDialog(title: String, message: String): Signal<Boolean>

        fun showSlogan()

        fun showBranding()

        fun openKhaltiSettings()

        fun openLinkInBrowser(link: String)

        fun closeWidget()

        fun updateConfirmationHeight()

        fun getMessage(action: String): String

        fun hasNetwork(): Boolean

        fun doesPackageExist(): Boolean

        fun setPresenter(presenter: Presenter)
    }

    interface Presenter : LifeCycle {

        fun isMobileValid(mobile: String): Boolean

        fun isInitialFormValid(mobile: String, pin: String): Boolean

        fun isFinalFormValid(pin: String, confirmationCode: String): Boolean

        fun onInitiatePayment(isNetwork: Boolean, mobile: String, pin: String)

        fun onConfirmPayment(isNetwork: Boolean, confirmationCode: String, transactionPin: String)
    }

    interface Model {

        suspend fun initiatePayment(mobile: String, pin: String, config: Config): Result<WalletInitPojo>

        suspend fun confirmPayment(confirmationCode: String, pin: String, token: String): Result<WalletConfirmPojo>
    }
}
