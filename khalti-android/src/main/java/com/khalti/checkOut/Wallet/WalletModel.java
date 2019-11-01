package com.khalti.checkOut.Wallet;


import java.util.HashMap;

import com.khalti.checkOut.Wallet.helper.WalletConfirmPojo;
import com.khalti.checkOut.Wallet.helper.WalletInitPojo;
import com.khalti.checkOut.api.ApiHelper;
import com.khalti.checkOut.api.Result;
import com.khalti.checkOut.helper.Config;
import com.khalti.checkOut.api.KhaltiApi;
import com.khalti.utils.EmptyUtil;
import com.khalti.utils.Store;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import kotlin.coroutines.Continuation;
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


    @Nullable
    @Override
    public Object initiatePayment(@NotNull String mobile, @NotNull Config config, @NotNull Continuation<? super Result<WalletInitPojo>> continuation) {
        return null;
    }

    @Nullable
    @Override
    public Object confirmPayment(@NotNull String confirmationCode, @NotNull String transactionPIN, @NotNull String token, @NotNull Continuation<? super Result<WalletConfirmPojo>> continuation) {
        return null;
    }
}