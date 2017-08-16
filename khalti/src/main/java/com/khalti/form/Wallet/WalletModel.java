package com.khalti.form.Wallet;


import com.khalti.form.ApiHelper;
import com.khalti.form.api.Config;
import com.khalti.form.api.KhaltiApi;
import com.utila.ApiUtil;
import com.utila.EmptyUtil;

import java.io.IOException;
import java.util.HashMap;

import retrofit2.Response;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

class WalletModel {
    private KhaltiApi khaltiService;
    private int HTTP_STATUS_CODE;
    private String HTTP_ERROR;
    private WalletInitPojo walletInitPojo;

    WalletModel() {
        khaltiService = ApiHelper.apiBuilder();
    }

    WalletModel(KhaltiApi mockedKhaltiService) {
        khaltiService = mockedKhaltiService;
    }

    Subscription initiatePayment(String mobile, Config config, WalletAction walletAction) {
        HashMap<String, Object> dataMap = new HashMap<>();
        dataMap.put("public_key", config.getPublicKey());
        dataMap.put("return_url", "http://a.khalti.com/client/spec/widget/verify.html");
        dataMap.put("product_identity", config.getProductId());
        dataMap.put("product_name", config.getProductName());
        dataMap.put("product_url", config.getProductUrl());
        dataMap.put("amount", config.getAmount());
        dataMap.put("mobile", mobile);
        dataMap.putAll(EmptyUtil.isNotNull(config.getAdditionalData()) ? config.getAdditionalData() : new HashMap<>());

        String url = "/api/payment/initiate/";

        return khaltiService.initiatePayment(url, dataMap)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<WalletInitPojo>>() {
                    @Override
                    public void onCompleted() {
                        if (ApiUtil.isSuccessFul(HTTP_STATUS_CODE)) {
                            walletAction.onCompleted(walletInitPojo);
                        } else {
                            walletAction.onError(HTTP_ERROR);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (EmptyUtil.isNotNull(e)) {
                            e.printStackTrace();
                        }
                        walletAction.onError(EmptyUtil.isNotNull(e) ? e.getMessage() : "");
                    }

                    @Override
                    public void onNext(Response<WalletInitPojo> response) {
                        HTTP_STATUS_CODE = response.code();
                        if (response.isSuccessful()) {
                            walletInitPojo = response.body();
                        } else {
                            try {
                                HTTP_ERROR = new String(response.errorBody().bytes());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }

    Subscription confirmPayment(String token, String confirmationCode, String transactionPIN, String pubKey, WalletAction walletAction) {
        return null;
    }

    interface WalletAction {
        void onCompleted(WalletInitPojo walletInitPojo);

        void onError(String message);
    }
}
