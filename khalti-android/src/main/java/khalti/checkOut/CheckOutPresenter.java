package khalti.checkOut;

import android.support.annotation.NonNull;

import java.util.List;

import khalti.checkOut.helper.MerchantPreferencePojo;
import khalti.rxBus.Event;
import khalti.rxBus.RxBus;
import khalti.utils.GuavaUtil;
import khalti.utils.Store;
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
//        view.setUpToolbar();
        compositeSubscription.add(RxBus.getInstance().register(Event.class, event -> {
            if (event.getTag().equals("close_check_out")) {
                view.closeCheckOut();
            }
        }));
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

                        view.setupViewPager(types);
                        view.setUpTabLayout(types);
                        view.setTabListener();
                    }
                }));
    }
}
