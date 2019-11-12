package com.khalti.checkOut.ebanking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.GridLayoutManager

import com.khalti.R
import com.khalti.checkOut.CheckOutActivity
import com.khalti.checkOut.ebanking.contactForm.ContactFormFragment
import com.khalti.checkOut.ebanking.helper.BankAdapter
import com.khalti.checkOut.ebanking.helper.BankPojo
import com.khalti.checkOut.ebanking.helper.BankingData
import com.khalti.signal.Signal
import com.khalti.utils.*
import kotlinx.android.synthetic.main.banking.view.*

import java.util.HashMap

class EBanking : Fragment(), EBankingContract.View {

    private lateinit var mainView: View
    private var fragmentActivity: FragmentActivity? = null
    private lateinit var bankAdapter: BankAdapter

    private lateinit var presenter: EBankingContract.Presenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mainView = inflater.inflate(R.layout.banking, container, false)
        fragmentActivity = activity
        presenter = EBankingPresenter(this)

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
        ViewUtil.toggleView((this.fragmentActivity as CheckOutActivity).flSearch, show)
    }

    override fun toggleSearchError(show: Boolean) {
//        ViewUtil.toggleView(mainView.rvList, !show)
        ViewUtil.toggleView(mainView.llIndented, show)
        ViewUtil.toggleView(mainView.tvMessage, true)
        ViewUtil.setText(mainView.tvMessage, ResourceUtil.getString(fragmentActivity, R.string.no_banks))
    }

    override fun setupList(bankList: MutableList<BankPojo>): Signal<Map<String, String>> {
        ViewUtil.toggleView(mainView.llTopBar, true)
        ViewUtil.toggleView(mainView.nsvContainer, true)
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
        return ViewUtil.setSearchListener((this.fragmentActivity as CheckOutActivity).svSearch)
    }

    override fun filterList(text: String): Int? {
        if (EmptyUtil.isNotNull(bankAdapter)) {
            return bankAdapter.filter(text)
        }
        return null
    }

    override fun hasNetwork(): Boolean {
        return NetworkUtil.isNetworkAvailable(fragmentActivity)
    }

    override fun setPresenter(presenter: EBankingContract.Presenter) {
        this.presenter = presenter
    }
}
