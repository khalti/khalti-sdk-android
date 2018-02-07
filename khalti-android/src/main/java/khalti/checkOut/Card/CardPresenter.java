package khalti.checkOut.Card;

import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.List;

import khalti.checkOut.EBanking.helper.BankPojo;
import khalti.checkOut.api.Config;
import khalti.checkOut.api.ErrorAction;
import khalti.utils.EmptyUtil;
import khalti.utils.GuavaUtil;
import khalti.utils.Store;
import rx.Observable;
import rx.Subscriber;
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
        this.config = Store.getConfig();
        HashMap<String, Observable<Void>> map = view.setClickListeners();
        compositeSubscription.add(map.get("pay").subscribe(aVoid -> {
            //
        }));
        compositeSubscription.add(view.setEditTextListener().subscribe(charSequence -> {
            view.setMobileError(null);
        }));
        view.showCardFields();
        view.toggleButton(false);
        if (view.hasNetwork()) {
            view.toggleProgressBar(true);
            compositeSubscription.add(model.fetchBankList()
                    .subscribe(new Subscriber<List<BankPojo>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            view.showError(e.getMessage());
                            config.getOnCheckOutListener().onError(ErrorAction.FETCH_CARD_BANK_LIST.getAction(), e.getMessage());
                        }

                        @Override
                        public void onNext(List<BankPojo> banks) {
                            view.toggleProgressBar(false);
                            if (EmptyUtil.isNotEmpty(banks)) {
                                view.toggleButton(true);
                                view.setBankItem(banks.get(0).getLogo(), banks.get(0).getName(), banks.get(0).getShortName(), banks.get(0).getIdx());
                                compositeSubscription.add(map.get("open_bank_list").subscribe(aVoid -> view.openBankList(new HashMap<String, Object>() {{
                                    put("banks", banks);
                                }})));
                            }
                        }
                    }));
        } else {
            view.showNetworkError();
        }
    }

    @Override
    public void onDestroy() {
        if (EmptyUtil.isNotNull(compositeSubscription) && compositeSubscription.hasSubscriptions() && !compositeSubscription.isUnsubscribed()) {
            compositeSubscription.unsubscribe();
        }
    }

    @Override
    public void onFormSubmitted(boolean isNetwork, String mobile, String bankId, String bankName, Config config) {

    }
}
