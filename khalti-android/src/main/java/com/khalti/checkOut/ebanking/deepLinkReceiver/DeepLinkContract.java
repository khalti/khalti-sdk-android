package com.khalti.checkOut.ebanking.deepLinkReceiver;

import java.util.HashMap;

import com.khalti.checkOut.helper.Config;

interface DeepLinkContract {
    interface View {
        HashMap<String, Object> receiveEBankingData();

        Config getConfigFromFile();

        void closeDeepLink();

        void setListener(Listener listener);
    }

    interface Listener {
        void receiveEBankingData();
    }
}
