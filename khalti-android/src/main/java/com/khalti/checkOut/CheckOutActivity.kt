package com.khalti.checkOut

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.card.MaterialCardView
import com.google.android.material.tabs.TabLayout
import com.khalti.R
import com.khalti.checkOut.helper.PaymentPreference
import com.khalti.signal.Signal
import com.khalti.utils.*
import kotlinx.android.synthetic.main.component_tab.view.*
import kotlinx.android.synthetic.main.payment_activity.*

public class CheckOutActivity : AppCompatActivity(), CheckOutContract.View {

    public var cdlMain: CoordinatorLayout? = null
    public var flSearch: FrameLayout? = null
    public var svSearch: SearchView? = null
    private lateinit var presenter: CheckOutContract.Presenter
    private val tabs: MutableList<TabLayout.Tab?> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.payment_activity)

        this.cdlMain = clMain
        this.flSearch = flSearchBank
        this.svSearch = svBank

        presenter = CheckOutPresenter(this)

        presenter.onCreate()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

    override fun toggleTab(position: Int, selected: Boolean, id: String) {
        val mcTab = tabs[position]?.customView as MaterialCardView?
        if (EmptyUtil.isNotNull(mcTab)) {
            val tabMap = MerchantUtil.getTab(id)
            if (EmptyUtil.isNotNull(tabMap)) {
                if (selected) {
                    mcTab!!.tvTitle?.setTextColor(ResourceUtil.getColor(this, R.color.white))
                    mcTab.mcContainer.setCardBackgroundColor(ResourceUtil.getColor(this, R.color.khaltiPrimary))
                    mcTab.ivIcon.setImageResource(tabMap.getValue("icon_active") as Int)
                } else {
                    mcTab!!.tvTitle?.setTextColor(ResourceUtil.getColor(this, R.color.black))
                    mcTab.mcContainer.setCardBackgroundColor(ResourceUtil.getColor(this, R.color.white))
                    mcTab.ivIcon.setImageResource(tabMap.getValue("icon") as Int)
                }
            }
        }
    }

    override fun toggleToolbarShadow(show: Boolean) {
        ViewUtil.toggleView(vToolbarShadow, show)
    }

    override fun toggleSearch(show: Boolean) {
        ViewUtil.toggleView(flSearchBank, show)
    }

    override fun setupViewPager(types: List<PaymentPreference>) {
        val adapter = ViewPagerAdapter(supportFragmentManager)

        for (p in types) {
            val map = MerchantUtil.getTab(p.value.toLowerCase())
            if (EmptyUtil.isNotNull(map)) {
                adapter.addFrag(map["fragment"] as Fragment?, map["title"].toString())
            }
        }

        vpContent.adapter = adapter
        vpContent.offscreenPageLimit = adapter.count
        tlTitle.setupWithViewPager(vpContent)
        if (adapter.count > 2) {
            tlTitle.tabMode = TabLayout.MODE_SCROLLABLE
        } else {
            tlTitle.tabMode = TabLayout.MODE_FIXED
        }
    }

    override fun setUpTabLayout(types: List<PaymentPreference>): Signal<Map<String, Any>> {
        val signal = Signal<Map<String, Any>>()
        var position = 0

        for (i in types.indices) {
            var color: Int
            var background: Int
            var icon: Int
            val map = MerchantUtil.getTab(types[i].value.toLowerCase())
            if (EmptyUtil.isNotNull(map)) {
                if (position == 0) {
                    color = ResourceUtil.getColor(this, R.color.white)
                    background = ResourceUtil.getColor(this, R.color.khaltiPrimary)
                    icon = map.getValue("icon_active") as Int
                } else {
                    color = ResourceUtil.getColor(this, R.color.black)
                    background = ResourceUtil.getColor(this, R.color.white)
                    icon = map.getValue("icon") as Int
                }

                val mcTab = LayoutInflater.from(this).inflate(R.layout.component_tab, tlTitle, false) as MaterialCardView
                mcTab.tvTitle.text = map["title"].toString()
                mcTab.tvTitle.setTextColor(color)
                mcTab.mcContainer.setCardBackgroundColor(background)
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
                signal.emit(object : HashMap<String, Any>() {
                    init {
                        put("position", tab.position)
                        put("selected", false)
                        put("id", types[tab.position].value)
                    }
                })
            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })

        return signal
    }

    override fun setOffsetListener(): Signal<Int> {
        val signal = Signal<Int>()
        appBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            signal.emit(verticalOffset)
        })

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

    override fun setPresenter(presenter: CheckOutContract.Presenter) {
        this.presenter = presenter
    }
}