package com.khalti.form.EBanking;


import com.khalti.form.ApiHelper;
import com.khalti.form.EBanking.chooseBank.BankPojo;
import com.khalti.form.api.KhaltiApi;
import com.utila.ApiUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Response;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class EBankingModel {
    private KhaltiApi khaltiService;
    private CompositeSubscription compositeSubscription;
    private int HTTP_STATUS_CODE;
    private String HTTP_ERROR;

    private List<BankPojo> bankList;

    public EBankingModel() {
        compositeSubscription = new CompositeSubscription();
        khaltiService = ApiHelper.apiBuilder();
    }

    public EBankingModel(KhaltiApi mockedKhaltiService) {
        compositeSubscription = new CompositeSubscription();
        khaltiService = mockedKhaltiService;
    }

    void fetchBankList(BankAction bankAction) {
        Subscription subscription = khaltiService.getBanks("api/bank/", 1, 100, true)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<BaseListPojo>>() {
                               @Override
                               public void onCompleted() {
                                   if (ApiUtil.isSuccessFul(HTTP_STATUS_CODE)) {
                                       if (bankList.size() > 1) {
                                           bankAction.onCompleted(bankList);
                                       } else {
                                           bankAction.onCompleted(getSimpleBankList(bankList));
                                       }
                                   } else {
                                       bankAction.onError(HTTP_ERROR);
                                   }
                               }

                               @Override
                               public void onError(Throwable e) {
                                   e.printStackTrace();
                                   bankAction.onError(e.getMessage());
                               }

                               @Override
                               public void onNext(Response<BaseListPojo> response) {
                                   HTTP_STATUS_CODE = response.code();
                                   if (response.isSuccessful()) {
                                       bankList = response.body().getRecords();
                                   } else {
                                       try {
                                           HTTP_ERROR = new String(response.errorBody().bytes());
                                       } catch (IOException e) {
                                           e.printStackTrace();
                                       }
                                   }
                               }
                           }
                );

        compositeSubscription.add(subscription);
    }

    public interface BankAction {
        void onCompleted(Object bankList);

        void onError(String message);
    }

    private HashMap<?, ?> getSimpleBankList(List<BankPojo> banks) {
        HashMap<String, List<String>> map = new HashMap<>();
        List<String> names = new ArrayList<>();
        List<String> idxes = new ArrayList<>();

        for (BankPojo bank : banks) {
            names.add(bank.getName());
            idxes.add(bank.getIdx());
        }

        map.put("name", names);
        map.put("idx", idxes);

        return map;
    }
}
