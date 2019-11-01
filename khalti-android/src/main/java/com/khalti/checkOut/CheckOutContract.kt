package com.khalti.checkOut

import com.khalti.base.LifeCycle
import com.khalti.checkOut.helper.MerchantPreferencePojo
import com.khalti.checkOut.helper.PaymentPreference
import kotlinx.coroutines.flow.Flow

import rx.Observable

interface CheckOutContract {
    interface View {

        fun setupViewPager(types: List<PaymentPreference>)

        fun setUpTabLayout(types: List<PaymentPreference>)

        fun toggleTab(position: Int, selected: Boolean)

        fun setStatusBarColor()

        fun dismissAllDialogs()

        fun closeCheckOut()

        fun setPresenter(presenter: Presenter)
    }

    interface Presenter : LifeCycle{

        fun onTabSelected(position: Int, selected: Boolean)
    }
}
