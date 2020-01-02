package com.khalti.checkOut.banking.contactForm

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.FragmentActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.khalti.R
import com.khalti.checkOut.banking.helper.BankingData
import com.khalti.checkOut.helper.Config
import com.khalti.signal.Signal
import com.khalti.utils.*
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.banking_contact.view.*
import kotlinx.android.synthetic.main.component_flat_button.view.*
import java.util.*

class ContactFormFragment : BottomSheetDialogFragment(), ContactFormContract.View {
    private lateinit var fragmentActivity: FragmentActivity
    private lateinit var presenter: ContactFormContract.Presenter
    private lateinit var mainView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mainView = inflater.inflate(R.layout.banking_contact, container, false)
        fragmentActivity = activity!!

        presenter = ContactFormPresenter(this)
        presenter.onCreate()

        return mainView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.onDestroy()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog

        dialog.setOnShowListener { dialog1 ->
            val d = dialog1 as BottomSheetDialog

            val bottomSheet = d.findViewById<FrameLayout>(R.id.design_bottom_sheet)
            if (EmptyUtil.isNotNull(bottomSheet)) {
                BottomSheetBehavior.from(bottomSheet!!).state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
        return dialog
    }

    override val packageName: String
        get() = fragmentActivity.packageName

    override val contactNumber: String
        get() = ViewUtil.getText(mainView.etContact)

    override val isNetworkAvailable: Boolean
        get() = NetworkUtil.isNetworkAvailable(fragmentActivity)

    override fun receiveData(): BankingData? {
        val bundle = this.arguments
        return if (EmptyUtil.isNotNull(bundle)) {
            bundle!!.getSerializable("data") as BankingData
        } else null
    }

    override fun setBankData(logo: String, name: String, icon: String) {
        ViewUtil.setText(mainView.tvBankName, name)
        if (EmptyUtil.isNotNull(mainView.ivBankLogo) && EmptyUtil.isNotNull(logo) && EmptyUtil.isNotEmpty(logo)) {
            Picasso.get()
                    .load(logo)
                    .noFade()
                    .into(mainView.ivBankLogo, object : Callback {
                        override fun onSuccess() {
                            ViewUtil.toggleView(mainView.flBankLogo, true)
                            ViewUtil.toggleView(mainView.flBankTextIcon, false)
                            ViewUtil.setText(mainView.tvBankIcon, icon)
                        }

                        override fun onError(e: Exception) {
                            e.printStackTrace()

                            ViewUtil.toggleView(mainView.flBankLogo, false)
                            ViewUtil.toggleView(mainView.flBankTextIcon, true)
                            ViewUtil.setText(mainView.tvBankIcon, icon)
                        }
                    })

        } else {
            ViewUtil.toggleView(mainView.flBankLogo, false)
            ViewUtil.toggleView(mainView.flBankTextIcon, true)
            ViewUtil.setText(mainView.tvBankIcon, icon)
        }
    }

    override fun setButtonText(text: String) {
        if (EmptyUtil.isNotNull(mainView.btnPay)) {
            ViewUtil.setText(mainView.btnPay.tvButton, text)
        }
    }

    override fun setEditTextError(error: String?) {
        mainView.tilContact?.isErrorEnabled = EmptyUtil.isNotNull(error)
        mainView.tilContact?.error = error
    }

    override fun setMobile(mobile: String) {
        ViewUtil.setText(mainView.etContact, mobile)
        mainView.etContact?.setSelection(mobile.length)
    }

    override fun showMessageDialog(title: String, message: String) {
        UserInterfaceUtil.showInfoDialog(fragmentActivity, title, message, autoDismiss = true, cancelable = true, positiveText = ResourceUtil.getString(fragmentActivity, R.string.got_it), negativeText = null)
    }

    override fun showError(message: String) {
        UserInterfaceUtil.showSnackBar(fragmentActivity, (this.fragmentActivity as com.khalti.checkOut.CheckOutActivity).cdlMain, message, null, Snackbar.LENGTH_LONG)
    }

    override fun showNetworkError() {
        UserInterfaceUtil.showSnackBar(fragmentActivity, (this.fragmentActivity as com.khalti.checkOut.CheckOutActivity).cdlMain, ResourceUtil.getString(fragmentActivity, R.string.network_error_body), null, Snackbar.LENGTH_LONG)

    }

    override fun openBanking(url: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        dismiss()
        startActivity(browserIntent)
    }

    override fun saveConfigInFile(config: Config) {
        FileStorageUtil.writeIntoFile(fragmentActivity, "khalti_config", config)
    }


    override fun setOnClickListener(): Map<String, Signal<Any>> = object : HashMap<String, Signal<Any>>() {
        init {
            put("pay", ViewUtil.setClickListener(mainView.btnPay))
        }
    }

    override fun setEditTextListener(): Signal<CharSequence> {
        return ViewUtil.setTextChangeListener(mainView.etContact)
    }

    override fun setPresenter(presenter: ContactFormContract.Presenter) {
        this.presenter = presenter
    }
}
