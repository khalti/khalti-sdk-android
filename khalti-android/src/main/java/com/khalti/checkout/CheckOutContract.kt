package com.khalti.checkout

import com.khalti.base.LifeCycle
import com.khalti.checkout.helper.Config
import com.khalti.checkout.helper.PaymentPreference
import com.khalti.signal.Signal

interface CheckOutContract {
    interface View {

        fun toggleToolbarShadow(show: Boolean)

        fun toggleTitle(show: Boolean)

        fun toggleSearch(paymentType: String, show: Boolean)

        fun toggleLoading(show: Boolean)

        fun toggleTestBanner(show: Boolean)

        fun setupViewPager(types: List<PaymentPreference>)

        fun setUpTabLayout(types: List<PaymentPreference>): Signal<Map<String, Any>>

        fun setPageScrollListener(currentPage: Int): Signal<Int>

        fun setStatusBarColor()

        fun dismissAllDialogs()

        fun closeCheckOut()

        fun convertDpToPx(dp: Int): Int

        fun getSearchViewMapInitSignal(): Signal<Any>

        fun getAppPackageName() : String

        fun setPresenter(presenter: Presenter)
    }

    interface Presenter : LifeCycle {

        fun onGetPreferenceList(config: Config): List<PaymentPreference>

        fun onTabSelected(preferences: List<PaymentPreference>, it: Map<String, Any>)

        fun onBackPressed()
    }
}
