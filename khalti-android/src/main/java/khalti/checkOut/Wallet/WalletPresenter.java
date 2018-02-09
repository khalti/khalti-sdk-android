package khalti.checkOut.Wallet;

import android.support.annotation.NonNull;

import java.util.HashMap;

import khalti.checkOut.api.Config;
import khalti.rxBus.Event;
import khalti.utils.Constant;
import khalti.utils.EmptyUtil;
import khalti.utils.GuavaUtil;
import khalti.utils.NumberUtil;
import khalti.utils.Store;
import khalti.utils.StringUtil;
import khalti.utils.ValidationUtil;
import rx.Observable;
import rx.subscriptions.CompositeSubscription;

public class WalletPresenter implements WalletContract.Presenter {
    @NonNull
    private final WalletContract.View view;
    private WalletModel model;
    private boolean smsListenerInitialized = false;
    private Config config;
    private String pinWebLink;
    private CompositeSubscription compositeSubscription;

    public WalletPresenter(@NonNull WalletContract.View view) {
        this.view = GuavaUtil.checkNotNull(view);
        view.setPresenter(this);
        model = new WalletModel();
        compositeSubscription = new CompositeSubscription();
    }

    @Override
    public void onCreate() {
        config = Store.getConfig();
        view.setButtonText("Pay Rs " + StringUtil.formatNumber(NumberUtil.convertToRupees(config.getAmount())));

        HashMap<String, Observable<CharSequence>> map = view.setEditTextListener();
        compositeSubscription.add(map.get("mobile").subscribe());
         view.setEditTextListener();
        view.setButtonClickListener();
    }

    @Override
    public void onDestroy() {
        if (EmptyUtil.isNotNull(compositeSubscription) && compositeSubscription.hasSubscriptions() && !compositeSubscription.isUnsubscribed()) {
            compositeSubscription.unsubscribe();
        }
        model.unSubscribe();
        toggleSmsListener(false);
    }

    @Override
    public void setUpLayout() {

    }

    @Override
    public void onSmsReceiptPermitted() {

    }

    @Override
    public void setConfirmationCode(Event event) {
        view.setConfirmationCode(event.getEvent().toString().replaceAll("\\D+", ""));
    }

    @Override
    public void toggleConfirmationLayout(boolean show) {
        view.toggleConfirmationLayout(show);
    }

    @Override
    public void toggleSmsListener(boolean listen) {
        view.toggleSmsListener(listen);
        smsListenerInitialized = false;
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
        /*if (isNetwork) {
            view.toggleProgressDialog("init", true);
            compositeSubscription = new CompositeSubscription();
            compositeSubscription.add(model.initiatePayment(mobile, config, new WalletModel.WalletAction() {
                @Override
                public void onCompleted(Object o) {
                    view.toggleSmsListener(!smsListenerInitialized);
                    smsListenerInitialized = true;
                    view.toggleProgressDialog("init", false);
                    view.toggleConfirmationLayout(true);
                }

                @Override
                public void onError(String message) {
                    view.toggleProgressDialog("init", false);
                    if (message.contains("</a>")) {
                        pinWebLink = HtmlUtil.getHrefLink(message);
                        view.showPINDialog("Error", view.getMessage("pin_not_set") + "\n\n" +
                                view.getMessage("pin_not_set_continue"));
                        config.getOnCheckOutListener().onError(ErrorAction.WALLET_INITIATE.getAction(), view.getMessage("pin_not_set"));
                    } else {
                        view.showMessageDialog("Error", message);
                        config.getOnCheckOutListener().onError(ErrorAction.WALLET_INITIATE.getAction(), message);
                    }
                }
            }));
        } else {
            view.showNetworkError();
        }*/
    }

    @Override
    public void confirmPayment(boolean isNetwork, String confirmationCode, String transactionPin) {
        /*if (isNetwork) {
            view.toggleProgressDialog("confirm", true);
            compositeSubscription = new CompositeSubscription();

            compositeSubscription.add(model.confirmPayment(confirmationCode, transactionPin, new WalletModel.WalletAction() {
                @Override
                public void onCompleted(Object o) {
                    WalletConfirmPojo walletConfirmPojo = (WalletConfirmPojo) o;
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

                @Override
                public void onError(String message) {
                    view.toggleProgressDialog("confirm", false);
                    view.showMessageDialog("Error", message);
                    config.getOnCheckOutListener().onError(ErrorAction.WALLET_CONFIRM.getAction(), message);
                }
            }));
        } else {
            view.showNetworkError();
        }*/
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
