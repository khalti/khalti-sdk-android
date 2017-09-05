package khalti.checkOut.Wallet;

import android.support.annotation.NonNull;

import java.util.HashMap;

import khalti.checkOut.api.Config;
import khalti.checkOut.api.ErrorAction;
import khalti.checkOut.api.OnCheckOutListener;
import khalti.rxBus.Event;
import khalti.utils.EmptyUtil;
import khalti.utils.GuavaUtil;
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

    public WalletPresenter(@NonNull WalletContract.View mWalletView) {
        this.mWalletView = GuavaUtil.checkNotNull(mWalletView);
        mWalletView.setListener(this);
        walletModel = new WalletModel();
    }

    @Override
    public void setUpLayout() {
        config = Store.getConfig();
        mWalletView.setEditTextListener();
        mWalletView.setButtonText("Pay Rs " + StringUtil.formatNumber(NumberUtil.convertToRupees(config.getAmount())));
        mWalletView.setButtonClickListener();
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
    public void showMessageDialog(String title, String message) {
        mWalletView.showMessageDialog(title, message);
    }

    @Override
    public void initiatePayment(boolean isNetwork, String mobile) {
        if (isNetwork) {
            if (EmptyUtil.isNotEmpty(mobile) && ValidationUtil.isMobileNumberValid(mobile)) {
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
                            mWalletView.showInteractiveMessageDialog("Error", mWalletView.getMessage("pin_not_set") + "\n\n" +
                                    mWalletView.getMessage("pin_not_set_continue"));
                            config.getOnCheckOutListener().onError(ErrorAction.WALLET_INITIATE.getAction(), mWalletView.getMessage("pin_not_set"));
                        } else {
                            mWalletView.showMessageDialog("Error", message);
                            config.getOnCheckOutListener().onError(ErrorAction.WALLET_INITIATE.getAction(), message);
                        }
                    }
                }));
            } else {
                if (EmptyUtil.isEmpty(mobile)) {
                    mWalletView.setEditTextError("mobile", "This field is required");
                } else {
                    mWalletView.setEditTextError("mobile", "Invalid mobile number");
                }
            }
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
                        WalletInitPojo walletInitPojo = (WalletInitPojo) o;
                        mWalletView.toggleProgressDialog("confirm", false);
                        OnCheckOutListener onCheckOutListener = Store.getConfig().getOnCheckOutListener();
                        HashMap<String, Object> data = new HashMap<>();
                        data.putAll((EmptyUtil.isNotNull(config.getAdditionalData()) && EmptyUtil.isNotEmpty(config.getAdditionalData())) ? config.getAdditionalData() : new HashMap<>());
                        data.put("amount", config.getAmount());
                        data.put("product_url", config.getProductUrl());
                        data.put("token", walletInitPojo.getToken());
                        data.put("product_name", config.getProductName());
                        data.put("product_identity", config.getProductId());

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
