package khalti.checkOut.Wallet;


import java.util.HashMap;

import khalti.checkOut.Wallet.helper.WalletConfirmPojo;
import khalti.checkOut.Wallet.helper.WalletInitPojo;
import khalti.checkOut.api.Config;
import khalti.rxBus.Event;
import rx.Observable;

public interface WalletContract {
    interface View {

        void toggleProgressDialog(String action, boolean show);

        void toggleConfirmationLayout(boolean show);

        void toggleSmsListener(boolean listen);

        HashMap<String, Observable<CharSequence>> setEditTextListener();

        void setEditTextError(String view, String error);

        void setButtonText(String text);

        Observable<Void> setButtonClickListener();

        void setConfirmationCode(String code);

        void setConfirmationLayoutHeight(String view);

        void showNetworkError();

        void showMessageDialog(String title, String message);

        void showPINDialog(String title, String message);

        void showPINInBrowserDialog(String title, String message);

        void openKhaltiSettings();

        void openLinkInBrowser(String link);

        void closeWidget();

        void updateConfirmationHeight();

        String getMessage(String action);

        boolean hasSmsReceiptPermission();

        void askSmsReceiptPermission();

        boolean hasNetwork();

        String getPayButtonText();

        HashMap<String, String> getFormData();

        void setPresenter(Presenter presenter);
    }

    interface Presenter {

        void onCreate();

        void onDestroy();

        void onSmsReceiptPermitted();

        void setConfirmationCode(Event event);

        void toggleSmsListener(boolean listen);

        void openKhaltiSettings();

        void openLinkInBrowser();

        void showPINInBrowserDialog(String title, String message);

        boolean isMobileValid(String mobile);

        boolean isFinalFormValid(String pin, String confirmationCode);

        void initiatePayment(boolean isNetwork, String mobile);

        void confirmPayment(boolean isNetwork, String confirmationCode, String transactionPin);

        void updateConfirmationHeight();

        void unSubscribe();
    }

    interface Model {

        Observable<WalletInitPojo> initiatePayment(String mobile, Config config);

        Observable<WalletConfirmPojo> confirmPayment(String confirmationCode, String transactionPIN);

        void unSubscribe();
    }
}
