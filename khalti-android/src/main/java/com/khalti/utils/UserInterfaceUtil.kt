package com.khalti.utils

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.text.Html
import android.text.method.LinkMovementMethod
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.widget.AppCompatTextView
import android.view.View
import android.view.Window
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatDialog

import com.khalti.R
import com.khalti.signal.Signal

class UserInterfaceUtil {

    companion object {

        private var progressDialog: AppCompatDialog? = null
        private var infoDialog: Dialog? = null

        fun showSnackBar(context: Context, coordinatorLayout: CoordinatorLayout?, message: String, buttonText: String?, snackBarLength: Int): Signal<Any> {
            val signal = Signal<Any>()
            if (EmptyUtil.isNotNull(coordinatorLayout)) {
                val snackbar = Snackbar.make(coordinatorLayout!!, message, snackBarLength)
                if (EmptyUtil.isNotNull(buttonText) && EmptyUtil.isNotEmpty(buttonText)) {
                    snackbar.setActionTextColor(ResourceUtil.getColor(context, ResourceUtil.getColor(context, R.color.khaltiAccentAlt)))
                    snackbar.setAction(buttonText) { signal.emit("") }
                }
                snackbar.show()
            }

            return signal
        }

        fun runProgressDialog(context: Context, title: String, body: String): Pair<AppCompatDialog?, Signal<Boolean>> {
            val signal = Signal<Boolean>()
            progressDialog = AppCompatDialog(context)
            progressDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
            progressDialog!!.setContentView(R.layout.progress_dialog)
            if (EmptyUtil.isNotNull(progressDialog!!.window)) {
                progressDialog!!.window!!.setLayout(android.widget.LinearLayout.LayoutParams.MATCH_PARENT, android.widget.LinearLayout.LayoutParams.WRAP_CONTENT)
            }
            progressDialog!!.setCanceledOnTouchOutside(true)
            progressDialog!!.setCancelable(true)
            progressDialog!!.show()

            val tvTitle = progressDialog!!.findViewById<AppCompatTextView>(R.id.tvTitle)
            val tvBody = progressDialog!!.findViewById<AppCompatTextView>(R.id.tvBody)

            tvTitle?.text = title
            tvBody?.text = body

            progressDialog!!.setOnCancelListener { signal.emit(true) }
            progressDialog!!.setOnDismissListener { signal.emit(true) }

            return Pair(progressDialog, signal)
        }

        fun showInfoDialog(context: Context, title: String, body: String, autoDismiss: Boolean, cancelable: Boolean, positiveText: String, negativeText: String?): Signal<Boolean> {
            val signal = Signal<Boolean>()
            infoDialog = AppCompatDialog(context)
            infoDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
            infoDialog!!.setContentView(R.layout.info_dialog)
            if (EmptyUtil.isNotNull(infoDialog!!.window)) {
                infoDialog!!.window!!.setLayout(android.widget.LinearLayout.LayoutParams.MATCH_PARENT, android.widget.LinearLayout.LayoutParams.WRAP_CONTENT)
            }
            infoDialog!!.setCanceledOnTouchOutside(autoDismiss)
            infoDialog!!.setCancelable(cancelable)
            infoDialog!!.show()

            val tvTitle = infoDialog!!.findViewById<AppCompatTextView>(R.id.tvTitle)
            val tvBody = infoDialog!!.findViewById<AppCompatTextView>(R.id.tvBody)
            val tvPositive = infoDialog!!.findViewById<AppCompatTextView>(R.id.tvPositive)
            val tvNegative = infoDialog!!.findViewById<AppCompatTextView>(R.id.tvNegative)
            val flNegativeAction = infoDialog!!.findViewById<FrameLayout>(R.id.flNegativeAction)
            val flPositiveAction = infoDialog!!.findViewById<FrameLayout>(R.id.flPositiveAction)

            if (EmptyUtil.isNotNull(negativeText) && EmptyUtil.isNotEmpty(negativeText)) {
                flNegativeAction.visibility = View.VISIBLE
                tvNegative.text = negativeText
            }

            if (EmptyUtil.isNotNull(positiveText) && EmptyUtil.isNotEmpty(positiveText)) {
                tvPositive.text = positiveText
            }

            tvTitle.text = title
            tvBody.text = Html.fromHtml(body)
            tvBody.movementMethod = LinkMovementMethod.getInstance()

            flPositiveAction.setOnClickListener {
                infoDialog?.dismiss()
                signal.emit(true)
            }
            flNegativeAction.setOnClickListener {
                infoDialog?.dismiss()
                signal.emit(false)
            }

            return signal
        }

        fun dismissAllDialogs() {
            progressDialog?.dismiss()
            infoDialog?.dismiss()
        }

        private fun openAppSettings(context: Context) {
            val intent = Intent()
            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            val uri = Uri.fromParts("package", context.packageName, null)
            intent.data = uri
            context.startActivity(intent)
        }
    }

}
