package com.khalti.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.widget.FrameLayout
import androidx.annotation.Keep
import androidx.core.view.ViewCompat
import com.khalti.R
import com.khalti.checkout.helper.Config
import com.khalti.checkout.helper.KhaltiCheckOut
import com.khalti.databinding.ComponentButtonBinding
import com.khalti.utils.EmptyUtil
import com.khalti.utils.ResourceUtil

@Keep
class KhaltiButton @JvmOverloads constructor(context: Context, private var attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    FrameLayout(context, attrs, defStyleAttr), KhaltiButtonInterface {

    private lateinit var binding: ComponentButtonBinding

    private lateinit var config: Config
    private var presenter: PayContract.Presenter
    private var customView: View? = null
    private var buttonStyle: Int = -1
    private var clickListener: OnClickListener? = null

    init {
        presenter = Pay().getPresenter()
        init()
    }

    override fun setText(buttonText: String) {
        presenter.onSetButtonText(buttonText)
    }

    override fun setCheckOutConfig(config: Config) {
        this.config = config
        val message = presenter.onCheckConfig(config)
        require(EmptyUtil.isEmpty(message)) { message }
    }

    override fun setCustomView(view: View) {
        this.customView = view
        presenter.onSetCustomButtonView()
        presenter.onSetButtonClick()
    }

    override fun setButtonStyle(style: ButtonStyle) {
        this.buttonStyle = style.value
        presenter.onSetButtonStyle(buttonStyle)
        presenter.onSetButtonClick()
    }

    override fun setOnClickListener(l: OnClickListener?) {
        super.setOnClickListener(l)
        this.clickListener = l
        presenter.onSetButtonClick()
    }

    private fun init() {
        val a = context.obtainStyledAttributes(attrs, R.styleable.KhaltiButton, 0, 0)
        val buttonText = a.getString(R.styleable.KhaltiButton_text)
        buttonStyle = a.getInt(R.styleable.KhaltiButton_khalti_button_style, -1)
        a.recycle()

        val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        if (EmptyUtil.isNotNull(inflater)) {
            binding = ComponentButtonBinding.inflate(inflater, this, true)

            if (EmptyUtil.isNotNull(buttonText)) {
                presenter.onSetButtonText(buttonText!!)
            }
            presenter.onSetButtonStyle(buttonStyle)
            presenter.onSetButtonClick()
        }
    }

    private inner class Pay : PayContract.View {

        private var presenter: PayContract.Presenter = PayPresenter(this)
        private lateinit var khaltiCheckOut: KhaltiCheckOut

        override fun setCustomButtonView() {
            if (EmptyUtil.isNotNull(customView)) {
                binding.btnPay.visibility = View.GONE
                binding.mrStyle.visibility = View.GONE
                binding.flCustomView.addView(customView)
            }
        }

        override fun setButtonStyle(id: Int) {
            var imageId = -1
            when (id) {
                0 -> imageId = R.drawable.bg_khalti
                1 -> imageId = R.drawable.bg_ebanking
                2 -> imageId = R.drawable.bg_mbanking
                3 -> imageId = R.drawable.bg_sct
                4 -> imageId = R.drawable.bg_connect_ips
            }

            if (imageId != -1) {
                binding.btnPay.visibility = View.GONE
                binding.flCustomView.visibility = View.GONE
                ViewCompat.setBackground(binding.mrStyle, ResourceUtil.getDrawable(context, imageId))
            }
        }

        override fun setButtonText(text: String) {
            binding.btnPay.text = text
        }

        override fun setButtonClick() {
            clickListener = if (EmptyUtil.isNull(clickListener)) OnClickListener {
                presenter.onOpenForm()
            } else clickListener

            when {
                EmptyUtil.isNotNull(customView) -> binding.flCustomView.getChildAt(0).setOnClickListener(clickListener)
                buttonStyle != -1 -> binding.mrStyle.setOnClickListener(clickListener)
                else -> binding.btnPay.setOnClickListener(clickListener)
            }
        }

        override fun openForm() {
            khaltiCheckOut = KhaltiCheckOut(context, config)
            khaltiCheckOut.show()
        }

        override fun destroyCheckOut() {
            require(EmptyUtil.isNotNull(khaltiCheckOut)) { "CheckOut cannot be destroyed before it is shown." }
            khaltiCheckOut.destroy()
        }

        override fun setPresenter(presenter: PayContract.Presenter) {
            this.presenter = presenter
        }

        fun getPresenter(): PayContract.Presenter {
            return presenter
        }
    }
}