package com.khalti.widget


import com.khalti.checkOut.api.Config

internal interface PayContract {
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

        fun checkConfig(config: Config): String

        fun setCustomButtonView()

        fun setButtonStyle(id: Int)

        fun setButtonText(text: String)

        fun setButtonClick()

        fun openForm()

        fun destroyCheckOut()
    }
}
