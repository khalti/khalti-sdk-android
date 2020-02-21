package com.khalti.checkout.banking.deepLinkReceiver

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.khalti.R
import com.khalti.checkout.helper.Config
import com.khalti.utils.EmptyUtil
import com.khalti.utils.FileStorageUtil
import com.khalti.utils.JsonUtil

class DeepLink : AppCompatActivity(), DeepLinkContract.View {

    private lateinit var presenter: DeepLinkContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.empty_activity)

        presenter = DeepLinkPresenter(this)
        presenter.onCreate()
    }

    override val configFromFile: Config
        get() = FileStorageUtil.readFromFile(this, "khalti_config") as Config

    override fun receiveEBankingData(): Map<String, Any>? {
        val bundle = intent.extras
        return if (EmptyUtil.isNotNull(bundle)) {
            JsonUtil.getEBankingData(bundle!!.getString("data"))
        } else null
    }

    override fun closeDeepLink() {
        finish()
    }

    override fun setPresenter(presenter: DeepLinkContract.Presenter) {
        this.presenter = presenter
    }
}