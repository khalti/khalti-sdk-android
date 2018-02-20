package khalti.checkOut.EBanking.contactForm;

import android.support.annotation.NonNull;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

import khalti.checkOut.EBanking.helper.BankingData;
import khalti.checkOut.api.Config;
import khalti.utils.ApiUtil;
import khalti.utils.Constant;
import khalti.utils.EmptyUtil;
import khalti.utils.GuavaUtil;
import khalti.utils.NumberUtil;
import khalti.utils.StringUtil;
import khalti.utils.ValidationUtil;
import rx.subscriptions.CompositeSubscription;

class ContactFormPresenter implements ContactFormContract.Presenter {
    @NonNull
    private final ContactFormContract.View view;
    private CompositeSubscription compositeSubscription;

    ContactFormPresenter(@NonNull ContactFormContract.View view) {
        this.view = GuavaUtil.checkNotNull(view);
        view.setPresenter(this);
        compositeSubscription = new CompositeSubscription();
    }

    @Override
    public void onCreate() {
        BankingData bankingData = view.receiveData();
        if (EmptyUtil.isNotNull(bankingData)) {
            view.setBankData(bankingData.getBankLogo(), bankingData.getBankName(), bankingData.getBankIcon());
            view.setButtonText("Pay Rs " + StringUtil.formatNumber(NumberUtil.convertToRupees(bankingData.getConfig().getAmount())));
            compositeSubscription.add(view.setClickListener().subscribe(aVoid -> onFormSubmitted(view.isNetworkAvailable(), view.getContactNumber(), bankingData.getBankIdx(),
                    bankingData.getBankName(), bankingData.getConfig())));
            compositeSubscription.add(view.setEditTextListener().subscribe(charSequence -> view.setEditTextError(null)));
        }
    }

    @Override
    public void onDestroy() {
        if (EmptyUtil.isNotNull(compositeSubscription) && compositeSubscription.hasSubscriptions() && !compositeSubscription.isUnsubscribed()) {
            compositeSubscription.unsubscribe();
        }
    }

    @Override
    public void onFormSubmitted(boolean isNetwork, String mobile, String bankId, String bankName, Config config) {
        if (EmptyUtil.isNotEmpty(mobile) && ValidationUtil.isMobileNumberValid(mobile)) {
            if (isNetwork) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("mobile", mobile);
                map.put("bankId", bankId);
                map.put("bankName", bankName);

                try {
                    String data = "public_key=" + URLEncoder.encode(config.getPublicKey(), "UTF-8") + "&" +
                            "product_identity=" + URLEncoder.encode(config.getProductId(), "UTF-8") + "&" +
                            "product_name=" + URLEncoder.encode(config.getProductName(), "UTF-8") + "&" +
                            "amount=" + URLEncoder.encode(config.getAmount() + "", "UTF-8") + "&" +
                            "mobile=" + URLEncoder.encode(map.get("mobile") + "", "UTF-8") + "&" +
                            "bank=" + URLEncoder.encode(map.get("bankId") + "", "UTF-8") + "&" +
                            "source=android" + "&" +
                            "return_url=" + URLEncoder.encode(view.getPackageName(), "UTF-8");

                    if (EmptyUtil.isNotNull(config.getProductUrl()) && EmptyUtil.isNotEmpty(config.getProductUrl())) {
                        data += "&" + "product_url=" + URLEncoder.encode(config.getProductUrl(), "UTF-8");
                    }

                    data += ApiUtil.getPostData(config.getAdditionalData());

                    view.saveConfigInFile(config);

                    view.openEBanking(Constant.url + "ebanking/initiate/?" + data);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    view.showMessageDialog("Error", "Something went wrong");
                }
            } else {
                view.showNetworkError();
            }
        } else {
            if (EmptyUtil.isEmpty(mobile)) {
                view.setEditTextError("This field is required");
            } else {
                view.setEditTextError("Enter a valid mobile number");
            }
        }
    }
}
