package khalti.checkOut.Wallet;

import android.support.annotation.NonNull;

import java.util.HashMap;

import khalti.R;
import khalti.checkOut.api.Config;
import khalti.checkOut.api.ErrorAction;
import khalti.checkOut.api.OnCheckOutListener;
import khalti.rxBus.Event;
import khalti.utils.DataHolder;
import khalti.utils.EmptyUtil;
import khalti.utils.GuavaUtil;
import khalti.utils.NumberUtil;
import khalti.utils.StringUtil;
import khalti.utils.ValidationUtil;
import rx.subscriptions.CompositeSubscription;

class WalletPresenter implements WalletContract.Listener {
    @NonNull
    private final WalletContract.View mWalletView;
    private WalletModel walletModel;
    private CompositeSubscription compositeSubscription;
    private boolean smsListenerInitialized = false;
    private OnCheckOutListener onCheckOutListener;

    WalletPresenter(@NonNull WalletContract.View mWalletView) {
        this.mWalletView = GuavaUtil.checkNotNull(mWalletView);
        mWalletView.setListener(this);
        walletModel = new WalletModel();
        onCheckOutListener = DataHolder.getConfig().getOnCheckOutListener();
    }

    @Override
    public void setUpLayout() {
        mWalletView.setEditTextListener();
        mWalletView.setButtonText("Pay Rs " + StringUtil.formatNumber(NumberUtil.convertToRupees(DataHolder.getConfig().getAmount())));
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
                compositeSubscription.add(walletModel.initiatePayment(mobile, DataHolder.getConfig(), new WalletModel.WalletAction() {
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
                            mWalletView.showInteractiveMessageDialog("Error", mWalletView.getStringFromResource(R.string.pin_not_set) + "\n\n" +
                                    mWalletView.getStringFromResource(R.string.pin_not_set_continue));
                            onCheckOutListener.onError(ErrorAction.WALLET_INITIATE.getAction(), mWalletView.getStringFromResource(R.string.pin_not_set));
                        } else {
                            mWalletView.showMessageDialog("Error", message);
                            onCheckOutListener.onError(ErrorAction.WALLET_INITIATE.getAction(), message);
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
                        OnCheckOutListener onCheckOutListener = DataHolder.getConfig().getOnCheckOutListener();
                        HashMap<String, Object> data = new HashMap<>();
                        Config config = DataHolder.getConfig();
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
                        onCheckOutListener.onError(ErrorAction.WALLET_CONFIRM.getAction(), message);
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
}
