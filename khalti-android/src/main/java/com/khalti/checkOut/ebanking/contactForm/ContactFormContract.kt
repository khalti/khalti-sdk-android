package com.khalti.checkOut.ebanking.contactForm

import com.khalti.base.LifeCycle
import com.khalti.checkOut.ebanking.helper.BankingData
import com.khalti.checkOut.helper.Config
import com.khalti.signal.Signal
import rx.Observable

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

        fun openEBanking(url: String)

        fun saveConfigInFile(config: Config)

        fun setOnClickListener(): Map<String, Signal<Any>>

        fun setEditTextListener(): Signal<CharSequence>

        fun setPresenter(presenter: Presenter)
    }

    interface Presenter : LifeCycle {

        fun onFormSubmitted(mobile: String, bankId: String, bankName: String, config: Config)
    }
}
