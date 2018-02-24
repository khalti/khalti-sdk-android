package khalti.checkOut.EBanking;

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

public class EBankingPresenter implements EBankingContract.Presenter {
    @NonNull
    private final EBankingContract.View view;
    private EBankingModel eBankingModel;
    private Config config;
    private CompositeSubscription compositeSubscription;

    public EBankingPresenter(@NonNull EBankingContract.View view) {
        this.view = GuavaUtil.checkNotNull(view);
        view.setPresenter(this);
        eBankingModel = new EBankingModel();
        compositeSubscription = new CompositeSubscription();
    }

    @Override
    public void onCreate(boolean hasNetwork) {
        this.config = Store.getConfig();
        view.toggleIndented(true);
        HashMap<String, Observable<Void>> map = view.setOnClickListener();
        compositeSubscription.add(map.get("try_again").subscribe(aVoid -> onCreate(hasNetwork)));
        if (hasNetwork) {
            compositeSubscription.add(eBankingModel.fetchBankList()
                    .subscribe(new Subscriber<List<BankPojo>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            view.showIndentedError(e.getMessage());
                            config.getOnCheckOutListener().onError(ErrorAction.FETCH_BANK_LIST.getAction(), e.getMessage());
                        }

                        @Override
                        public void onNext(List<BankPojo> banks) {
                            view.toggleIndented(false);
                            view.setUpList(banks);
                            view.toggleSearch(banks.size() > 3);
                            compositeSubscription.add(view.getItemClickObservable()
                                    .subscribe(hashMap -> view.openMobileForm(new BankingData(hashMap.get("idx"), hashMap.get("name"), hashMap.get("logo"),
                                            hashMap.get("icon"), config))));

                            compositeSubscription.add(view.setSearchListener()
                                    .debounce(500, TimeUnit.MILLISECONDS)
                                    .subscribe(charSequence -> compositeSubscription.add(view.filterList(charSequence + "")
                                            .subscribe(integer -> {
                                                if (EmptyUtil.isNotNull(integer)) {
                                                    view.toggleSearchError(integer == 0);
                                                }
                                            }))));
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
        eBankingModel.unSubscribe();
    }

    public void injectModel(EBankingModel eBankingModel) {
        this.eBankingModel = eBankingModel;
    }

    public void injectConfig(Config config) {
        this.config = config;
    }
}
