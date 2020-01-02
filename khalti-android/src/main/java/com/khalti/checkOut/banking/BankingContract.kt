package com.khalti.checkOut.banking

import com.khalti.base.LifeCycle
import com.khalti.checkOut.api.Result
import com.khalti.checkOut.banking.helper.BankPojo
import com.khalti.checkOut.banking.helper.BankingData
import com.khalti.checkOut.banking.helper.BaseListPojo
import com.khalti.signal.Signal

interface BankingContract {
    interface View {

        fun receiveData(): Map<String, Any>?

        fun toggleIndented(show: Boolean)

        fun toggleSearch(show: Boolean)

        fun toggleSearchError(show: Boolean)

        fun setupList(bankList: MutableList<BankPojo>): Signal<Map<String, String>>

        fun showIndentedNetworkError()

        fun showIndentedError(error: String)

        fun openMobileForm(bankingData: BankingData)

        fun setOnClickListener(): Map<String, Signal<Any>>

        fun setSearchListener(): Signal<String>

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
