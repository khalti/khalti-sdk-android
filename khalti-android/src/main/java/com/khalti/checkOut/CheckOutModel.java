package com.khalti.checkOut;

import com.khalti.checkOut.api.ApiHelper;
import com.khalti.checkOut.api.KhaltiApi;
import com.khalti.checkOut.helper.MerchantPreferencePojo;
import com.khalti.utils.EmptyUtil;
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
    public Observable<MerchantPreferencePojo> fetchPreference(String key) {
        PublishSubject<MerchantPreferencePojo> prefObservable = PublishSubject.create();
        compositeSubscription.add(apiHelper.callApi(khaltiService.getPreference("/api/merchant/preference/", "Key " + key))
                .map(o -> (MerchantPreferencePojo) o)
                .subscribe(new Subscriber<MerchantPreferencePojo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        prefObservable.onError(e);
                    }

                    @Override
                    public void onNext(MerchantPreferencePojo merchantPreferencePojo) {
                        prefObservable.onNext(merchantPreferencePojo);
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
