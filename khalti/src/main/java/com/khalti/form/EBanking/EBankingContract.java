package com.khalti.form.EBanking;

interface EBankingContract {
    interface View {

        void setListener(Listener listener);
    }

    interface Listener {
        void setUpLayout();
    }
}
