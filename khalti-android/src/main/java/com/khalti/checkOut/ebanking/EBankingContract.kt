package com.khalti.checkOut.ebanking

import com.khalti.base.LifeCycle
import com.khalti.checkOut.api.Result
import com.khalti.checkOut.ebanking.helper.BankPojo
import com.khalti.checkOut.ebanking.helper.BankingData
import com.khalti.signal.Signal

import java.util.HashMap

import rx.Observable

interface EBankingContract {
    interface View {

        fun toggleIndented(show: Boolean)

        fun toggleSearch(show: Boolean)

        fun toggleSearchError(show: Boolean)

        fun setupList(bankList: MutableList<BankPojo>): Signal<Map<String, String>>

        fun showIndentedNetworkError()

        fun showIndentedError(error: String)

        fun openMobileForm(bankingData: BankingData)

        fun setOnClickListener(): Map<String, Signal<Any>>

        fun setSearchListener(): Signal<String>

        fun filterList(text: String): Signal<Int>

        fun hasNetwork(): Boolean

        fun setPresenter(presenter: Presenter)
    }

    interface Presenter : LifeCycle

    interface Model {

        suspend fun fetchBankList(): Result<List<BankPojo>>
    }
}
