package com.khalti.form.EBanking.loadBank;


import java.util.HashMap;

interface BankContract {

    interface View {

        HashMap<?, ?> receiveArguments();

        void setUpToolbar(String title);

        void setupWebView(String url, String postData);

        void toggleIndentedProgress(boolean show);

        void showIndentedError(String message);

        void showIndentedNetworkError();

        void setListener(BankContract.Listener listener);
    }


    interface Listener {

        void toggleIndentedProgress(boolean show);

        void showIndentedError(String message);

        void showIndentedNetworkError();

        void setupLayout(boolean isNetwork);
    }
}
