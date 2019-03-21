package com.khalti.checkOut.EBanking.deepLinkReceiver;

import java.util.HashMap;

import com.khalti.checkOut.api.Config;

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
