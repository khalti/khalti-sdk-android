package com.khalti.checkout.helper

import androidx.appcompat.widget.SearchView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.khalti.signal.Signal

interface BaseComm {

    fun getCoordinator(): CoordinatorLayout?

    fun addSearchView(paymentType: String, searchView: SearchView)

    fun toggleSearchView(paymentType: String, show: Boolean)

    fun getBackPressedSignal(): Signal<Any>
}