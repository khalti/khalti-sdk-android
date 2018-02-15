package khalti.checkOut;

import java.util.HashMap;

import khalti.checkOut.api.ApiHelper;
import khalti.checkOut.api.KhaltiApi;
import khalti.checkOut.helper.PaymentPreferencePojo;
import khalti.utils.EmptyUtil;
import rx.Observable;
import rx.Subscriber;
import rx.subjects.PublishSubject;
import rx.subscriptions.CompositeSubscription;

public class CheckOutModel implements CheckOutContract.Model {
    private KhaltiApi khaltiService;
    private ApiHelper apiHelper;
    private CompositeSubscription compositeSubscription;

    public CheckOutModel() {
        khaltiService = ApiHelper.apiBuilder();
        apiHelper = new ApiHelper();
        compositeSubscription = new CompositeSubscription();
    }

    @Override
    public Observable<PaymentPreferencePojo> fetchPreference(String key) {
        PublishSubject<PaymentPreferencePojo> prefObservable = PublishSubject.create();

        HashMap<String, Object> dataMap = new HashMap<>();
        dataMap.put("public_key", key);

        compositeSubscription.add(apiHelper.callApi(khaltiService.getPreference("/api/payment/preference/", dataMap))
                .map(o -> (PaymentPreferencePojo) o)
                .subscribe(new Subscriber<PaymentPreferencePojo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        prefObservable.onError(e);
                    }

                    @Override
                    public void onNext(PaymentPreferencePojo paymentPreferencePojo) {
                        prefObservable.onNext(paymentPreferencePojo);
                    }
                }));

        return prefObservable;
    }

    @Override
    public void unSubscribe() {
        if (EmptyUtil.isNotNull(compositeSubscription) && compositeSubscription.hasSubscriptions() && !compositeSubscription.isUnsubscribed()) {
            compositeSubscription.unsubscribe();
        }
    }
}
