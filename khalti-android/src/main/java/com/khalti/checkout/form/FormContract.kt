package com.khalti.checkout.form


import com.khalti.base.LifeCycle
import com.khalti.checkout.api.Result
import com.khalti.checkout.form.helper.WalletConfirmPojo
import com.khalti.checkout.form.helper.WalletInitPojo
import com.khalti.checkout.helper.Config
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

        fun toggleAttemptRemaining(show: Boolean)

        fun setPinMessage(message: String)

        fun setAttemptsRemaining(attempts: String)

        fun setMobile(mobile: String)

        fun setEditTextError(view: String, error: String?)

        fun setButtonText(text: String)

        fun setConfirmationLayoutHeight()

        fun setEditTextListener(): Map<String, Signal<CharSequence>>

        fun setClickListener(): Map<String, Signal<Any>>

        fun showNetworkError()

        fun showMessageDialog(title: String, message: String, actionListener: Boolean = false): Signal<Boolean>

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

        fun clearForm()

        fun getBackPressedSignal(): Signal<Any>

        fun setPresenter(presenter: Presenter)
    }

    interface Presenter : LifeCycle {

        fun isInitialFormValid(mobile: String, pin: String?): Boolean

        fun isFinalFormValid(confirmationCode: String): Boolean

        fun onInitiateWalletPayment(isNetwork: Boolean, mobile: String, pin: String)

        fun onConfirmWalletPayment(isNetwork: Boolean, confirmationCode: String, transactionPin: String)

        fun onBankingPayment(isNetwork: Boolean, mobile: String)

        fun onSetFormError(errorMap: Map<String, String>)
    }

    interface Model {

        suspend fun initiatePayment(mobile: String, pin: String, config: Config): Result<WalletInitPojo>

        suspend fun confirmPayment(confirmationCode: String, pin: String, token: String): Result<WalletConfirmPojo>
    }
}
