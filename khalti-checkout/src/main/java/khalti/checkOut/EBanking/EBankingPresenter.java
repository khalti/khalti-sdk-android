package khalti.checkOut.EBanking;

import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.List;

import khalti.checkOut.EBanking.chooseBank.BankPojo;
import khalti.checkOut.api.ApiHelper;
import khalti.checkOut.api.Config;
import khalti.checkOut.api.ErrorAction;
import khalti.utils.ApiUtil;
import khalti.utils.EmptyUtil;
import khalti.utils.GuavaUtil;
import khalti.utils.NumberUtil;
import khalti.utils.Store;
import khalti.utils.StringUtil;
import khalti.utils.ValidationUtil;
import rx.subscriptions.CompositeSubscription;

public class EBankingPresenter implements EBankingContract.Listener {
    @NonNull
    private final EBankingContract.View mEBankingView;
    private EBankingModel eBankingModel;
    private List<BankPojo> bankLists;
    private Config config;
    private CompositeSubscription compositeSubscription;

    public EBankingPresenter(@NonNull EBankingContract.View mEBankingView) {
        this.mEBankingView = GuavaUtil.checkNotNull(mEBankingView);
        mEBankingView.setListener(this);
        eBankingModel = new EBankingModel();
    }

    @Override
    public void setUpLayout(boolean hasNetwork) {
        this.config = Store.getConfig();
        mEBankingView.toggleButton(false);
        mEBankingView.showBankField();
        mEBankingView.setButtonText("Pay Rs " + StringUtil.formatNumber(NumberUtil.convertToRupees(config.getAmount())));
        if (hasNetwork) {
            mEBankingView.toggleProgressBar(true);
            compositeSubscription = new CompositeSubscription();
            compositeSubscription.add(eBankingModel.fetchBankList(new EBankingModel.BankAction() {
                @Override

                public void onCompleted(Object bankList) {
                    mEBankingView.toggleButton(true);
                    mEBankingView.toggleProgressBar(false);
                    if (bankList instanceof HashMap) {
                        HashMap<?, ?> map = (HashMap<?, ?>) bankList;
                        mEBankingView.setUpSpinner(map.get("name"), map.get("idx"));
                    } else {
                        List<BankPojo> banks = (List<BankPojo>) bankList;
                        bankLists = banks;
                        mEBankingView.setUpBankItem(banks.get(0).getName(), banks.get(0).getIdx());
                    }
                }

                @Override
                public void onError(String message) {
                    mEBankingView.toggleProgressBar(false);
                    mEBankingView.showError(message);
                    config.getOnCheckOutListener().onError(ErrorAction.FETCH_BANK_LIST.getAction(), message);
                }
            }));
        } else {
            mEBankingView.showNetworkError();
        }
    }

    @Override
    public void toggleEditTextListener(boolean set) {
        mEBankingView.toggleEditTextListener(set);
    }

    @Override
    public void setErrorAnimation() {
        mEBankingView.setErrorAnimation();
    }

    @Override
    public void openBankList() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("banks", bankLists);
        mEBankingView.openBankList(map);
    }

    @Override
    public void updateBankItem(String bankName, String bankId) {
        mEBankingView.setUpBankItem(bankName, bankId);
    }

    @Override
    public void initiatePayment(boolean isNetwork, String mobile, String bankId, String bankName) {
        if (EmptyUtil.isNotEmpty(mobile) && ValidationUtil.isMobileNumberValid(mobile)) {
            if (isNetwork) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("mobile", mobile);
                map.put("bankId", bankId);
                map.put("bankName", bankName);

                String data = "public_key=" + config.getPublicKey() + "&" +
                        "product_identity=" + config.getProductId() + "&" +
                        "product_name=" + config.getProductName() + "&" +
                        "amount=" + config.getAmount() + "&" +
                        "mobile=" + map.get("mobile") + "&" +
                        "bank=" + map.get("bankId") + "&" +
                        "source=android" + "&" +
                        "product_url=" + config.getProductUrl() +
                        ApiUtil.getPostData(config.getAdditionalData());

                mEBankingView.saveConfigInFile("khalti_config", config);
                mEBankingView.openEBanking(ApiHelper.getUrl() + "ebanking/initiate/?" + data);
            } else {
                mEBankingView.showNetworkError();
            }
        } else {
            if (EmptyUtil.isEmpty(mobile)) {
                mEBankingView.setMobileError("This field is required");
            } else {
                mEBankingView.setMobileError("Invalid mobile number");
            }
        }
    }

    @Override
    public void unSubscribe() {
        if (compositeSubscription.hasSubscriptions() && !compositeSubscription.isUnsubscribed()) {
            compositeSubscription.unsubscribe();
        }
    }

    public void injectModel(EBankingModel eBankingModel) {
        this.eBankingModel = eBankingModel;
    }

    public void injectConfig(Config config) {
        this.config = config;
    }
}
