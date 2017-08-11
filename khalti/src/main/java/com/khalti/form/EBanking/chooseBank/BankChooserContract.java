package com.khalti.form.EBanking.chooseBank;

import java.util.HashMap;
import java.util.List;

interface BankChooserContract {
    interface View {

        HashMap<?, ?> receiveArgument();

        void setUpToolbar();

        void setUpList(List<BankPojo> bankList);

        void setListener(BankChooserContract.Listener listener);
    }

    interface Listener {

        void setUpLayout();
    }
}
