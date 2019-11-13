package com.khalti.checkOut

import com.khalti.base.LifeCycle
import com.khalti.checkOut.helper.MerchantPreferencePojo
import com.khalti.checkOut.helper.PaymentPreference
import com.khalti.signal.Signal
import kotlinx.coroutines.flow.Flow

import rx.Observable

interface CheckOutContract {
    interface View {

        fun toggleTab(position: Int, selected: Boolean, id: String)

        fun toggleToolbarShadow(show: Boolean)

        fun toggleSearch(show: Boolean)

        fun setupViewPager(types: List<PaymentPreference>)

        fun setUpTabLayout(types: List<PaymentPreference>): Signal<Map<String, Any>>

        fun setOffsetListener(): Signal<Int>

        fun setStatusBarColor()

        fun dismissAllDialogs()

        fun closeCheckOut()

        fun setPresenter(presenter: Presenter)
    }

    interface Presenter : LifeCycle
}
