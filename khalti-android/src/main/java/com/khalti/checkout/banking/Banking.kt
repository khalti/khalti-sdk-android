package com.khalti.checkout.banking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.khalti.R
import com.khalti.checkout.banking.contactForm.ContactFormFragment
import com.khalti.checkout.banking.helper.BankAdapter
import com.khalti.checkout.banking.helper.BankPojo
import com.khalti.checkout.banking.helper.BankingData
import com.khalti.checkout.helper.BaseComm
import com.khalti.databinding.BankingBinding
import com.khalti.databinding.ComponentBankSearchBinding
import com.khalti.signal.Signal
import com.khalti.utils.*
import java.util.*

class Banking : Fragment(), BankingContract.View {

    private lateinit var binding: BankingBinding

    private var fragmentActivity: FragmentActivity? = null
    private lateinit var bankAdapter: BankAdapter

    private lateinit var presenter: BankingContract.Presenter

    private lateinit var baseComm: BaseComm


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = BankingBinding.inflate(inflater, container, false)
        fragmentActivity = activity
        presenter = BankingPresenter(this)

        baseComm = Store.getBaseComm()

        presenter.onCreate()
        return binding.root
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
        ViewUtil.toggleView(binding.llIndented, show)
        ViewUtil.toggleViewInvisible(binding.flLoad, show)
        ViewUtil.toggleView(binding.tvMessage, false)
        ViewUtil.toggleView(binding.btnTryAgain, false)
    }

    override fun toggleSearchError(show: Boolean) {
//        ViewUtil.toggleView(binding.rvList, !show)
        ViewUtil.toggleView(binding.llIndented, show)
        ViewUtil.toggleView(binding.tvMessage, true)
        ViewUtil.setText(binding.tvMessage, ResourceUtil.getString(fragmentActivity, R.string.no_banks))
    }

    override fun setupList(bankList: MutableList<BankPojo>): Signal<Map<String, String>> {
        ViewUtil.toggleView(binding.llTopBar, true)
        ViewUtil.toggleView(binding.llContainer, true)
        bankAdapter = BankAdapter(bankList)
        binding.rvList.adapter = bankAdapter
        binding.rvList.setHasFixedSize(false)
        val layoutManager = GridLayoutManager(fragmentActivity, 3)
        binding.rvList.layoutManager = layoutManager

        return bankAdapter.getClickedItem()
    }

    override fun setupSearch(paymentType: String): Signal<String> {
        val view = ComponentBankSearchBinding.inflate(LayoutInflater.from(context), binding.nsvContainer, false)
        val searchView = view.svBank

        baseComm.addSearchView(paymentType, searchView)
        return ViewUtil.setSearchListener(searchView)
    }

    override fun showIndentedNetworkError() {
        ViewUtil.toggleViewInvisible(binding.flLoad, false)
        ViewUtil.toggleViewInvisible(binding.btnTryAgain, false)
        ViewUtil.toggleView(binding.tvMessage, true)
        ViewUtil.setText(binding.tvMessage, ResourceUtil.getString(fragmentActivity, R.string.network_error_body))
    }

    override fun showIndentedError(error: String) {
        ViewUtil.toggleViewInvisible(binding.flLoad, false)
        ViewUtil.toggleView(binding.btnTryAgain, true)
        ViewUtil.toggleView(binding.tvMessage, true)
        ViewUtil.setText(binding.tvMessage, error)
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
            put("try_again", ViewUtil.setClickListener(binding.btnTryAgain))
        }
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