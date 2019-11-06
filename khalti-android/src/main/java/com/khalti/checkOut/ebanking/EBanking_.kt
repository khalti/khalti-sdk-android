package com.khalti.checkOut.ebanking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.button.MaterialButton
import com.jakewharton.rxbinding.support.v7.widget.RxSearchView
import com.jakewharton.rxbinding.view.RxView
import com.khalti.R
import com.khalti.checkOut.ebanking.contactForm.ContactFormFragment
import com.khalti.checkOut.ebanking.helper.BankAdapter
import com.khalti.checkOut.ebanking.helper.BankPojo
import com.khalti.checkOut.ebanking.helper.BankingData
import com.khalti.checkOut.wallet.WalletContract
import com.khalti.signal.Signal
import com.khalti.utils.EmptyUtil
import com.khalti.utils.NetworkUtil
import com.khalti.utils.ResourceUtil

import java.util.HashMap

import rx.Observable
import rx.subjects.PublishSubject

class EBanking_ : Fragment(), EBankingContract.View {

    private lateinit var mainView: View
    private var fragmentActivity: FragmentActivity? = null
    private var bankAdapter: BankAdapter? = null

    private lateinit var presenter: EBankingContract.Presenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mainView = inflater.inflate(R.layout.banking, container, false)
        fragmentActivity = activity
        presenter = EBankingPresenter_(this)

        presenter.onCreate()

        return mainView
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

    override fun toggleIndented(show: Boolean) {

    }

    override fun toggleSearch(show: Boolean) {
    }

    override fun toggleSearchError(show: Boolean) {
    }

    override fun setupList(bankList: List<BankPojo>): Signal<Map<String, String>> {
    }

    override fun showIndentedNetworkError() {
    }

    override fun showIndentedError(error: String) {
    }

    override fun openMobileForm(bankingData: BankingData) {
    }

    override fun setOnClickListener(): Map<String, Signal<Any>> {
    }

    override fun setSearchListener(): Signal<CharSequence> {
    }

    override fun filterList(text: String): Signal<Int> {
    }

    override fun hasNetwork(): Boolean {
    }

    override fun setPresenter(presenter: EBankingContract.Presenter) {
    }
}
