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
import com.khalti.utils.ViewUtil
import kotlinx.android.synthetic.main.banking.*
import kotlinx.android.synthetic.main.banking.view.*
import kotlinx.android.synthetic.main.wallet_form.view.*

import java.util.HashMap

import rx.Observable
import rx.subjects.PublishSubject

class EBanking_ : Fragment(), EBankingContract.View {

    private lateinit var mainView: View
    private var fragmentActivity: FragmentActivity? = null
    private lateinit var bankAdapter: BankAdapter

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
        ViewUtil.toggleView(mainView.llIndented, show)
        ViewUtil.toggleViewInvisible(mainView.flLoad, show)
    }

    override fun toggleSearch(show: Boolean) {
        ViewUtil.toggleView(mainView.flSearchBank, show)
    }

    override fun toggleSearchError(show: Boolean) {
        ViewUtil.toggleView(mainView.rvList, !show)
        ViewUtil.toggleView(mainView.llIndented, show)
        ViewUtil.toggleView(mainView.tvMessage, true)
        ViewUtil.setText(tvMessage, ResourceUtil.getString(fragmentActivity, R.string.no_banks))
    }

    override fun setupList(bankList: MutableList<BankPojo>): Signal<Map<String, String>> {
        ViewUtil.toggleView(mainView.appBar, true)
        ViewUtil.toggleView(mainView.rvList, true)
        bankAdapter = BankAdapter(bankList)
        mainView.rvList.adapter = bankAdapter
        mainView.rvList.setHasFixedSize(false)
        val layoutManager = GridLayoutManager(fragmentActivity, 3)
        mainView.rvList.layoutManager = layoutManager

        return bankAdapter.getClickedItem()
    }

    override fun showIndentedNetworkError() {
        ViewUtil.toggleViewInvisible(mainView.flLoad, false)
        ViewUtil.toggleViewInvisible(mainView.btnTryAgain, false)
        ViewUtil.toggleView(mainView.tvMessage, true)
        ViewUtil.setText(mainView.tvMessage, ResourceUtil.getString(fragmentActivity, R.string.network_error_body))
    }

    override fun showIndentedError(error: String) {
        ViewUtil.toggleViewInvisible(mainView.flLoad, false)
        ViewUtil.toggleView(mainView.btnTryAgain, true)
        ViewUtil.toggleView(mainView.tvMessage, true)
        ViewUtil.setText(mainView.tvMessage, error)
    }

    override fun openMobileForm(bankingData: BankingData) {
        val contactFormFragment = ContactFormFragment()
        val bundle = Bundle()
        bundle.putSerializable("data", bankingData)
        contactFormFragment.arguments = bundle
        if (EmptyUtil.isNotNull(fragmentManager)) {
            contactFormFragment.show(fragmentManager!!, contactFormFragment.tag)
        }
    }

    override fun setOnClickListener(): Map<String, Signal<Any>> = object : HashMap<String, Signal<Any>>() {
        init {
            put("try_again", ViewUtil.setClickListener(mainView.btnTryAgain))
        }
    }

    override fun setSearchListener(): Signal<String> {
        return ViewUtil.setSearchListener(mainView.svBank)
    }

    override fun filterList(text: String): Signal<Int> {
        val signal = Signal<Int>()

        if (EmptyUtil.isNotNull(bankAdapter)) {
            fragmentActivity!!.runOnUiThread {
                val count = bankAdapter.setFilter(text)
                if (EmptyUtil.isNotNull(count)) {
                    signal.emit(count!!)
                }
            }
        }

        return signal
    }

    override fun hasNetwork(): Boolean {
        return NetworkUtil.isNetworkAvailable(fragmentActivity)
    }

    override fun setPresenter(presenter: EBankingContract.Presenter) {
        this.presenter = presenter
    }
}
