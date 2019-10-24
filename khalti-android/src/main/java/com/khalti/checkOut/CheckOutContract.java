package com.khalti.checkOut;

import java.util.List;

import com.khalti.checkOut.helper.MerchantPreferencePojo;
import com.khalti.checkOut.helper.PaymentPreference;

import rx.Observable;

interface CheckOutContract {
    interface View {

        void toggleIndented(boolean show);

        void showIndentedNetworkError();

        void showIndentedError(String error);

        Observable<Void> setTryAgainClickListener();

        void setupViewPager(List<PaymentPreference> types);

        void setUpTabLayout(List<PaymentPreference> types);

        void setTabListener();

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
