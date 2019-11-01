package com.khalti.checkOut.Wallet


import com.khalti.base.BaseModel
import java.util.HashMap

import com.khalti.checkOut.Wallet.helper.WalletConfirmPojo
import com.khalti.checkOut.Wallet.helper.WalletInitPojo
import com.khalti.checkOut.api.Result
import com.khalti.checkOut.helper.Config
import rx.Observable

interface WalletContract {
    interface View {

        val payButtonText: String

        val formData: HashMap<String, String>

        fun toggleProgressDialog(action: String, show: Boolean)

        fun toggleConfirmationLayout(show: Boolean)

        fun togglePinMessage(show: Boolean)

        fun setPinMessage(message: String)

        fun setMobile(mobile: String)

        fun setEditTextListener(): HashMap<String, Observable<CharSequence>>

        fun setEditTextError(view: String, error: String)

        fun setButtonText(text: String)

        fun setButtonClickListener(): Observable<Void>

        fun setImageClickListener(): Observable<Void>

        fun setConfirmationLayoutHeight(view: String)

        fun showNetworkError()

        fun showMessageDialog(title: String, message: String)

        fun showPINDialog(title: String, message: String)

        fun showPINInBrowserDialog(title: String, message: String)

        fun openKhaltiSettings()

        fun openLinkInBrowser(link: String)

        fun closeWidget()

        fun updateConfirmationHeight()

        fun getMessage(action: String): String

        fun hasNetwork(): Boolean

        fun showSlogan()

        fun showBranding()

        fun setPresenter(presenter: Presenter)
    }

    interface Presenter {

        fun onCreate()

        fun onDestroy()

        fun openKhaltiSettings()

        fun openLinkInBrowser()

        fun showPINInBrowserDialog(title: String, message: String)

        fun isMobileValid(mobile: String): Boolean

        fun isFinalFormValid(pin: String, confirmationCode: String): Boolean

        fun initiatePayment(isNetwork: Boolean, mobile: String)

        fun confirmPayment(isNetwork: Boolean, confirmationCode: String, transactionPin: String)

        fun updateConfirmationHeight()

        fun unSubscribe()
    }

    interface Model {

        suspend fun initiatePayment(mobile: String, config: Config): Result<WalletInitPojo>

        suspend fun confirmPayment(confirmationCode: String, transactionPIN: String, token: String): Result<WalletConfirmPojo>
    }
}
