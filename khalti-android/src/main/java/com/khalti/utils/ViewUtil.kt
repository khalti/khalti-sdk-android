package com.khalti.utils

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.TextView
import com.khalti.signal.Signal

class ViewUtil {

    companion object {

        fun setClickListener(view: View): Signal<Any> {
            val signal = Signal<Any>()
            if (EmptyUtil.isNotNull(view)) {
                view.setOnClickListener {
                    signal.emit("")
                }
            }
            return signal
        }

        fun setTextChangeListener(view: TextView): Signal<CharSequence> {
            val signal = Signal<CharSequence>()

            if (EmptyUtil.isNotNull(view)) {
                view.addTextChangedListener(object : TextWatcher {
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

        fun getText(view: TextView): String {
            if (EmptyUtil.isNotNull(view)) {
                return view.text.toString()
            }

            return ""
        }
    }
}