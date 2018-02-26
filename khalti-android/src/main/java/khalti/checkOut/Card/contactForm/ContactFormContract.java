package khalti.checkOut.Card.contactForm;

import khalti.checkOut.EBanking.helper.BankingData;
import khalti.checkOut.api.Config;
import rx.Observable;

interface ContactFormContract {

    interface View {

        BankingData receiveData();

        void setBankData(String logo, String name, String icon);

        void setButtonText(String text);

        void setEditTextError(String error);

        void showMessageDialog(String title, String message);

        void showError(String message);

        void showNetworkError();

        void openEBanking(String url);

        void saveConfigInFile(Config config);

        String getPackageName();

        String getContactNumber();

        Observable<Void> setClickListener();

        Observable<CharSequence> setEditTextListener();

        boolean isNetworkAvailable();

        void setPresenter(Presenter presenter);
    }

    interface Presenter {

        void onCreate();

        void onDestroy();

        void onFormSubmitted(boolean isNetwork, String mobile, String bankId, String bankName, Config config);
    }
}
