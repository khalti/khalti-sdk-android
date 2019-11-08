package com.khalti.utils

import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import com.khalti.signal.Signal

class ViewUtil {

    companion object {

        private val handler = Handler()
        private lateinit var runnable: Runnable

        fun setClickListener(view: View?): Signal<Any> {
            val signal = Signal<Any>()
            if (EmptyUtil.isNotNull(view)) {
                view!!.setOnClickListener {
                    signal.emit("")
                }
            }
            return signal
        }

        fun setTextChangeListener(view: TextView?): Signal<CharSequence> {
            val signal = Signal<CharSequence>()

            if (EmptyUtil.isNotNull(view)) {
                view!!.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(p0: Editable?) {
                    }

                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        if (EmptyUtil.isNotNull(p0)) {
                            signal.emit(p0!!)
                        }
                    }
                })
            }
            return signal
        }

        fun setSearchListener(view: SearchView?): Signal<String> {
            val signal = Signal<String>()

            if (EmptyUtil.isNotNull(view)) {
                view!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String): Boolean {
                        return true
                    }

                    override fun onQueryTextChange(newText: String): Boolean {
                        if (::runnable.isInitialized) {
                            handler.removeCallbacks(runnable)
                        }

                        runnable = Runnable {
                            signal.emit(newText)
                        }
                        handler.postDelayed(runnable, 500)
                        return true
                    }
                })
            }
            return signal
        }

        fun getText(view: TextView?): String {
            if (EmptyUtil.isNotNull(view)) {
                return view!!.text.toString()
            }

            return ""
        }

        fun toggleView(view: View?, show: Boolean) {
            if (EmptyUtil.isNotNull(view)) {
                view!!.visibility = if (show) View.VISIBLE else View.GONE
            }
        }

        fun toggleViewInvisible(view: View?, show: Boolean) {
            if (EmptyUtil.isNotNull(view)) {
                view!!.visibility = if (show) View.VISIBLE else View.INVISIBLE
            }
        }

        fun setText(view: TextView?, text: String?) {
            if (EmptyUtil.isNotNull(view)) {
                view!!.text = text
            }
        }
    }
}