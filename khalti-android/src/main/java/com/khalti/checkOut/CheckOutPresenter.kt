package com.khalti.checkOut

import com.khalti.checkOut.helper.CheckoutEventListener
import com.khalti.checkOut.helper.PaymentPreference
import com.khalti.signal.CompositeSignal
import com.khalti.utils.EmptyUtil
import com.khalti.utils.GuavaUtil
import com.khalti.utils.LogUtil
import com.khalti.utils.Store

internal class CheckOutPresenter(view: CheckOutContract.View) : CheckOutContract.Presenter {
    private val view: CheckOutContract.View = GuavaUtil.checkNotNull<CheckOutContract.View>(view)
    private val compositeSignal = CompositeSignal()

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

        compositeSignal.add(view.setUpTabLayout(uniqueList)
                .connect {
                    view.toggleTab(it.getValue("position") as Int, it.getValue("selected") as Boolean, it.getValue("id") as String)
                })

        compositeSignal.add(view.setOffsetListener()
                .connect { view.toggleToolbarShadow(it < 0) })
    }

    override fun onDestroy() {
        view.dismissAllDialogs()
        compositeSignal.clear()
    }
}