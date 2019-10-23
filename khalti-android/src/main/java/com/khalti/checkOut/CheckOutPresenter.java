package com.khalti.checkOut;

import androidx.annotation.NonNull;

import com.khalti.checkOut.helper.CheckoutEventListener;
import com.khalti.checkOut.helper.MerchantPreferencePojo;
import com.khalti.utils.GuavaUtil;
import com.khalti.utils.LogUtil;
import com.khalti.utils.MerchantUtil;
import com.khalti.utils.Store;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import rx.Subscriber;
import rx.subscriptions.CompositeSubscription;

class CheckOutPresenter implements CheckOutContract.Presenter {
    @NonNull
    private final CheckOutContract.View view;
    private CheckOutModel model;
    private CompositeSubscription compositeSubscription;

    CheckOutPresenter(@NonNull CheckOutContract.View view) {
        this.view = GuavaUtil.checkNotNull(view);
        view.setPresenter(this);
        model = new CheckOutModel();
        compositeSubscription = new CompositeSubscription();
    }

    @Override
    public void onCreate() {
        view.setStatusBarColor();
       /* compositeSubscription.add(RxBus.getInstance().register(Event.class, event -> {
            if (event.getTag().equals("close_check_out")) {
                view.closeCheckOut();
            }
        }));*/

        CheckoutEventListener checkoutEventListener = view::closeCheckOut;
        Store.setCheckoutEventListener(checkoutEventListener);

        compositeSubscription.add(view.setTryAgainClickListener().subscribe(aVoid -> fetchPreference(Store.getConfig().getPublicKey())));
        view.toggleIndented(false);
        if (view.hasNetwork()) {
            fetchPreference(Store.getConfig().getPublicKey());
        } else {
            view.showIndentedNetworkError();
        }
    }

    @Override
    public void onDestroy() {
        view.dismissAllDialogs();
    }

    @Override
    public void onTabSelected(int position, boolean selected) {
        view.toggleTab(position, selected);
    }

    @Override
    public void fetchPreference(String key) {
        view.toggleIndented(true);
        compositeSubscription.add(model.fetchPreference(key)
                .subscribe(new Subscriber<MerchantPreferencePojo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showIndentedError(e.getMessage());
                    }

                    @Override
                    public void onNext(MerchantPreferencePojo preference) {
                        view.toggleIndented(false);
                        List<String> types = preference.getSdkPosition();

                        List<String> uniqueList = new ArrayList<>(new LinkedHashSet<>(types));
                        if (uniqueList.size() == 0) {
                            uniqueList.add(MerchantUtil.EBANKING);
                            uniqueList.add(MerchantUtil.WALLET);
                            uniqueList.add(MerchantUtil.CARD);
                        }

                        LogUtil.log("unique list size", uniqueList.size());
                        view.setupViewPager(uniqueList);
                        view.setUpTabLayout(uniqueList);
                        view.setTabListener();
                    }
                }));

    }
}
