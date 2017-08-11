package com.khalti.form.EBanking;

import java.util.HashMap;

interface EBankingContract {
    interface View {
        void toggleProgressBar(boolean show);

        void showBankField();

        void setUpSpinner(Object banks, Object bankIds);

        void setUpBankItem(String bankName, String bankId);

        void openBankList(HashMap<String, Object> dataMap);

        void setListener(Listener listener);
    }

    interface Listener {
        void setUpLayout(boolean hasNetwork);

        void openBankList();

        void updateBankItem(String bankName, String bankId);
    }
}
