package khalti.form.Wallet;


import khalti.form.ApiHelper;
import khalti.form.api.Config;
import khalti.form.api.KhaltiApi;
import khalti.utils.DataHolder;
import com.utila.EmptyUtil;

import java.util.HashMap;

import rx.Subscription;

class WalletModel {
    private KhaltiApi khaltiService;
    private ApiHelper apiHelper;
    private WalletInitPojo walletInitPojo;
    private WalletConfirmPojo walletConfirmPojo;

    WalletModel() {
        khaltiService = ApiHelper.apiBuilder();
        apiHelper = new ApiHelper();
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
        return new ApiHelper().callApi(khaltiService.initiatePayment(url, dataMap), new ApiHelper.ApiCallback() {
            @Override
            public void onComplete() {
                walletAction.onCompleted(null);
            }

            @Override
            public void onError(String errorMessage) {
                walletAction.onError(errorMessage);
            }

            @Override
            public void onNext(Object o) {
                walletInitPojo = (WalletInitPojo) o;
            }
        });
    }

    Subscription confirmPayment(String confirmationCode, String transactionPIN, WalletAction walletAction) {
        HashMap<String, Object> dataMap = new HashMap<>();
        dataMap.put("token", walletInitPojo.getToken());
        dataMap.put("confirmation_code", confirmationCode);
        dataMap.put("transaction_pin", transactionPIN);
        dataMap.put("public_key", DataHolder.getConfig().getPublicKey());

        String url = "api/payment/confirm/";

        return apiHelper.callApi(khaltiService.confirmPayment(url, dataMap), new ApiHelper.ApiCallback() {
            @Override
            public void onComplete() {
                walletAction.onCompleted(walletInitPojo);
            }

            @Override
            public void onError(String errorMessage) {
                walletAction.onError(errorMessage);
            }

            @Override
            public void onNext(Object o) {
            }
        });
    }

    interface WalletAction {
        void onCompleted(Object o);

        void onError(String message);
    }
}
