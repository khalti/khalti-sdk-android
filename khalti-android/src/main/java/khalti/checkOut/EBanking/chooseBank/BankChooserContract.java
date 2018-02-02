package khalti.checkOut.EBanking.chooseBank;

import java.util.HashMap;
import java.util.List;

import khalti.checkOut.EBanking.helper.BankPojo;

interface BankChooserContract {
    interface View {

        HashMap<?, ?> receiveArgument();

        void setUpToolbar();

        void setUpList(List<BankPojo> bankList);

        void setStatusBarColor();

        void setListener(BankChooserContract.Listener listener);
    }

    interface Listener {

        void setUpLayout();
    }
}
