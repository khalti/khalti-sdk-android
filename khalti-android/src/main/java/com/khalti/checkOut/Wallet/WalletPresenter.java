package com.khalti.checkOut.Wallet;

import androidx.annotation.NonNull;

import java.util.HashMap;

import com.khalti.checkOut.Wallet.helper.WalletConfirmPojo;
import com.khalti.checkOut.Wallet.helper.WalletInitPojo;
import com.khalti.checkOut.helper.Config;
import com.khalti.checkOut.api.ErrorAction;
import com.khalti.checkOut.api.OnCheckOutListener;
import com.khalti.utils.Constant;
import com.khalti.utils.EmptyUtil;
import com.khalti.utils.GuavaUtil;
import com.khalti.utils.HtmlUtil;
import com.khalti.utils.NumberUtil;
import com.khalti.utils.Store;
import com.khalti.utils.StringUtil;
import com.khalti.utils.ValidationUtil;

import rx.Observable;
import rx.Subscriber;
import rx.subscriptions.CompositeSubscription;

public class WalletPresenter implements WalletContract.Presenter {
    @NonNull
    private final WalletContract.View view;
    private WalletModel model;
    private Config config;
    private CompositeSubscription compositeSubscription;

    private String pinWebLink;
    private int clicks = 0;

    public WalletPresenter(@NonNull WalletContract.View view) {
        this.view = GuavaUtil.checkNotNull(view);
        view.setPresenter(this);
        model = new WalletModel();
        compositeSubscription = new CompositeSubscription();
    }

    @Override
    public void onCreate() {
        config = Store.getConfig();
        if (EmptyUtil.isNotNull(config.getMobile()) && EmptyUtil.isNotEmpty(config.getMobile()) && ValidationUtil.isMobileNumberValid(config.getMobile())) {
            view.setMobile(config.getMobile());
        }
        view.setButtonText("Pay Rs " + StringUtil.formatNumber(NumberUtil.convertToRupees(config.getAmount())));
        view.showBranding();

        compositeSubscription.add(view.setImageClickListener().subscribe(aVoid -> {
            clicks += 1;
            if (clicks > 2) {
                clicks = 0;
                view.showSlogan();
            }
        }));
        HashMap<String, Observable<CharSequence>> map = view.setEditTextListener();
        compositeSubscription.add(map.get("mobile").subscribe(charSequence -> {
            view.setEditTextError("mobile", null);
            if (view.getPayButtonText().toLowerCase().contains("confirm")) {
                view.toggleConfirmationLayout(false);
            }
        }));
        compositeSubscription.add(map.get("code").subscribe(charSequence -> view.setConfirmationLayoutHeight("code")));
        compositeSubscription.add(map.get("pin").subscribe(charSequence -> view.setConfirmationLayoutHeight("pin")));
        compositeSubscription.add(view.setButtonClickListener().subscribe(aVoid -> {
            HashMap<String, String> dataMap = view.getFormData();
            if (view.getPayButtonText().toLowerCase().contains("confirm")) {
                if (isFinalFormValid(dataMap.get("pin"), dataMap.get("code"))) {
                    confirmPayment(view.hasNetwork(), dataMap.get("code"), dataMap.get("pin"));
                } else {
                    updateConfirmationHeight();
                }
            } else {
                if (isMobileValid(dataMap.get("mobile"))) {
                    initiatePayment(view.hasNetwork(), dataMap.get("mobile"));
                }
            }
        }));
    }

    @Override
    public void onDestroy() {
        if (EmptyUtil.isNotNull(compositeSubscription) && compositeSubscription.hasSubscriptions() && !compositeSubscription.isUnsubscribed()) {
            compositeSubscription.unsubscribe();
        }
        model.unSubscribe();
    }

    @Override
    public void openKhaltiSettings() {
        view.openKhaltiSettings();
    }

    @Override
    public void openLinkInBrowser() {
        view.openLinkInBrowser(Constant.url + pinWebLink.substring(1));
    }

    @Override
    public void showPINInBrowserDialog(String title, String message) {
        view.showPINInBrowserDialog(title, message);
    }

    @Override
    public boolean isMobileValid(String mobile) {
        if (EmptyUtil.isNotEmpty(mobile) && ValidationUtil.isMobileNumberValid(mobile)) {
            return true;
        } else {
            if (EmptyUtil.isEmpty(mobile)) {
                view.setEditTextError("mobile", "This field is required");
            } else {
                view.setEditTextError("mobile", "Enter a valid mobile number");
            }
        }
        return false;
    }

    @Override
    public boolean isFinalFormValid(String pin, String confirmationCode) {
        String status = "";
        if (EmptyUtil.isNotEmpty(pin)) {
            if (pin.length() == 4) {
                status += "pc";
            } else {
                status += "pl";
            }
        } else {
            status += "pe";
        }

        if (EmptyUtil.isNotEmpty(confirmationCode)) {
            if (confirmationCode.length() == 6) {
                status += "cc";
            } else {
                status += "cl";
            }
        } else {
            status += "ce";
        }

        switch (status) {
            case "pccc":
                return true;
            case "pccl":
                view.setEditTextError("code", "Enter a valid 6 digit confirmation code");
                return false;
            case "pcce":
                view.setEditTextError("code", "This field is required");
                return false;
            case "plcc":
                view.setEditTextError("pin", "Enter a valid 4 digit PIN");
                return false;
            case "plcl":
                view.setEditTextError("pin", "Enter a valid 4 digit PIN");
                view.setEditTextError("code", "Enter a valid 6 digit confirmation code");
                return false;
            case "plce":
                view.setEditTextError("pin", "Enter a valid 4 digit PIN");
                view.setEditTextError("code", "This field is required");
                return false;
            case "pecc":
                view.setEditTextError("pin", "This field is required");
                return false;
            case "pecl":
                view.setEditTextError("pin", "This field is required");
                view.setEditTextError("code", "Enter a valid 6 digit confirmation code");
                return false;
            case "pece":
                view.setEditTextError("pin", "This field is required");
                view.setEditTextError("code", "This field is required");
                return false;
        }
        return false;
    }

    @Override
    public void initiatePayment(boolean isNetwork, String mobile) {
        if (isNetwork) {
            view.toggleProgressDialog("init", true);
            compositeSubscription.add(model.initiatePayment(mobile, config)
                    .subscribe(new Subscriber<WalletInitPojo>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            view.toggleProgressDialog("init", false);
                            if (e.getMessage().contains("</a>")) {
                                pinWebLink = HtmlUtil.getHrefLink(e.getMessage());
                                view.showPINDialog("Error", view.getMessage("pin_not_set") + "\n\n" +
                                        view.getMessage("pin_not_set_continue"));
                                config.getOnCheckOutListener().onError(ErrorAction.WALLET_INITIATE.getAction(), view.getMessage("pin_not_set"));
                            } else {
                                view.showMessageDialog("Error", e.getMessage());
                                config.getOnCheckOutListener().onError(ErrorAction.WALLET_INITIATE.getAction(), e.getMessage());
                            }
                        }

                        @Override
                        public void onNext(WalletInitPojo walletInitPojo) {
                            view.toggleProgressDialog("init", false);
                            view.toggleConfirmationLayout(true);
                            view.togglePinMessage(walletInitPojo.isPinCreated());
                            view.setPinMessage(walletInitPojo.getPinCreatedMessage());
                        }
                    }));
        } else {
            view.showNetworkError();
        }
    }

    @Override
    public void confirmPayment(boolean isNetwork, String confirmationCode, String transactionPin) {
        if (isNetwork) {
            view.toggleProgressDialog("confirm", true);
            compositeSubscription.add(model.confirmPayment(confirmationCode, transactionPin)
                    .subscribe(new Subscriber<WalletConfirmPojo>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            view.toggleProgressDialog("confirm", false);
                            view.showMessageDialog("Error", e.getMessage());
                            config.getOnCheckOutListener().onError(ErrorAction.WALLET_CONFIRM.getAction(), e.getMessage());
                        }

                        @Override
                        public void onNext(WalletConfirmPojo walletConfirmPojo) {
                            view.toggleProgressDialog("confirm", false);
                            OnCheckOutListener onCheckOutListener = config.getOnCheckOutListener();
                            HashMap<String, Object> data = new HashMap<>();
                            data.putAll((EmptyUtil.isNotNull(config.getAdditionalData()) && EmptyUtil.isNotEmpty(config.getAdditionalData())) ? config.getAdditionalData() : new HashMap<>());
                            data.put("amount", walletConfirmPojo.getAmount());
                            data.put("product_url", walletConfirmPojo.getProductUrl());
                            data.put("token", walletConfirmPojo.getToken());
                            data.put("product_name", walletConfirmPojo.getProductName());
                            data.put("product_identity", walletConfirmPojo.getProductIdentity());

                            onCheckOutListener.onSuccess(data);
                            view.closeWidget();
                        }
                    }));
        } else {
            view.showNetworkError();
        }
    }

    @Override
    public void updateConfirmationHeight() {
        view.updateConfirmationHeight();
    }

    @Override
    public void unSubscribe() {
        if (compositeSubscription.hasSubscriptions() && !compositeSubscription.isUnsubscribed()) {
            compositeSubscription.unsubscribe();
        }
    }

    public void injectModel(WalletModel walletModel) {
        this.model = walletModel;
    }

    public void injectConfig(Config config) {
        this.config = config;
    }
}
