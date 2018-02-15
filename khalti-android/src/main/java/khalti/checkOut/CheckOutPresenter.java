package khalti.checkOut;

import android.support.annotation.NonNull;

import khalti.checkOut.helper.PaymentPreferencePojo;
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
                .subscribe(new Subscriber<PaymentPreferencePojo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showIndentedError(e.getMessage());
                    }

                    @Override
                    public void onNext(PaymentPreferencePojo preference) {
                        view.toggleIndented(false);
                        view.setupViewPager(true, true, true);
                        view.setUpTabLayout();
                    }
                }));
    }
}
