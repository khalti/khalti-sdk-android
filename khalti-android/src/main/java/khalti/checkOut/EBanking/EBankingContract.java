package khalti.checkOut.EBanking;

import java.util.HashMap;
import java.util.List;

import khalti.checkOut.EBanking.helper.BankPojo;
import khalti.checkOut.EBanking.helper.EBankingData;
import rx.Observable;

public interface EBankingContract {
    interface View {

        void toggleIndented(boolean show);

        void setUpList(List<BankPojo> bankList);

        void showIndentedNetworkError();

        void showIndentedError(String error);

        void openMobileForm(EBankingData eBankingData);

        Observable<HashMap<String, String>> getItemClickObservable();

        HashMap<String, Observable<Void>> setOnClickListener();

        void toggleSearch(boolean show);

        void setPresenter(Presenter presenter);
    }

    interface Presenter {

        void onCreate(boolean hasNetwork);

        void onDestroy();
    }

    interface Model {

        Observable<List<BankPojo>> fetchBankList();

        void unSubscribe();
    }
}
