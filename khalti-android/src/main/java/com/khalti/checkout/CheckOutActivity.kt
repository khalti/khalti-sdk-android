package com.khalti.checkout

import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.khalti.R
import com.khalti.checkout.helper.BaseComm
import com.khalti.checkout.helper.PaymentPreference
import com.khalti.signal.Signal
import com.khalti.utils.*
import kotlinx.android.synthetic.main.component_tab.view.*
import kotlinx.android.synthetic.main.payment_activity.*

class CheckOutActivity : AppCompatActivity(), CheckOutContract.View, BaseComm {

    var flSearch: FrameLayout? = null
    private lateinit var adapter: ViewPagerAdapter
    private lateinit var presenter: CheckOutContract.Presenter

    private val tabs: MutableList<TabLayout.Tab?> = ArrayList()

    private val searchViewMap = HashMap<String, SearchView>()
    private val searchViewMapInitSignal = Signal<Any>()

    companion object {
        var isActive: Boolean = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.payment_activity)

        this.flSearch = flSearchBankContainer

        Store.setBaseComm(this)

        isActive = true
        presenter = CheckOutPresenter(this)
        presenter.onCreate()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
        isActive = false
    }

    override fun toggleTab(position: Int, selected: Boolean, id: String) {
        /*val flTab = tabs[position]?.customView
        val mcTab = flTab?.mcContainer
        if (EmptyUtil.isNotNull(mcTab)) {
            val tabMap = MerchantUtil.getTab(id)
            if (EmptyUtil.isNotNull(tabMap)) {
                if (selected) {
                    mcTab!!.tvTitle?.setTextColor(ResourceUtil.getColor(this, R.color.black))
                } else {
                    mcTab!!.tvTitle?.setTextColor(ResourceUtil.getColor(this, R.color.black))
                }
            }
        }*/
    }

    override fun toggleToolbarShadow(show: Boolean) {
        ViewUtil.toggleView(vToolbarShadow, show)
    }

    override fun toggleTitle(show: Boolean) {
        ViewUtil.toggleView(clTitle, show)
    }

    override fun setupViewPager(types: List<PaymentPreference>) {
        adapter = ViewPagerAdapter(this)

        for (p in types) {
            val map = MerchantUtil.getTab(p.value)
            if (EmptyUtil.isNotNull(map)) {
                val fragment = map["fragment"] as Fragment
                val bundle = Bundle()
                val dataMap = HashMap<String, Any>()
                dataMap["payment_type"] = p.value
                bundle.putSerializable("map", dataMap)
                fragment.arguments = bundle
                adapter.addFrag(fragment)
            }
        }

        vpContent.adapter = adapter
        vpContent.offscreenPageLimit = adapter.itemCount

        TabLayoutMediator(tlTitle, vpContent) { tab, position ->
            tab.text = MerchantUtil.getTab(types[position].value)["title"].toString()
        }.attach()
        tlTitle.tabMode = TabLayout.MODE_SCROLLABLE
    }

    override fun setUpTabLayout(types: List<PaymentPreference>): Signal<Map<String, Any>> {
        val signal = Signal<Map<String, Any>>()
        var position = 0

        for (i in types.indices) {
            var color: Int
            var icon: Int
            val map = MerchantUtil.getTab(types[i].value)
            if (EmptyUtil.isNotNull(map)) {

                color = ResourceUtil.getColor(this, R.color.black)
                icon = map.getValue("icon") as Int

                val flTab = LayoutInflater.from(this).inflate(R.layout.component_tab, tlTitle, false)
                val mcTab = flTab.mcContainer
                mcTab.tvTitle.text = map["title"].toString()
                mcTab.tvTitle.setTextColor(color)
                mcTab.ivIcon.setImageResource(icon)
                val tab: TabLayout.Tab? = tlTitle.getTabAt(position)
                if (EmptyUtil.isNotNull(tab)) {
                    tab!!.customView = mcTab
                }
                mcTab.setOnClickListener {
                    vpContent.currentItem = i
                }
                tabs.add(tab)
                position++
            }
        }

        tlTitle.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                signal.emit(object : HashMap<String, Any>() {
                    init {
                        put("position", tab.position)
                        put("selected", true)
                        put("id", types[tab.position].value)
                    }
                })
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })

        return signal
    }

    override fun setPageScrollListener(currentPage: Int): Signal<Int> {
        val signal = Signal<Int>()

        if (EmptyUtil.isNotNull(adapter)) {
            val currentFragment = adapter.getItem(currentPage)
            if (EmptyUtil.isNotNull(currentFragment)) {
                val view = currentFragment.view
                if (EmptyUtil.isNotNull(view)) {
                    val nestedScrollView = view!!.findViewById<NestedScrollView>(R.id.nsvContainer)
                    nestedScrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                        signal.emit(scrollY)
                    })
                }
            }
        }
        return signal
    }

    override fun setStatusBarColor() {
        val window = window
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            var flags = window.decorView.systemUiVisibility
            flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.decorView.systemUiVisibility = flags
            window.statusBarColor = ResourceUtil.getColor(this, R.color.bg)
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && Build.VERSION.SDK_INT < 23) {
                window.statusBarColor = ResourceUtil.getColor(this, R.color.khaltiPrimaryDark)
            }
        }
    }

    override fun dismissAllDialogs() {
        UserInterfaceUtil.dismissAllDialogs()
    }

    override fun closeCheckOut() {
        finish()
    }

    override fun convertDpToPx(dp: Int): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), resources.displayMetrics).toInt()
    }

    override fun getSearchViewMapInitSignal(): Signal<Any> {
        return searchViewMapInitSignal
    }

    override fun toggleSearch(paymentType: String, show: Boolean) {
        toggleSearchView(paymentType, show)
    }

    override fun toggleLoading(show: Boolean) {
        ViewUtil.toggleView(flMainLoad, show)
    }

    override fun toggleTestBanner(show: Boolean) {
        ViewUtil.toggleView(flTestBanner, show)
    }

    override fun setPresenter(presenter: CheckOutContract.Presenter) {
        this.presenter = presenter
    }

    override fun getCoordinator(): CoordinatorLayout? {
        return clMain
    }

    override fun addSearchView(paymentType: String, searchView: SearchView) {
        searchViewMap[paymentType] = searchView
        searchViewMapInitSignal.emit(true)
    }

    override fun toggleSearchView(paymentType: String, show: Boolean) {
        if (show) {
            ViewUtil.toggleView(flSearchBankContainer, true)
            if (EmptyUtil.isNotNull(flSearchBank) && searchViewMap.containsKey(paymentType)) {
                flSearchBank.removeAllViews()
                flSearchBank.addView(searchViewMap[paymentType])
            }
        } else {
            ViewUtil.toggleView(flSearchBankContainer, false)
        }
    }
}