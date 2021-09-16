package com.khalti.checkout.banking.contactForm

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.fragment.app.FragmentActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.khalti.R
import com.khalti.checkout.banking.helper.BankingData
import com.khalti.checkout.helper.BaseComm
import com.khalti.checkout.helper.Config
import com.khalti.databinding.BankingContactBinding
import com.khalti.signal.Signal
import com.khalti.utils.*
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.util.*

class ContactFormFragment : BottomSheetDialogFragment(), ContactFormContract.View {
    private lateinit var binding: BankingContactBinding

    private lateinit var fragmentActivity: FragmentActivity
    private lateinit var presenter: ContactFormContract.Presenter
    private lateinit var baseComm: BaseComm

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = BankingContactBinding.inflate(inflater, container, false)
        fragmentActivity = requireActivity()

        baseComm = Store.getBaseComm()
        presenter = ContactFormPresenter(this)
        presenter.onCreate()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.onDestroy()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog

        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
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
        get() = ViewUtil.getText(binding.etContact)

    override val isNetworkAvailable: Boolean
        get() = NetworkUtil.isNetworkAvailable(fragmentActivity)

    override fun receiveData(): BankingData? {
        val bundle = this.arguments
        return if (EmptyUtil.isNotNull(bundle)) {
            bundle!!.getSerializable("data") as BankingData
        } else null
    }

    override fun setBankData(logo: String, name: String, icon: String) {
        ViewUtil.setText(binding.tvBankName, name)
        if (EmptyUtil.isNotNull(binding.ivBankLogo) && EmptyUtil.isNotNull(logo) && EmptyUtil.isNotEmpty(logo)) {
            Picasso.get()
                .load(logo)
                .noFade()
                .into(binding.ivBankLogo, object : Callback {
                    override fun onSuccess() {
                        ViewUtil.toggleView(binding.flBankLogo, true)
                        ViewUtil.toggleView(binding.flBankTextIcon, false)
                        ViewUtil.setText(binding.tvBankIcon, icon)
                    }

                    override fun onError(e: Exception) {
                        e.printStackTrace()

                        ViewUtil.toggleView(binding.flBankLogo, false)
                        ViewUtil.toggleView(binding.flBankTextIcon, true)
                        ViewUtil.setText(binding.tvBankIcon, icon)
                    }
                })

        } else {
            ViewUtil.toggleView(binding.flBankLogo, false)
            ViewUtil.toggleView(binding.flBankTextIcon, true)
            ViewUtil.setText(binding.tvBankIcon, icon)
        }
    }

    override fun setButtonText(text: String) {
        if (EmptyUtil.isNotNull(binding.btnPay)) {
            ViewUtil.setText(binding.btnPay.root, text)
        }
    }

    override fun setEditTextError(error: String?) {
        binding.tilContact.isErrorEnabled = EmptyUtil.isNotNull(error)
        binding.tilContact.error = error
    }

    override fun setMobile(mobile: String) {
        ViewUtil.setText(binding.etContact, mobile)
        binding.etContact.setSelection(mobile.length)
    }

    override fun showMessageDialog(title: String, message: String) {
        UserInterfaceUtil.showInfoDialog(
            fragmentActivity,
            title,
            message,
            autoDismiss = true,
            cancelable = true,
            positiveText = ResourceUtil.getString(fragmentActivity, R.string.got_it),
            negativeText = null
        )
    }

    override fun showError(message: String) {
        UserInterfaceUtil.showSnackBar(fragmentActivity, baseComm.getCoordinator(), message, null, Snackbar.LENGTH_LONG)
    }

    override fun showNetworkError() {
        UserInterfaceUtil.showSnackBar(
            fragmentActivity,
            baseComm.getCoordinator(),
            ResourceUtil.getString(fragmentActivity, R.string.network_error_body),
            null,
            Snackbar.LENGTH_LONG
        )
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
            put("pay", ViewUtil.setClickListener(binding.btnPay.root))
        }
    }

    override fun setEditTextListener(): Signal<CharSequence> {
        return ViewUtil.setTextChangeListener(binding.etContact)
    }

    override fun setPresenter(presenter: ContactFormContract.Presenter) {
        this.presenter = presenter
    }
}
