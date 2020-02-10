package com.khalti.checkout

import com.khalti.checkout.helper.CheckoutEventListener
import com.khalti.checkout.helper.Config
import com.khalti.checkout.helper.PaymentPreference
import com.khalti.signal.CompositeSignal
import com.khalti.utils.*

class CheckOutPresenter(view: CheckOutContract.View) : CheckOutContract.Presenter {
    private val view: CheckOutContract.View = GuavaUtil.checkNotNull<CheckOutContract.View>(view)
    private val compositeSignal = CompositeSignal()
    private var currentPage = 0

    private val searchList = listOf(PaymentPreference.EBANKING.value, PaymentPreference.MOBILE_BANKING.value)

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

        val uniqueList = onGetPreferenceList(Store.getConfig())
        view.setupViewPager(uniqueList)
        view.toggleTitle(uniqueList.size > 1)

        val barWidth = view.convertDpToPx(250) / uniqueList.size
        compositeSignal.add(view.setUpTabLayout(uniqueList)
                .connect {
                    onTabSelected(uniqueList, it, barWidth)
                })
        view.toggleIndicator(uniqueList.size > 1)
        view.setIndicatorBarWidth(barWidth)

        HandlerUtil.delayedTask(1000) {
            compositeSignal.add(view.setPageScrollListener(currentPage)
                    .connect {
                        view.toggleToolbarShadow(it > 0)
                    })
        }

        compositeSignal.add(view.getSearchViewMapInitSignal()
                .connect {
                    view.toggleSearch(uniqueList[currentPage].value, searchList.contains(uniqueList[currentPage].value))
                })
    }

    override fun onDestroy() {
        view.dismissAllDialogs()
        compositeSignal.clear()
    }

    override fun onGetPreferenceList(config: Config): List<PaymentPreference> {
        val types = config.paymentPreferences

        val uniqueList = ArrayList<PaymentPreference>()
        if (EmptyUtil.isNull(types) || EmptyUtil.isEmpty(types)) {
            uniqueList.add(PaymentPreference.KHALTI)
            uniqueList.add(PaymentPreference.EBANKING)
            uniqueList.add(PaymentPreference.MOBILE_BANKING)
            uniqueList.add(PaymentPreference.CONNECT_IPS)
            uniqueList.add(PaymentPreference.SCT)
        } else {
            uniqueList.addAll(LinkedHashSet<PaymentPreference>(types))
        }

        return uniqueList
    }

    override fun onTabSelected(preferences: List<PaymentPreference>, it: Map<String, Any>, barWidth: Int) {
        currentPage = it.getValue("position") as Int
        view.toggleTab(currentPage, it.getValue("selected") as Boolean, it.getValue("id") as String)
        view.setIndicatorBarPosition(currentPage * barWidth)
        view.toggleSearch(preferences[currentPage].value, searchList.contains(preferences[currentPage].value))
    }
}