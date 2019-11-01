package com.khalti.checkOut

import com.khalti.checkOut.helper.CheckoutEventListener
import com.khalti.checkOut.helper.PaymentPreference
import com.khalti.utils.EmptyUtil
import com.khalti.utils.GuavaUtil
import com.khalti.utils.Store

internal class CheckOutPresenter(view: CheckOutContract.View) : CheckOutContract.Presenter {
    private val view: CheckOutContract.View = GuavaUtil.checkNotNull<CheckOutContract.View>(view)

    init {
        view.setPresenter(this)
    }

    override fun onCreate() {
        view.setStatusBarColor()
        Store.setCheckoutEventListener(object : CheckoutEventListener {
            override fun closeCheckout() {
                view.closeCheckOut()
            }
        })

        val config = Store.getConfig()
        val types = config.paymentPreferences

        val uniqueList = ArrayList<PaymentPreference>()
        if (EmptyUtil.isNull(types) || EmptyUtil.isEmpty(types)) {
            uniqueList.add(PaymentPreference.EBANKING)
            uniqueList.add(PaymentPreference.WALLET)
        } else {
            uniqueList.addAll(LinkedHashSet<PaymentPreference>(types))
        }

        view.setupViewPager(uniqueList)
        view.setUpTabLayout(uniqueList)
    }

    override fun onDestroy() {
        view.dismissAllDialogs()
    }

    override fun onTabSelected(position: Int, selected: Boolean) {
        view.toggleTab(position, selected)
    }
}