package com.khalti.checkOut

import com.khalti.base.LifeCycle
import com.khalti.checkOut.helper.PaymentPreference
import com.khalti.signal.Signal

interface CheckOutContract {
    interface View {

        fun toggleTab(position: Int, selected: Boolean, id: String)

        fun toggleToolbarShadow(show: Boolean)

        fun toggleIndicator(show: Boolean)

        fun setIndicatorBarWidth(width: Int)

        fun setIndicatorBarPosition(position: Int)

        fun toggleSearch(show: Boolean)

        fun toggleTitle(show: Boolean)

        fun setupViewPager(types: List<PaymentPreference>)

        fun setUpTabLayout(types: List<PaymentPreference>): Signal<Map<String, Any>>

        fun setPageScrollListener(currentPage: Int): Signal<Int>

        fun setStatusBarColor()

        fun dismissAllDialogs()

        fun closeCheckOut()

        fun convertDpToPx(dp: Int): Int

        fun setPresenter(presenter: Presenter)
    }

    interface Presenter : LifeCycle
}
