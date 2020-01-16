package com.khalti.widget


import com.khalti.checkout.helper.Config

interface PayContract {
    interface View {

        fun setCustomButtonView()

        fun setButtonStyle(id: Int)

        fun setButtonText(text: String)

        fun setButtonClick()

        fun openForm()

        fun destroyCheckOut()

        fun setPresenter(presenter: Presenter)
    }

    interface Presenter {

        fun onCheckConfig(config: Config): String

        fun onSetCustomButtonView()

        fun onSetButtonStyle(id: Int)

        fun onSetButtonText(text: String)

        fun onSetButtonClick()

        fun onOpenForm()

        fun onDestroyCheckOut()
    }
}