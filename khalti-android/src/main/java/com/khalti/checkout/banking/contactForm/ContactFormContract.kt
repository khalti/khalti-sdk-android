package com.khalti.checkout.banking.contactForm

import com.khalti.base.LifeCycle
import com.khalti.checkout.banking.helper.BankingData
import com.khalti.checkout.helper.Config
import com.khalti.signal.Signal

interface ContactFormContract {

    interface View {

        val packageName: String

        val contactNumber: String

        val isNetworkAvailable: Boolean

        fun receiveData(): BankingData?

        fun setBankData(logo: String, name: String, icon: String)

        fun setButtonText(text: String)

        fun setEditTextError(error: String?)

        fun setMobile(mobile: String)

        fun showMessageDialog(title: String, message: String)

        fun showError(message: String)

        fun showNetworkError()

        fun openBanking(url: String)

        fun saveConfigInFile(config: Config)

        fun setOnClickListener(): Map<String, Signal<Any>>

        fun setEditTextListener(): Signal<CharSequence>

        fun setPresenter(presenter: Presenter)
    }

    interface Presenter : LifeCycle {

        fun onFormSubmitted(mobile: String, bankId: String, bankName: String, paymentType: String, config: Config)
    }
}
