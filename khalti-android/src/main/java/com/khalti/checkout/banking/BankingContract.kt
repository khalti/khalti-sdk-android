package com.khalti.checkout.banking

import com.khalti.base.LifeCycle
import com.khalti.checkout.api.Result
import com.khalti.checkout.banking.helper.BankPojo
import com.khalti.checkout.banking.helper.BankingData
import com.khalti.checkout.banking.helper.BaseListPojo
import com.khalti.signal.Signal

interface BankingContract {
    interface View {

        fun receiveData(): Map<String, Any>?

        fun toggleIndented(show: Boolean)

        fun toggleSearchError(show: Boolean)

        fun setupList(bankList: MutableList<BankPojo>): Signal<Map<String, String>>

        fun setupSearch(paymentType: String): Signal<String>

        fun showIndentedNetworkError()

        fun showIndentedError(error: String)

        fun openMobileForm(bankingData: BankingData)

        fun setOnClickListener(): Map<String, Signal<Any>>

        fun filterList(text: String): Int?

        fun hasNetwork(): Boolean

        fun setPresenter(presenter: Presenter)
    }

    interface Presenter : LifeCycle {

        fun onFetch()
    }

    interface Model {

        suspend fun fetchBankList(paymentType: String): Result<BaseListPojo>
    }
}
