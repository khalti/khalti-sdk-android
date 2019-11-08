package com.khalti.checkOut.wallet

import com.khalti.checkOut.api.ErrorAction
import com.khalti.checkOut.api.Result
import com.khalti.checkOut.helper.Config
import com.khalti.checkOut.wallet.helper.WalletInitPojo
import com.khalti.signal.CompositeSignal
import com.khalti.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class WalletPresenter(view: WalletContract.View) : WalletContract.Presenter {

    private val view: WalletContract.View = GuavaUtil.checkNotNull<WalletContract.View>(view)
    private lateinit var config: Config
    private val compositeSignal = CompositeSignal()
    private val model = WalletModel()

    private val parentJob = Job()
    private val scope = CoroutineScope(Dispatchers.Main + parentJob)

    private var pinWebLink: String? = null
    private var clicks = 0

    private var mobile = ""

    private lateinit var walletInitPojo: WalletInitPojo

    init {
        view.setPresenter(this)
    }

    override fun onCreate() {
        config = Store.getConfig()
        val mobile = config.mobile
        if (EmptyUtil.isNotNull(mobile) && EmptyUtil.isNotEmpty(mobile) && ValidationUtil.isMobileNumberValid(mobile)) {
            view.setMobile(mobile!!)
        }

        view.setButtonText("Pay Rs " + StringUtil.formatNumber(NumberUtil.convertToRupees(config.amount)))
        view.showBranding()

        val clickMap = view.setClickListener()
        if (EmptyUtil.isNotNull(clickMap["pay"])) {
            compositeSignal.add(clickMap.getValue("pay").connect {

                val dataMap = view.formData
                if (view.payButtonText.toLowerCase().contains("confirm")) {
                    if (isFinalFormValid(dataMap.getValue("pin"), dataMap.getValue("code"))) {
                        onConfirmPayment(view.hasNetwork(), dataMap.getValue("code"), dataMap.getValue("pin"))
                    } else {
                        view.updateConfirmationHeight()
                    }
                } else {
                    if (isMobileValid(dataMap.getValue("mobile"))) {
                        onInitiatePayment(view.hasNetwork(), dataMap.getValue("mobile"))
                    }
                }
            })
        }

        if (EmptyUtil.isNotNull(clickMap["khalti"])) {
            compositeSignal.add(clickMap.getValue("khalti").connect {
                clicks += 1
                if (clicks > 2) {
                    clicks = 0
                    view.showSlogan()
                }
            })
        }

        val watcherMap = view.setEditTextListener()
        if (EmptyUtil.isNotNull(watcherMap["mobile"])) {
            compositeSignal.add(watcherMap.getValue("mobile")
                    .connect {
                        view.setEditTextError("mobile", null)
                        if (view.payButtonText.toLowerCase().contains("confirm")) {
                            view.toggleConfirmationLayout(false)
                        }
                    })
        }

        if (EmptyUtil.isNotNull(watcherMap["code"])) {
            compositeSignal.add(watcherMap.getValue("code")
                    .connect { view.setConfirmationLayoutHeight("code") })
        }

        if (EmptyUtil.isNotNull(watcherMap["pin"])) {
            compositeSignal.add(watcherMap.getValue("pin")
                    .connect { view.setConfirmationLayoutHeight("pin") })
        }
    }

    override fun onDestroy() {
        compositeSignal.clear()
        parentJob.cancel()
    }

    override fun isMobileValid(mobile: String): Boolean {
        if (EmptyUtil.isNotEmpty(mobile) && ValidationUtil.isMobileNumberValid(mobile)) {
            return true
        } else {
            if (EmptyUtil.isEmpty(mobile)) {
                view.setEditTextError("mobile", "This field is required")
            } else {
                view.setEditTextError("mobile", "Enter a valid mobile number")
            }
        }
        return false
    }

    override fun isFinalFormValid(pin: String, confirmationCode: String): Boolean {
        var status = ""
        status += if (EmptyUtil.isNotEmpty(pin)) {
            if (pin.length == 4) {
                "pc"
            } else {
                "pl"
            }
        } else {
            "pe"
        }

        status += if (EmptyUtil.isNotEmpty(confirmationCode)) {
            if (confirmationCode.length == 6) {
                "cc"
            } else {
                "cl"
            }
        } else {
            "ce"
        }

        when (status) {
            "pccc" -> return true
            "pccl" -> {
                view.setEditTextError("code", "Enter a valid 6 digit confirmation code")
                return false
            }
            "pcce" -> {
                view.setEditTextError("code", "This field is required")
                return false
            }
            "plcc" -> {
                view.setEditTextError("pin", "Enter a valid 4 digit PIN")
                return false
            }
            "plcl" -> {
                view.setEditTextError("pin", "Enter a valid 4 digit PIN")
                view.setEditTextError("code", "Enter a valid 6 digit confirmation code")
                return false
            }
            "plce" -> {
                view.setEditTextError("pin", "Enter a valid 4 digit PIN")
                view.setEditTextError("code", "This field is required")
                return false
            }
            "pecc" -> {
                view.setEditTextError("pin", "This field is required")
                return false
            }
            "pecl" -> {
                view.setEditTextError("pin", "This field is required")
                view.setEditTextError("code", "Enter a valid 6 digit confirmation code")
                return false
            }
            "pece" -> {
                view.setEditTextError("pin", "This field is required")
                view.setEditTextError("code", "This field is required")
                return false
            }
        }
        return false
    }

    override fun onInitiatePayment(isNetwork: Boolean, mobile: String) {
        if (isNetwork) {
            this.mobile = mobile
            scope.launch {
                view.toggleProgressDialog("init", true)
                when (val result = model.initiatePayment(mobile, config)) {
                    is Result.Success -> {
                        walletInitPojo = result.data
                        view.toggleProgressDialog("init", false)
                        view.toggleConfirmationLayout(true)
                        view.togglePinMessage(walletInitPojo.isPinCreated)
                        if (EmptyUtil.isNotNull(walletInitPojo.pinCreatedMessage)) {
                            view.setPinMessage(walletInitPojo.pinCreatedMessage!!)
                        }
                    }
                    is Result.Error -> {
                        view.toggleProgressDialog("init", false)
                        var message = result.throwable.message
                        if (EmptyUtil.isNotNull(message) && message!!.contains("</a>")) {
                            pinWebLink = HtmlUtil.getHrefLink(message)

                            compositeSignal.add(view.showPINDialog("Error", view.getMessage("pin_not_set") + "\n\n" + view.getMessage("pin_not_set_continue"))
                                    .connect { it ->
                                        if (it) {
                                            if (view.doesPackageExist()) {
                                                view.openKhaltiSettings()
                                            } else {
                                                compositeSignal.add(view.showPINDialog("Error", view.getMessage("khalti_not_found") + "\n\n" + view.getMessage("set_pin_in_browser"))
                                                        .connect {
                                                            if (it && EmptyUtil.isNotNull(pinWebLink)) {

                                                                view.openLinkInBrowser(Constant.url + pinWebLink!!.substring(1))
                                                            }
                                                        })
                                            }
                                        }
                                    })
                            config.onCheckOutListener.onError(ErrorAction.WALLET_INITIATE.action, message)
                        } else {
                            if (EmptyUtil.isNull(message)) {
                                message = ""
                            }
                            view.showMessageDialog("Error", message!!)
                        }
                    }
                }
            }
        } else {
            view.showNetworkError()
        }
    }

    override fun onConfirmPayment(isNetwork: Boolean, confirmationCode: String, transactionPin: String) {
        if (isNetwork) {
            scope.launch {
                if (EmptyUtil.isNotNull(walletInitPojo) && EmptyUtil.isNotNull(walletInitPojo.token)) {
                    view.toggleProgressDialog("confirm", true)
                    when (val result = model.confirmPayment(confirmationCode, transactionPin, walletInitPojo.token!!)) {
                        is Result.Success -> {
                            view.toggleProgressDialog("confirm", false)
                            val data = HashMap<String, Any>()
                            if (EmptyUtil.isNotNull(config.additionalData) && EmptyUtil.isNotEmpty(config.additionalData)) {
                                val walletConfirmPojo = result.data
                                data.putAll(config.additionalData!!)
                                data["mobile"] = mobile
                                if (EmptyUtil.isNotNull(walletConfirmPojo.amount)) {
                                    data["amount"] = walletConfirmPojo.amount!!
                                }
                                if (EmptyUtil.isNotNull(walletConfirmPojo.productUrl)) {
                                    data["product_url"] = walletConfirmPojo.productUrl!!
                                }
                                if (EmptyUtil.isNotNull(walletConfirmPojo.token)) {
                                    data["token"] = walletConfirmPojo.token!!
                                }
                                if (EmptyUtil.isNotNull(walletConfirmPojo.productName)) {
                                    data["product_name"] = walletConfirmPojo.productName!!
                                }
                                if (EmptyUtil.isNotNull(walletConfirmPojo.productIdentity)) {
                                    data["product_identity"] = walletConfirmPojo.productIdentity!!
                                }

                                config.onCheckOutListener.onSuccess(data)
                                view.closeWidget()
                            }

                        }

                        is Result.Error -> {
                            var message = result.throwable.message
                            if (EmptyUtil.isNull(message)) {
                                message = ""
                            }
                            view.toggleProgressDialog("confirm", false)
                            view.showMessageDialog("Error", message!!)
                            config.onCheckOutListener.onError(ErrorAction.WALLET_CONFIRM.action, message)
                        }

                    }
                }
            }
        } else {
            view.showNetworkError()
        }
    }
}