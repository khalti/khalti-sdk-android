package khalti.checkOut.Card;

import java.util.HashMap;
import java.util.List;

import khalti.checkOut.EBanking.helper.BankPojo;
import khalti.checkOut.api.Config;
import rx.Observable;

public interface CardContract {

    interface View {

        void toggleProgressBar(boolean show);

        void toggleButton(boolean enabled);

        void showCardFields();

        void setBankItem(String logo, String name, String shortName, String bankId);

        void setButtonText(String text);

        Observable<CharSequence> setEditTextListener();

        void setErrorAnimation();

        void setMobileError(String error);

        void showNetworkError();

        void showError(String message);

        void showMessageDialog(String title, String message);

        void openBankList(HashMap<String, Object> dataMap);

        void openCardBanking(String url);

        void saveConfigInFile(String fileName, Config config);

        String getPackageName();

        boolean hasNetwork();

        HashMap<String, Observable<Void>> setClickListeners();

        void setPresenter(Presenter presenter);
    }

    interface Presenter {

        void onCreate();

        void onDestroy();

        void onFormSubmitted(boolean isNetwork, String mobile, String bankId, String bankName, Config config);
    }

    interface Model {

        Observable<List<BankPojo>> fetchBankList();

        void unSubscribe();
    }
}
