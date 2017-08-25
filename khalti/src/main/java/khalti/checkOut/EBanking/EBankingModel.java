package khalti.checkOut.EBanking;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import khalti.checkOut.api.ApiHelper;
import khalti.checkOut.EBanking.chooseBank.BankPojo;
import khalti.checkOut.api.KhaltiApi;
import rx.Subscription;
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
        String url = "api/bank/";
        Subscription subscription = new ApiHelper().callApi(khaltiService.getBanks(url, 1, 100, true), new ApiHelper.ApiCallback() {
            @Override
            public void onComplete() {
                if (bankList.size() > 5) {
                    bankAction.onCompleted(bankList);
                } else {
                    bankAction.onCompleted(getSimpleBankList(bankList));
                }
            }

            @Override
            public void onError(String errorMessage) {
                bankAction.onError(errorMessage);
            }

            @Override
            public void onNext(Object o) {
                bankList = ((BaseListPojo) o).getRecords();
            }
        });

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
