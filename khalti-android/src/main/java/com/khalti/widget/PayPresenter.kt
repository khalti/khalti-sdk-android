package com.khalti.widget

import com.khalti.checkout.helper.Config
import com.khalti.utils.ConfigUtil

class PayPresenter(private var view: PayContract.View) : PayContract.Presenter {

    override fun onCheckConfig(config: Config): String {
        return ConfigUtil.validateConfig(config)
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