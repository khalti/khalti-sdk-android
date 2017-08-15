package com.khalti.form.Wallet;

interface WalletContract {
    interface View {

        void toggleEditTextListener(boolean set);

        void toggleProgressDialog(String action, boolean show);

        void toggleConfirmationLayout(boolean show);

        void setEditTextError(String view, String error);

        void setButtonText(String text);

        void setButtonClickListener();

        void showNetworkError();

        void showMessageDialog(String title, String message);

        void showInteractiveMessageDialog(String title, String message);

        String getStringFromResource(int id);

        void openKhaltiSettings();

        void setListener(Listener listener);
    }

    interface Listener {

        void setUpLayout();

        void toggleEditTextListener(boolean set);

        void toggleConfirmationLayout(boolean show);

        void openKhaltiSettings();

        void showMessageDialog(String title, String message);

        void initiatePayment(boolean isNetwork, String mobile);

        void unSubscribe();
    }
}
