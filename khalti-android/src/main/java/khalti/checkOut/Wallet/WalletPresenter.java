package khalti.checkOut.Wallet;

import android.support.annotation.NonNull;

import java.util.HashMap;

import khalti.checkOut.api.Config;
import khalti.checkOut.api.ErrorAction;
import khalti.checkOut.api.OnCheckOutListener;
import khalti.rxBus.Event;
import khalti.utils.Constant;
import khalti.utils.EmptyUtil;
import khalti.utils.GuavaUtil;
import khalti.utils.HtmlUtil;
import khalti.utils.NumberUtil;
import khalti.utils.Store;
import khalti.utils.StringUtil;
import khalti.utils.ValidationUtil;
import rx.subscriptions.CompositeSubscription;

public class WalletPresenter implements WalletContract.Listener {
    @NonNull
    private final WalletContract.View mWalletView;
    private WalletModel walletModel;
    private CompositeSubscription compositeSubscription;
    private boolean smsListenerInitialized = false;
    private Config config;
    private String pinWebLink;

    public WalletPresenter(@NonNull WalletContract.View mWalletView) {
        this.mWalletView = GuavaUtil.checkNotNull(mWalletView);
        mWalletView.setListener(this);
        walletModel = new WalletModel();
    }

    @Override
    public void setUpLayout() {
        config = Store.getConfig();
        mWalletView.setButtonText("Pay Rs " + StringUtil.formatNumber(NumberUtil.convertToRupees(config.getAmount())));
        mWalletView.setButtonClickListener();
        mWalletView.setEditTextListener();
    }

    @Override
    public void setConfirmationCode(Event event) {
        mWalletView.setConfirmationCode(event.getEvent().toString().replaceAll("\\D+", ""));
    }

    @Override
    public void toggleConfirmationLayout(boolean show) {
        mWalletView.toggleConfirmationLayout(show);
    }

    @Override
    public void toggleSmsListener(boolean listen) {
        mWalletView.toggleSmsListener(listen);
        smsListenerInitialized = false;
    }

    @Override
    public void openKhaltiSettings() {
        mWalletView.openKhaltiSettings();
    }

    @Override
    public void openLinkInBrowser() {
        mWalletView.openLinkInBrowser(Constant.url + pinWebLink.substring(1));
    }

    @Override
    public void showPINInBrowserDialog(String title, String message) {
        mWalletView.showPINInBrowserDialog(title, message);
    }

    @Override
    public boolean isMobileValid(String mobile) {
        if (EmptyUtil.isNotEmpty(mobile) && ValidationUtil.isMobileNumberValid(mobile)) {
            return true;
        } else {
            if (EmptyUtil.isEmpty(mobile)) {
                mWalletView.setEditTextError("mobile", "This field is required");
            } else {
                mWalletView.setEditTextError("mobile", "Invalid mobile number");
            }
        }
        return false;
    }

    @Override
    public void initiatePayment(boolean isNetwork, String mobile) {
        if (isNetwork) {
            mWalletView.toggleProgressDialog("init", true);
            compositeSubscription = new CompositeSubscription();
            compositeSubscription.add(walletModel.initiatePayment(mobile, config, new WalletModel.WalletAction() {
                @Override
                public void onCompleted(Object o) {
                    mWalletView.toggleSmsListener(!smsListenerInitialized);
                    smsListenerInitialized = true;
                    mWalletView.toggleProgressDialog("init", false);
                    mWalletView.toggleConfirmationLayout(true);
                }

                @Override
                public void onError(String message) {
                    mWalletView.toggleProgressDialog("init", false);
                    if (message.contains("</a>")) {
                        pinWebLink = HtmlUtil.getHrefLink(message);
                        mWalletView.showPINDialog("Error", mWalletView.getMessage("pin_not_set") + "\n\n" +
                                mWalletView.getMessage("pin_not_set_continue"));
                        config.getOnCheckOutListener().onError(ErrorAction.WALLET_INITIATE.getAction(), mWalletView.getMessage("pin_not_set"));
                    } else {
                        mWalletView.showMessageDialog("Error", message);
                        config.getOnCheckOutListener().onError(ErrorAction.WALLET_INITIATE.getAction(), message);
                    }
                }
            }));
        } else {
            mWalletView.showNetworkError();
        }
    }

    @Override
    public void confirmPayment(boolean isNetwork, String confirmationCode, String transactionPin) {
        if (isNetwork) {
            if (EmptyUtil.isNotEmpty(confirmationCode) && EmptyUtil.isNotEmpty(transactionPin)) {
                mWalletView.toggleProgressDialog("confirm", true);
                compositeSubscription = new CompositeSubscription();

                compositeSubscription.add(walletModel.confirmPayment(confirmationCode, transactionPin, new WalletModel.WalletAction() {
                    @Override
                    public void onCompleted(Object o) {
                        WalletConfirmPojo walletConfirmPojo = (WalletConfirmPojo) o;
                        mWalletView.toggleProgressDialog("confirm", false);
                        OnCheckOutListener onCheckOutListener = config.getOnCheckOutListener();
                        HashMap<String, Object> data = new HashMap<>();
                        data.putAll((EmptyUtil.isNotNull(config.getAdditionalData()) && EmptyUtil.isNotEmpty(config.getAdditionalData())) ? config.getAdditionalData() : new HashMap<>());
                        data.put("amount", walletConfirmPojo.getAmount());
                        data.put("product_url", walletConfirmPojo.getProductUrl());
                        data.put("token", walletConfirmPojo.getToken());
                        data.put("product_name", walletConfirmPojo.getProductName());
                        data.put("product_identity", walletConfirmPojo.getProductIdentity());

                        onCheckOutListener.onSuccess(data);
                        mWalletView.closeWidget();
                    }

                    @Override
                    public void onError(String message) {
                        mWalletView.toggleProgressDialog("confirm", false);
                        mWalletView.showMessageDialog("Error", message);
                        config.getOnCheckOutListener().onError(ErrorAction.WALLET_CONFIRM.getAction(), message);
                    }
                }));
            } else {
                if (EmptyUtil.isEmpty(confirmationCode) && EmptyUtil.isEmpty(transactionPin)) {
                    mWalletView.setEditTextError("code", "This field is required");
                    mWalletView.setEditTextError("pin", "This field is required");
                } else if (EmptyUtil.isEmpty(confirmationCode)) {
                    mWalletView.setEditTextError("code", "This field is required");
                } else {
                    mWalletView.setEditTextError("pin", "This field is required");
                }
            }
        } else {
            mWalletView.showNetworkError();
        }
    }

    @Override
    public void unSubscribe() {
        if (compositeSubscription.hasSubscriptions() && !compositeSubscription.isUnsubscribed()) {
            compositeSubscription.unsubscribe();
        }
    }

    public void injectModel(WalletModel walletModel) {
        this.walletModel = walletModel;
    }

    public void injectConfig(Config config) {
        this.config = config;
    }
}
