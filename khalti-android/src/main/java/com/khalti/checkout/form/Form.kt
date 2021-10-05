package com.khalti.checkout.form

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.material.snackbar.Snackbar
import com.khalti.R
import com.khalti.checkout.helper.BaseComm
import com.khalti.checkout.helper.PaymentPreference
import com.khalti.databinding.FormBinding
import com.khalti.signal.Signal
import com.khalti.utils.*
import java.util.*
import kotlin.math.abs

class Form : Fragment(), FormContract.View {
    private lateinit var binding: FormBinding

    private var progressDialog: AppCompatDialog? = null
    private lateinit var fragmentActivity: FragmentActivity
    private lateinit var presenter: FormContract.Presenter

    private var height: Int = 0
    private lateinit var baseComm: BaseComm

    private var isKhalti = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FormBinding.inflate(inflater, container, false)
        fragmentActivity = requireActivity()
        presenter = FormPresenter(this)

        baseComm = Store.getBaseComm()
        presenter.onCreate()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.onDestroy()
    }

    override val packageName: String get() = fragmentActivity.packageName

    override fun receiveData(): Map<String, Any>? {
        val bundle = arguments
        if (EmptyUtil.isNotNull(bundle)) {
            return bundle!!.getSerializable("map") as Map<String, Any>
        }
        return null
    }

    override val payButtonText: String get() = ViewUtil.getText(binding.btnPay)

    override val formData: Map<String, String>
        get() = object : HashMap<String, String>() {
            init {
                put("mobile", ViewUtil.getText(binding.etMobile))
                put("code", ViewUtil.getText(binding.etCode))
                put("pin", ViewUtil.getText(binding.etPIN))
            }
        }

    override fun toggleProgressDialog(action: String, show: Boolean): Signal<Boolean> {
        var signal = Signal<Boolean>()
        var message = ""
        when (action) {
            "init" -> message = ResourceUtil.getString(fragmentActivity, R.string.init_payment)
            "confirm" -> message = ResourceUtil.getString(fragmentActivity, R.string.confirming_payment)
        }

        if (show) {
            val pair = UserInterfaceUtil.runProgressDialog(
                fragmentActivity,
                message,
                ResourceUtil.getString(fragmentActivity, R.string.please_wait)
            )
            progressDialog = pair.first
            signal = pair.second
        } else {
            progressDialog?.dismiss()
        }
        return signal
    }

    override fun toggleConfirmationLayout(show: Boolean) {
        val buttonText = if (show) ResourceUtil.getString(
            fragmentActivity,
            R.string.confirm_payment
        ) else "Pay Rs ${NumberUtil.convertToRupees(Store.getConfig().amount)}"
        binding.btnPay.text = buttonText
        binding.etCode.setText("")
        binding.elConfirmation.toggleExpansion()
    }

    override fun togglePinLayout(show: Boolean) {
        ViewUtil.toggleView(binding.llPIN, show)
    }

    override fun togglePinMessage(show: Boolean) {
        ViewUtil.toggleView(binding.llPINMessage, show)
    }

    override fun toggleMobileLabel(paymentType: String) {
        val label = when (paymentType) {
            PaymentPreference.KHALTI.value -> {
                ResourceUtil.getString(fragmentActivity, R.string.mobile_hint)
            }
            PaymentPreference.CONNECT_IPS.value, PaymentPreference.SCT.value -> {
                ResourceUtil.getString(fragmentActivity, R.string.contact_hint)
            }
            else -> ResourceUtil.getString(fragmentActivity, R.string.mobile_hint)
        }
        ViewUtil.setHint(binding.tilMobile, label)
    }

    override fun toggleAttemptRemaining(show: Boolean) {
        if (EmptyUtil.isNotNull(binding.elAttempts)) {
            binding.elAttempts.setExpanded(show, true)
        }
    }

    override fun setPinMessage(message: String) {
//        ViewUtil.setText(binding.tvPinMessage, message + " " + ResourceUtil.getString(fragmentActivity, R.string.sms_info))
    }

    override fun setAttemptsRemaining(attempts: String) {
        ViewUtil.setText(binding.tvAttemptsRemaining, attempts)
    }

    override fun setMobile(mobile: String) {
        binding.etMobile.setText(mobile)
        binding.etMobile.setSelection(mobile.length)
    }

    override fun setEditTextError(view: String, error: String?) {
        val isError = EmptyUtil.isNotNull(error)
        when (view) {
            "mobile" -> {
                binding.tilMobile.isErrorEnabled = isError
                binding.tilMobile.error = error
            }
            "pin" -> {
                binding.tilPIN.isErrorEnabled = isError
                binding.tilPIN.error = error
            }
            "code" -> {
                binding.llCode.measure(0, 0)
                val beforeA = binding.llCode.measuredHeight

                binding.tilCode.isErrorEnabled = isError
                binding.tilCode.error = error

                binding.llCode.measure(0, 0)
                val afterA = binding.llCode.measuredHeight

                height = abs(height + (afterA - beforeA))
            }
        }
    }

    override fun setButtonText(text: String) {
        binding.btnPay.text = text
    }

    override fun setConfirmationLayoutHeight() {
        val til = binding.tilCode
        val ll: LinearLayout = binding.llCode
        if (EmptyUtil.isNotNull(til.error)) {
            ll.measure(0, 0)
            val beforeA = ll.measuredHeight

            til.isErrorEnabled = false

            ll.measure(0, 0)
            val afterA = ll.measuredHeight

            if (EmptyUtil.isNotNull(beforeA) && EmptyUtil.isNotNull(afterA)) {
                height = abs(height + (beforeA - afterA))

                val layoutParams = binding.llConfirmation.layoutParams as ExpandableLayout.LayoutParams
                layoutParams.height = binding.llConfirmation.height - height
                binding.llConfirmation.layoutParams = layoutParams

                height = 0
            }
        }
    }

    override fun setEditTextListener(): Map<String, Signal<CharSequence>> = object : HashMap<String, Signal<CharSequence>>() {
        init {
            put("mobile", ViewUtil.setTextChangeListener(binding.etMobile))
            put("code", ViewUtil.setTextChangeListener(binding.etCode))
            put("pin", ViewUtil.setTextChangeListener(binding.etPIN))
        }
    }

    override fun setClickListener(): Map<String, Signal<Any>> = object : HashMap<String, Signal<Any>>() {
        init {
            put("pay", ViewUtil.setClickListener(binding.btnPay))
            if (isKhalti) {
                put("khalti", ViewUtil.setClickListener(binding.ivBranding))
            }
            put("pin", ViewUtil.setClickListener(binding.btnSetPin))
        }
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

    override fun showMessageDialog(title: String, message: String, actionListener: Boolean): Signal<Boolean> {
        val cancelText = if (actionListener) ResourceUtil.getString(fragmentActivity, R.string.cancel) else null
        return UserInterfaceUtil.showInfoDialog(
            fragmentActivity,
            title,
            message,
            autoDismiss = true,
            cancelable = true,
            positiveText = ResourceUtil.getString(fragmentActivity, R.string.ok),
            negativeText = cancelText
        )
    }

    override fun showSlogan() {
        Toast.makeText(fragmentActivity, ResourceUtil.getString(fragmentActivity, R.string.slogan), Toast.LENGTH_SHORT).show()
    }

    override fun showBranding(paymentType: String) {
//        ViewUtil.toggleView(binding.llBranding, true)
        var drawable: Int? = null
        when (paymentType) {
            PaymentPreference.KHALTI.value -> {
                isKhalti = true
                drawable = R.drawable.khalti_logo_full
            }
            PaymentPreference.CONNECT_IPS.value -> {
                drawable = R.drawable.connect_ips
            }
            PaymentPreference.SCT.value -> {
                drawable = R.drawable.sct
            }
        }

        if (EmptyUtil.isNotNull(binding.ivBranding)) {
            binding.ivBranding.setImageResource(drawable!!)
        }
    }

    override fun openKhaltiSettings() {
        var i: Intent?
        val manager = fragmentActivity.packageManager
        try {
            i = manager.getLaunchIntentForPackage("com.khalti.red")
            i = if (EmptyUtil.isNotNull(i)) i else manager.getLaunchIntentForPackage("com.khalti")
            if (EmptyUtil.isNull(i)) {
                throw PackageManager.NameNotFoundException()
            }
            i!!.addCategory(Intent.CATEGORY_LAUNCHER)

            val map = HashMap<String, String>()
            map["pin_settings"] = "pin_settings"
            i.putExtra("map", map)

            startActivity(i)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
    }

    override fun openLinkInBrowser(link: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        startActivity(browserIntent)
    }

    override fun closeWidget() {
        fragmentActivity.finish()
    }

    override fun updateConfirmationHeight() {
        val layoutParams = binding.llConfirmation.layoutParams as ExpandableLayout.LayoutParams
        layoutParams.height = binding.llConfirmation.height + height
        binding.llConfirmation.layoutParams = layoutParams

        height = 0
    }

    override fun openBanking(url: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
    }

    override fun getMessage(action: String): String {
        return when (action) {
            "pin_not_set" -> ResourceUtil.getString(fragmentActivity, R.string.pin_not_set)
            "pin_not_set_continue" -> ResourceUtil.getString(fragmentActivity, R.string.pin_not_set_continue)
            "khalti_not_found" -> ResourceUtil.getString(fragmentActivity, R.string.khalti_not_found)
            "set_pin_in_browser" -> ResourceUtil.getString(fragmentActivity, R.string.set_pin_in_browser)
            "pin_error" -> ResourceUtil.getString(fragmentActivity, R.string.pin_error)
            else -> ""
        }
    }

    override fun hasNetwork(): Boolean {
        return NetworkUtil.isNetworkAvailable(fragmentActivity)
    }

    override fun doesPackageExist(): Boolean {
        val manager = fragmentActivity.packageManager
        return try {
            var i = manager.getLaunchIntentForPackage("com.khalti.red")
            if (EmptyUtil.isNull(i)) {
                i = manager.getLaunchIntentForPackage("com.khalti")
            }

            EmptyUtil.isNotNull(i)

        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            false
        }
    }

    override fun clearForm() {
        binding.etMobile.setText("")
        binding.etCode.setText("")
        binding.etPIN.setText("")
    }

    override fun getBackPressedSignal(): Signal<Any> {
        return baseComm.getBackPressedSignal()
    }

    override fun setPresenter(presenter: FormContract.Presenter) {
        this.presenter = presenter
    }
}