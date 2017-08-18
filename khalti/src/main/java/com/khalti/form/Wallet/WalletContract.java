package com.khalti.form.Wallet;

interface WalletContract {
    interface View {

        void setListener(Listener listener);
    }

    interface Listener {
        void setUpLayout();
    }
}
