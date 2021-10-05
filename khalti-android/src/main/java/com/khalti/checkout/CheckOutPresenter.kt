package com.khalti.checkout

import com.khalti.checkout.helper.CheckoutEventListener
import com.khalti.checkout.helper.Config
import com.khalti.checkout.helper.PaymentPreference
import com.khalti.signal.CompositeSignal
import com.khalti.signal.Signal
import com.khalti.utils.EmptyUtil
import com.khalti.utils.GuavaUtil
import com.khalti.utils.HandlerUtil
import com.khalti.utils.Store

class CheckOutPresenter(view: CheckOutContract.View) : CheckOutContract.Presenter {
    private val view: CheckOutContract.View = GuavaUtil.checkNotNull(view)
    private val compositeSignal = CompositeSignal()
    private var currentPage = 0

    private val searchList = listOf(PaymentPreference.EBANKING.value, PaymentPreference.MOBILE_BANKING.value)

    init {
        view.setPresenter(this)
    }

    override fun onCreate() {
        view.setStatusBarColor()
        Store.setAppPackageName(view.getAppPackageName())
        Store.setCheckoutEventListener(object : CheckoutEventListener {
            override fun closeCheckout() {
                view.closeCheckOut()
            }
        })

        view.toggleLoading(true)

        val uniqueList = onGetPreferenceList(Store.getConfig())
        HandlerUtil.delayedTask(500) {
            view.toggleLoading(false)
            view.setupViewPager(uniqueList)
            view.toggleTitle(uniqueList.size > 1)

            compositeSignal.add(view.setUpTabLayout(uniqueList)
                .connect {
                    onTabSelected(uniqueList, it)
                })

            HandlerUtil.delayedTask(1000) {
                for (i in 0 until uniqueList.size - 1) {
                    compositeSignal.add(view.setPageScrollListener(i)
                        .connect {
                            view.toggleToolbarShadow(it > 0)
                        })
                }
            }

            compositeSignal.add(view.getSearchViewMapInitSignal()
                .connect {
                    view.toggleSearch(uniqueList[currentPage].value, searchList.contains(uniqueList[currentPage].value))
                })

            view.toggleTestBanner(Store.getConfig().publicKey.contains("test_"))
        }
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

    override fun onTabSelected(preferences: List<PaymentPreference>, it: Map<String, Any>) {
        currentPage = it.getValue("position") as Int
        view.toggleSearch(preferences[currentPage].value, searchList.contains(preferences[currentPage].value))
    }

    override fun onBackPressed() {
        Store.getConfig().onCancelListener?.onCancel()
    }
}