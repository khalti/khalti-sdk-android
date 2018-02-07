package khalti.checkOut.Card;

import android.support.annotation.NonNull;

import khalti.checkOut.api.Config;
import khalti.utils.GuavaUtil;
import rx.subscriptions.CompositeSubscription;

public class CardPresenter implements CardContract.Presenter {

    @NonNull
    private final CardContract.View view;
    private CardModel model;
    private Config config;
    private CompositeSubscription compositeSubscription;

    public CardPresenter(@NonNull CardContract.View view) {
        this.view = GuavaUtil.checkNotNull(view);
        view.setPresenter(this);
        model = new CardModel();
        compositeSubscription = new CompositeSubscription();
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onFormSubmitted(boolean isNetwork, String mobile, String bankId, String bankName, Config config) {

    }
}
