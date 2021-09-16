package com.khalti.widget


import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.khalti.R

class TextInputLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : com.google.android.material.textfield.TextInputLayout(context, attrs, defStyleAttr) {
    private var mainHintTextSize: Float = 0.toFloat()
    private var editTextSize: Float = 0.toFloat()

    init {

        val a = context.obtainStyledAttributes(
                attrs, R.styleable.TextInputLayout)

        mainHintTextSize = a.getDimensionPixelSize(
                R.styleable.TextInputLayout_mainHintTextSize, 0).toFloat()

        a.recycle()
    }

    override fun addView(child: View, index: Int, params: ViewGroup.LayoutParams) {
        val b = child is EditText && mainHintTextSize > 0

        if (b) {
            val e = child as EditText
            editTextSize = e.textSize
            e.setTextSize(TypedValue.COMPLEX_UNIT_PX, mainHintTextSize)
        }

        super.addView(child, index, params)

        if (b) {
            editText?.setTextSize(TypedValue.COMPLEX_UNIT_PX, editTextSize)
        }
    }

    // Units are pixels.

    fun getMainHintTextSize(): Float {
        return mainHintTextSize
    }

    // This optional method allows for dynamic instantiation of this class and
    // its EditText, but it cannot be used after the EditText has been added.
    // Units are scaled pixels.

    fun setMainHintTextSize(size: Float) {
        check(editText == null) { "Hint text size must be set before EditText is added" }

        mainHintTextSize = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, size, resources.displayMetrics)
    }
}
