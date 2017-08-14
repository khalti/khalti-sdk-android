package com.khalti.form.Wallet;

interface WalletContract {
    interface View {

        void toggleEditTextListener(boolean set);

        void toggleConfirmationLayout(boolean show);

        void setEditTextError(String view, String error);

        void setButtonText(String text);

        void setButtonClickListener();

        void setListener(Listener listener);
    }

    interface Listener {

        void setUpLayout();

        void toggleEditTextListener(boolean set);

        void toggleConfirmationLayout(boolean show);

        void continuePayment(boolean isNetwork, String mobile);
    }
}
