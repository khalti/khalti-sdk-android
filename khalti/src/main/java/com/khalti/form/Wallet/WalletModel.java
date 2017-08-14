package com.khalti.form.Wallet;


import com.khalti.form.ApiHelper;
import com.khalti.form.api.KhaltiApi;

import rx.subscriptions.CompositeSubscription;

public class WalletModel {
    private KhaltiApi khaltiService;
    private CompositeSubscription compositeSubscription;
    private int HTTP_STATUS_CODE;
    private String HTTP_ERROR;

    public WalletModel() {
        compositeSubscription = new CompositeSubscription();
        khaltiService = ApiHelper.apiBuilder();
    }

    public WalletModel(KhaltiApi mockedKhaltiService) {
        compositeSubscription = new CompositeSubscription();
        khaltiService = mockedKhaltiService;
    }


    public interface WalletAction {
        void onCompleted();

        void onError(String message);
    }
}
