package khalti.checkOut.Wallet;


import java.util.HashMap;

import khalti.checkOut.Wallet.helper.WalletConfirmPojo;
import khalti.checkOut.Wallet.helper.WalletInitPojo;
import khalti.checkOut.api.Config;
import rx.Observable;

public interface WalletContract {
    interface View {

        void toggleProgressDialog(String action, boolean show);

        void toggleConfirmationLayout(boolean show);

        void togglePinMessage(boolean show);

        void setPinMessage(String message);

        void setMobile(String mobile);

        HashMap<String, Observable<CharSequence>> setEditTextListener();

        void setEditTextError(String view, String error);

        void setButtonText(String text);

        Observable<Void> setButtonClickListener();

        Observable<Void> setImageClickListener();

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

        boolean hasNetwork();

        String getPayButtonText();

        HashMap<String, String> getFormData();

        void showSlogan();

        void showBranding();

        void setPresenter(Presenter presenter);
    }

    interface Presenter {

        void onCreate();

        void onDestroy();

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
