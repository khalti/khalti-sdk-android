package khalti.checkOut.Card;

import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import khalti.checkOut.EBanking.helper.BankPojo;
import khalti.checkOut.EBanking.helper.BankingData;
import khalti.checkOut.api.Config;
import khalti.checkOut.api.ErrorAction;
import khalti.utils.EmptyUtil;
import khalti.utils.GuavaUtil;
import khalti.utils.Store;
import rx.Observable;
import rx.Subscriber;
import rx.subscriptions.CompositeSubscription;

class CardPresenter implements CardContract.Presenter {

    @NonNull
    private final CardContract.View view;
    private CardModel model;
    private Config config;
    private CompositeSubscription compositeSubscription;

    CardPresenter(@NonNull CardContract.View view) {
        this.view = GuavaUtil.checkNotNull(view);
        view.setPresenter(this);
        model = new CardModel();
        compositeSubscription = new CompositeSubscription();
    }

    @Override
    public void onCreate(boolean hasNetwork) {
        this.config = Store.getConfig();
        view.showBranding();
        view.toggleIndented(true);
        HashMap<String, Observable<Void>> map = view.setOnClickListener();
        compositeSubscription.add(map.get("try_again").subscribe(aVoid -> onCreate(hasNetwork)));
        compositeSubscription.add(map.get("open_search").subscribe(aVoid -> {
            view.toggleSearch(true);
            view.toggleKeyboard(true);
        }));
        compositeSubscription.add(map.get("close_search").subscribe(aVoid -> {
            view.toggleSearch(false);
            view.toggleKeyboard(false);
            view.flushList();
        }));
        if (hasNetwork) {
            compositeSubscription.add(model.fetchBankList()
                    .subscribe(new Subscriber<List<BankPojo>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            view.showIndentedError(e.getMessage());
                            config.getOnCheckOutListener().onError(ErrorAction.FETCH_CARD_BANK_LIST.getAction(), e.getMessage());
                        }

                        @Override
                        public void onNext(List<BankPojo> banks) {
                            view.toggleIndented(false);
                            view.setUpList(banks);
                            compositeSubscription.add(view.getItemClickObservable()
                                    .subscribe(hashMap -> view.openMobileForm(new BankingData(hashMap.get("idx"), hashMap.get("name"), hashMap.get("logo"),
                                            hashMap.get("icon"), config))));
                            compositeSubscription.add(view.setEditTextListener()
                                    .debounce(500, TimeUnit.MILLISECONDS)
                                    .subscribe(charSequence -> view.filterList(charSequence + "")));
                        }
                    }));
        } else {
            view.showIndentedNetworkError();
        }
    }

    @Override
    public void onDestroy() {
        if (EmptyUtil.isNotNull(compositeSubscription) && compositeSubscription.hasSubscriptions() && !compositeSubscription.isUnsubscribed()) {
            compositeSubscription.unsubscribe();
        }
        model.unSubscribe();
    }

    public void injectModel(CardModel model) {
        this.model = model;
    }

    public void injectConfig(Config config) {
        this.config = config;
    }
}
