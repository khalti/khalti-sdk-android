package com.khalti.checkOut

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.khalti.R
import com.khalti.checkOut.helper.PaymentPreference
import com.khalti.utils.*
import kotlinx.android.synthetic.main.component_tab.view.*
import kotlinx.android.synthetic.main.payment_activity.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

public class CheckOutActivity : AppCompatActivity(), CheckOutContract.View {

    public var cdlMain: CoordinatorLayout? = null
    private lateinit var presenter: CheckOutContract.Presenter
    private val tabs: MutableList<TabLayout.Tab?> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.payment_activity)

        this.cdlMain = clMain

        presenter = CheckOutPresenter(this)
        presenter.onCreate()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
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

        tlTitle.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                vpContent.currentItem = tab.position
                presenter.onTabSelected(tab.position, true)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                presenter.onTabSelected(tab.position, false)
            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
    }

    override fun setUpTabLayout(types: List<PaymentPreference>) {
        var position = 0
        for (p in types) {
            val color: Int
            val map = MerchantUtil.getTab(p.value.toLowerCase())
            if (EmptyUtil.isNotNull(map)) {
                color = if (position == 0) {
                    ResourceUtil.getColor(this, R.color.khaltiAccentAlt)
                } else {
                    ResourceUtil.getColor(this, R.color.khaltiPrimary)
                }

                val llTab: LinearLayout = LayoutInflater.from(this).inflate(R.layout.component_tab, tlTitle, false) as LinearLayout
                llTab.tvTitle.text = map["title"].toString()
                llTab.tvTitle.setTextColor(color)
                llTab.ivIcon.setImageResource(map["icon"] as Int)
                DrawableCompat.setTint(llTab.ivIcon.drawable, color)
                val tab: TabLayout.Tab? = tlTitle.getTabAt(position)
                if (EmptyUtil.isNotNull(tab)) {
                    tab!!.customView = llTab
                }
                tabs.add(tab)

                position++
            }
        }
    }

    override fun toggleTab(position: Int, selected: Boolean) {
        val ll: LinearLayout? = tabs[position]?.customView as LinearLayout?
        if (EmptyUtil.isNotNull(ll)) {

            if (selected) {
                ll?.tvTitle?.setTextColor(ResourceUtil.getColor(this, R.color.khaltiAccentAlt))
                DrawableCompat.setTint(ll?.ivIcon?.drawable!!, ResourceUtil.getColor(this, R.color.khaltiAccentAlt))
            } else {
                ll?.tvTitle?.setTextColor(ResourceUtil.getColor(this, R.color.khaltiPrimary))
                DrawableCompat.setTint(ll?.ivIcon?.drawable!!, ResourceUtil.getColor(this, R.color.khaltiPrimary))
            }
        }
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