package com.khalti.checkOut.Wallet;


import java.util.HashMap;

import com.khalti.checkOut.Wallet.helper.WalletConfirmPojo;
import com.khalti.checkOut.Wallet.helper.WalletInitPojo;
import com.khalti.checkOut.api.ApiHelper;
import com.khalti.checkOut.api.Config;
import com.khalti.checkOut.api.KhaltiApi;
import com.khalti.utils.EmptyUtil;
import com.khalti.utils.Store;
import rx.Observable;
import rx.Subscriber;
import rx.subjects.PublishSubject;
import rx.subscriptions.CompositeSubscription;

public class WalletModel implements WalletContract.Model {
    private KhaltiApi khaltiService;
    private ApiHelper apiHelper;
    private WalletInitPojo walletInit;
    private CompositeSubscription compositeSubscription;

    WalletModel() {
        khaltiService = ApiHelper.apiBuilder();
        apiHelper = new ApiHelper();
        compositeSubscription = new CompositeSubscription();
    }

    WalletModel(KhaltiApi mockedKhaltiService) {
        khaltiService = mockedKhaltiService;
        compositeSubscription = new CompositeSubscription();
    }

    @Override
    public Observable<WalletInitPojo> initiatePayment(String mobile, Config config) {
        PublishSubject<WalletInitPojo> initObservable = PublishSubject.create();

        HashMap<String, Object> dataMap = new HashMap<>();
        dataMap.put("public_key", config.getPublicKey());
        dataMap.put("return_url", "http://a.khalti.com/client/spec/widget/verify.html");
        dataMap.put("product_identity", config.getProductId());
        dataMap.put("product_name", config.getProductName());
        if (EmptyUtil.isNotNull(config.getProductUrl()) && EmptyUtil.isNotEmpty(config.getProductUrl())) {
            dataMap.put("product_url", config.getProductUrl());
        }
        dataMap.put("amount", config.getAmount());
        dataMap.put("mobile", mobile);
        dataMap.putAll((EmptyUtil.isNotNull(config.getAdditionalData()) && EmptyUtil.isNotEmpty(config.getAdditionalData())) ? config.getAdditionalData() : new HashMap<>());

        compositeSubscription.add(apiHelper.callApi(khaltiService.initiatePayment("/api/payment/initiate/", dataMap))
                .map(o -> (WalletInitPojo) o)
                .subscribe(new Subscriber<WalletInitPojo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        initObservable.onError(e);
                    }

                    @Override
                    public void onNext(WalletInitPojo walletInitPojo) {
                        walletInit = walletInitPojo;
                        initObservable.onNext(walletInitPojo);
                    }
                }));

        return initObservable;
    }

    @Override
    public Observable<WalletConfirmPojo> confirmPayment(String confirmationCode, String transactionPIN) {
        PublishSubject<WalletConfirmPojo> confirmObservable = PublishSubject.create();

        HashMap<String, Object> dataMap = new HashMap<>();
        dataMap.put("token", walletInit.getToken());
        dataMap.put("confirmation_code", confirmationCode);
        dataMap.put("transaction_pin", transactionPIN);
        dataMap.put("public_key", Store.getConfig().getPublicKey());

        compositeSubscription.add(apiHelper.callApi(khaltiService.confirmPayment("api/payment/confirm/", dataMap))
                .map(o -> (WalletConfirmPojo) o)
                .subscribe(new Subscriber<WalletConfirmPojo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        confirmObservable.onError(e);
                    }

                    @Override
                    public void onNext(WalletConfirmPojo walletConfirmPojo) {
                        confirmObservable.onNext(walletConfirmPojo);
                    }
                }));

        return confirmObservable;
    }

    @Override
    public void unSubscribe() {
        if (EmptyUtil.isNotNull(compositeSubscription) && compositeSubscription.hasSubscriptions() && !compositeSubscription.isUnsubscribed()) {
            compositeSubscription.unsubscribe();
        }
    }

    public interface WalletAction {
        void onCompleted(Object o);

        void onError(String message);
    }
}
