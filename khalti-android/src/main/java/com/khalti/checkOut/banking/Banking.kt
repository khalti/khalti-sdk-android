package com.khalti.checkOut.banking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.khalti.R
import com.khalti.checkOut.CheckOutActivity
import com.khalti.checkOut.banking.contactForm.ContactFormFragment
import com.khalti.checkOut.banking.helper.BankAdapter
import com.khalti.checkOut.banking.helper.BankPojo
import com.khalti.checkOut.banking.helper.BankingData
import com.khalti.signal.Signal
import com.khalti.utils.EmptyUtil
import com.khalti.utils.NetworkUtil
import com.khalti.utils.Pair
import com.khalti.utils.ResourceUtil
import com.khalti.utils.ViewUtil
import kotlinx.android.synthetic.main.banking.view.*
import java.util.*

class Banking : Fragment(), BankingContract.View {

    private lateinit var mainView: View
    private var fragmentActivity: FragmentActivity? = null
    private lateinit var bankAdapter: BankAdapter

    private lateinit var presenter: BankingContract.Presenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mainView = inflater.inflate(R.layout.banking, container, false)
        fragmentActivity = activity
        presenter = BankingPresenter(this)

        presenter.onCreate()

        return mainView
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

    override fun receiveData(): Map<String, Any>? {
        val bundle = arguments
        if (EmptyUtil.isNotNull(bundle)) {
            return bundle!!.getSerializable("map") as Map<String, Any>
        }
        return null
    }

    override fun toggleIndented(show: Boolean) {
        ViewUtil.toggleView(mainView.llIndented, show)
        ViewUtil.toggleViewInvisible(mainView.flLoad, show)
        ViewUtil.toggleView(mainView.tvMessage, false)
        ViewUtil.toggleView(mainView.btnTryAgain, false)
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
        ViewUtil.toggleView(mainView.llContainer, true)
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

    override fun setSearchListener(): Signal<Pair<String, String>> {
        return (this.fragmentActivity as CheckOutActivity).searchSignal
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

    override fun setPresenter(presenter: BankingContract.Presenter) {
        this.presenter = presenter
    }
}
