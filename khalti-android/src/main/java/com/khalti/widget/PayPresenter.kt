package com.khalti.widget

import com.khalti.checkOut.api.Config
import com.khalti.utils.EmptyUtil

class PayPresenter(private var view: PayContract.View) : PayContract.Presenter {

    override fun onCheckConfig(config: Config): String {

        if (EmptyUtil.isNull(config.publicKey)) {
            return "Public key cannot be null"
        }
        if (EmptyUtil.isEmpty(config.publicKey)) {
            return "Public key cannot be empty"
        }
        if (EmptyUtil.isNull(config.productId)) {
            return "Product identity cannot be null"
        }
        if (EmptyUtil.isEmpty(config.productId)) {
            return "Product identity cannot be empty"
        }
        if (EmptyUtil.isNull(config.productName)) {
            return "Product name cannot be null"
        }
        if (EmptyUtil.isEmpty(config.productName)) {
            return "Product name cannot be empty"
        }
        if (EmptyUtil.isNull(config.amount)) {
            return "Amount cannot be null"
        }
        if (EmptyUtil.isEmpty(config.amount)) {
            return "Amount cannot be 0"
        }
        return if (EmptyUtil.isNull(config.onCheckOutListener)) {
            "Listener cannot be null"
        } else ""
    }

    override fun onSetCustomButtonView() {
        view.setCustomButtonView()
    }

    override fun onSetButtonStyle(id: Int) {
        view.setButtonStyle(id)
    }

    override fun onSetButtonText(text: String) {
        view.setButtonText(text)
    }

    override fun onSetButtonClick() {
        view.setButtonClick()
    }

    override fun onOpenForm() {
        view.openForm()
    }

    override fun onDestroyCheckOut() {
        view.destroyCheckOut()
    }
}