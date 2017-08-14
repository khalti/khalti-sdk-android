package com.khalti.form.Wallet;


import com.khalti.form.ApiHelper;
import com.khalti.form.api.Config;
import com.khalti.form.api.KhaltiApi;
import com.utila.EmptyUtil;

import java.util.HashMap;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

class WalletModel {
    private KhaltiApi khaltiService;
    private CompositeSubscription compositeSubscription;
    private int HTTP_STATUS_CODE;
    private String HTTP_ERROR;

    WalletModel() {
        compositeSubscription = new CompositeSubscription();
        khaltiService = ApiHelper.apiBuilder();
    }

    WalletModel(KhaltiApi mockedKhaltiService) {
        compositeSubscription = new CompositeSubscription();
        khaltiService = mockedKhaltiService;
    }

    void initiatePayment(String mobile, Config config) {
        HashMap<String, Object> dataMap = new HashMap<>();
        dataMap.put("public_key", config.getPublicKey());
        dataMap.put("return_url", "http://a.khalti.com/client/spec/widget/verify.html");
        dataMap.put("product_identity", config.getProductId());
        dataMap.put("product_name", config.getProductName());
        dataMap.put("product_url", config.getProductUrl());
        dataMap.put("amount", config.getAmount());
        dataMap.put("mobile", mobile);
        dataMap.putAll(EmptyUtil.isNotNull(config.getAdditionalData()) ? config.getAdditionalData() : new HashMap<>());

        String url = "/api/payment/initiate";

        compositeSubscription.add(khaltiService.initiatePayment(url, dataMap)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Object o) {

                    }
                }));
    }


    interface WalletAction {
        void onCompleted();

        void onError(String message);
    }
}
