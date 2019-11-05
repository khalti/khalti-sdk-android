package com.khalti.checkOut.ebanking;

import com.khalti.checkOut.ebanking.helper.BankPojo;
import com.khalti.checkOut.ebanking.helper.BankingData;

import java.util.HashMap;
import java.util.List;

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

        boolean hasNetwork();

        void setPresenter(Presenter presenter);
    }

    interface Presenter {

        void onCreate();

        void onDestroy();
    }

    interface Model {

        Observable<List<BankPojo>> fetchBankList();

        void unSubscribe();
    }
}
