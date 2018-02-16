package khalti.checkOut;

import java.util.List;

import khalti.checkOut.helper.MerchantPreferencePojo;
import rx.Observable;

interface CheckOutContract {
    interface View {

        void toggleIndented(boolean show);

        void showIndentedNetworkError();

        void showIndentedError(String error);

        Observable<Void> setTryAgainClickListener();

        void setupViewPager(List<String> types);

        void setUpTabLayout(List<String> types);

        void setTabListener();

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

        Observable<MerchantPreferencePojo> fetchPreference(String key);

        void unSubscribe();
    }
}
