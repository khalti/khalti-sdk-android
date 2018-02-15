package khalti.checkOut;

import khalti.checkOut.helper.PaymentPreferencePojo;
import rx.Observable;

interface CheckOutContract {
    interface View {

        void toggleIndented(boolean show);

        void showIndentedNetworkError();

        void showIndentedError(String error);

        Observable<Void> setTryAgainClickListener();

        void setupViewPager(boolean eBanking, boolean wallet, boolean card);

        void setUpTabLayout();

        void setUpToolbar();

        void toggleTab(int position, boolean selected);

        void setStatusBarColor();

        void dismissAllDialogs();

        boolean hasNetwork();

        void closeCheckOut();

        void setPresenter(Presenter presenter);
    }

    interface Presenter {

        void onCreate();

        void onDestroy();

        void onTabSelected(int position, boolean selected);

        void fetchPreference(String key);
    }

    interface Model {

        Observable<PaymentPreferencePojo> fetchPreference(String key);

        void unSubscribe();
    }
}
