package com.khalti.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.Keep
import com.khalti.R
import com.khalti.checkout.helper.Config
import com.khalti.checkout.helper.KhaltiCheckOut
import com.khalti.utils.ConfigUtil
import com.khalti.utils.EmptyUtil
import com.khalti.utils.Store
import kotlinx.android.synthetic.main.component_button.view.*

@Keep
class KhaltiButton @JvmOverloads constructor(context: Context, private var attrs: AttributeSet? = null, private var defStyleAttr: Int = 0)
    : FrameLayout(context, attrs, defStyleAttr), KhaltiButtonInterface {

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

        if (EmptyUtil.isEmpty(message)) {
            val message2 = ConfigUtil.validateIfConfigIsSerializable(context, Store.getConfig())

            require(EmptyUtil.isEmpty(message2)) { message2 }
        } else {
            throw IllegalArgumentException(message)
        }
    }

    override fun setCustomView(view: View) {
        this.customView = view
        presenter.onSetCustomButtonView()
        presenter.onSetButtonClick()
    }

    override fun setButtonStyle(style: ButtonStyle) {
        this.buttonStyle = style.id
        presenter.onSetButtonStyle(buttonStyle)
        presenter.onSetButtonClick()
    }

    override fun showCheckOut() {
        presenter.onOpenForm()
    }

    override fun showCheckOut(config: Config) {
        this.config = config
        val message = presenter.onCheckConfig(config)

        if (EmptyUtil.isEmpty(message)) {
            val message2 = ConfigUtil.validateIfConfigIsSerializable(context, Store.getConfig())

            require(EmptyUtil.isEmpty(message2)) { message2 }
        } else {
            throw IllegalArgumentException(message)
        }
        presenter.onOpenForm()
    }

    override fun destroyCheckOut() {
        presenter.onDestroyCheckOut()
    }

    override fun setOnClickListener(l: OnClickListener?) {
        super.setOnClickListener(l)
        this.clickListener = l
        presenter.onSetButtonClick()
    }

    private fun init() {
        val a = context.obtainStyledAttributes(attrs, R.styleable.khalti, 0, 0)
        val buttonText = a.getString(R.styleable.khalti_text)
        buttonStyle = a.getInt(R.styleable.khalti_button_style, -1)
        a.recycle()

        val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        if (EmptyUtil.isNotNull(inflater)) {
            inflater.inflate(R.layout.component_button, this, true)

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
                btnPay.visibility = View.GONE
                flCustomView.addView(customView)
            }
        }

        override fun setButtonStyle(id: Int) {
            var imageId = -1
            when (id) {
                0 -> imageId = R.mipmap.ebanking_dark
                1 -> imageId = R.mipmap.ebanking_light
                2 -> imageId = R.mipmap.ebanking_light
                3 -> imageId = R.mipmap.ebanking_light
                4 -> imageId = R.mipmap.ebanking_light
            }

            if (imageId != -1) {
                btnPay.visibility = View.GONE
                flCustomView.visibility = View.GONE
                flStyle.setBackgroundResource(imageId)
            }
        }

        override fun setButtonText(text: String) {
            btnPay.text = text
        }

        override fun setButtonClick() {

            clickListener = if (EmptyUtil.isNull(clickListener)) OnClickListener {
                presenter.onOpenForm()
            } else clickListener

            when {
                EmptyUtil.isNotNull(customView) -> flCustomView.getChildAt(0).setOnClickListener(clickListener)
                buttonStyle != -1 -> flStyle.setOnClickListener(clickListener)
                else -> btnPay.setOnClickListener(clickListener)
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