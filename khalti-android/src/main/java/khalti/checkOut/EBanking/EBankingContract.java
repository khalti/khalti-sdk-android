package khalti.checkOut.EBanking;

import java.util.HashMap;
import java.util.List;

import khalti.checkOut.EBanking.helper.BankPojo;
import khalti.checkOut.EBanking.helper.BankingData;
import rx.Observable;

public interface EBankingContract {
    interface View {

        void toggleIndented(boolean show);

        void setUpList(List<BankPojo> bankList);

        void showIndentedNetworkError();

        void showIndentedError(String error);

        void openMobileForm(BankingData bankingData);

        Observable<HashMap<String, String>> getItemClickObservable();

        HashMap<String, Observable<Void>> setOnClickListener();

        Observable<CharSequence> setSearchListener();

        Observable<Integer> filterList(String text);

        void toggleSearch(boolean show);

        void toggleSearchError(boolean show);

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
