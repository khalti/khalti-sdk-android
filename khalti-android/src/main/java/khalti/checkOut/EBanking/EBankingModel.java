package khalti.checkOut.EBanking;


import java.util.List;

import khalti.checkOut.EBanking.helper.BankPojo;
import khalti.checkOut.EBanking.helper.BaseListPojo;
import khalti.checkOut.api.ApiHelper;
import khalti.checkOut.api.KhaltiApi;
import khalti.utils.EmptyUtil;
import rx.Observable;
import rx.Subscriber;
import rx.subjects.PublishSubject;
import rx.subscriptions.CompositeSubscription;

class EBankingModel implements EBankingContract.Model {
    private KhaltiApi khaltiService;
    private CompositeSubscription compositeSubscription;

    EBankingModel() {
        khaltiService = ApiHelper.apiBuilder();
        compositeSubscription = new CompositeSubscription();
    }

    public EBankingModel(KhaltiApi mockedKhaltiService) {
        khaltiService = mockedKhaltiService;
        compositeSubscription = new CompositeSubscription();
    }

    @Override
    public Observable<List<BankPojo>> fetchBankList() {
        PublishSubject<List<BankPojo>> bankObservable = PublishSubject.create();
        compositeSubscription.add(new ApiHelper().callApiAlt(khaltiService.getBanks("api/bank/", 1, 100, true))
                .map(o -> ((BaseListPojo) o).getRecords())
                .subscribe(new Subscriber<List<BankPojo>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        bankObservable.onError(e);
                    }

                    @Override
                    public void onNext(List<BankPojo> o) {
                        bankObservable.onNext(o);
                    }
                }));
        return bankObservable;
    }

    @Override
    public void unSubscribe() {
        if (EmptyUtil.isNotNull(compositeSubscription) && compositeSubscription.hasSubscriptions() && !compositeSubscription.isUnsubscribed()) {
            compositeSubscription.unsubscribe();
        }
    }
}
