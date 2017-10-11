package khalti.checkOut.Wallet;


import khalti.rxBus.Event;

public interface WalletContract {
    interface View {

        void toggleProgressDialog(String action, boolean show);

        void toggleConfirmationLayout(boolean show);

        void toggleSmsListener(boolean listen);

        void setEditTextListener();

        void setEditTextError(String view, String error);

        void setButtonText(String text);

        void setButtonClickListener();

        void setConfirmationCode(String code);

        void showNetworkError();

        void showMessageDialog(String title, String message);

        void showPINDialog(String title, String message);

        void showPINInBrowserDialog(String title, String message);

        void openKhaltiSettings();

        void openLinkInBrowser(String link);

        void closeWidget();

        String getMessage(String action);

        void setListener(Listener listener);
    }

    interface Listener {

        void setUpLayout();

        void setConfirmationCode(Event event);

        void toggleConfirmationLayout(boolean show);

        void toggleSmsListener(boolean listen);

        void openKhaltiSettings();

        void openLinkInBrowser();

        void showPINInBrowserDialog(String title, String message);

        boolean isMobileValid(String mobile);

        boolean isFinalFormValid(String pin, String confirmationCode);

        void initiatePayment(boolean isNetwork, String mobile);

        void confirmPayment(boolean isNetwork, String confirmationCode, String transactionPin);

        void unSubscribe();
    }
}
