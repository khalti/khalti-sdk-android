package khalti.checkOut.Wallet;


import java.util.HashMap;

import khalti.checkOut.api.ApiHelper;
import khalti.checkOut.api.Config;
import khalti.checkOut.api.KhaltiApi;
import khalti.utils.Store;
import khalti.utils.EmptyUtil;
import rx.Subscription;

public class WalletModel {
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

    public Subscription initiatePayment(String mobile, Config config, WalletAction walletAction) {
        HashMap<String, Object> dataMap = new HashMap<>();
        dataMap.put("public_key", config.getPublicKey());
        dataMap.put("return_url", "http://a.khalti.com/client/spec/widget/verify.html");
        dataMap.put("product_identity", config.getProductId());
        dataMap.put("product_name", config.getProductName());
        dataMap.put("product_url", config.getProductUrl());
        dataMap.put("amount", config.getAmount());
        dataMap.put("mobile", mobile);
        dataMap.putAll((EmptyUtil.isNotNull(config.getAdditionalData()) && EmptyUtil.isNotEmpty(config.getAdditionalData())) ? config.getAdditionalData() : new HashMap<>());

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

    public Subscription confirmPayment(String confirmationCode, String transactionPIN, WalletAction walletAction) {
        HashMap<String, Object> dataMap = new HashMap<>();
        dataMap.put("token", walletInitPojo.getToken());
        dataMap.put("confirmation_code", confirmationCode);
        dataMap.put("transaction_pin", transactionPIN);
        dataMap.put("public_key", Store.getConfig().getPublicKey());

        String url = "api/payment/confirm/";

        return apiHelper.callApi(khaltiService.confirmPayment(url, dataMap), new ApiHelper.ApiCallback() {
            @Override
            public void onComplete() {
                walletAction.onCompleted(walletConfirmPojo);
            }

            @Override
            public void onError(String errorMessage) {
                walletAction.onError(errorMessage);
            }

            @Override
            public void onNext(Object o) {
                walletConfirmPojo = (WalletConfirmPojo) o;
            }
        });
    }

   public interface WalletAction {
        void onCompleted(Object o);

        void onError(String message);
    }
}
