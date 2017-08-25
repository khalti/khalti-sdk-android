package khalti.checkOut.EBanking.loadBank;

import android.support.annotation.NonNull;

import com.utila.ApiUtil;
import com.utila.EmptyUtil;
import com.utila.GuavaUtil;
import com.utila.HtmlUtil;

import java.util.HashMap;

import khalti.checkOut.api.ApiHelper;
import khalti.checkOut.api.Config;
import khalti.utils.DataHolder;

class BankPresenter implements BankContract.Listener {

    @NonNull
    private final BankContract.View mBankView;

    BankPresenter(@NonNull BankContract.View mBankView) {
        this.mBankView = GuavaUtil.checkNotNull(mBankView);
        mBankView.setListener(this);
    }

    @Override
    public void toggleIndentedProgress(boolean show, String message) {
        mBankView.toggleIndentedProgress(show, message);
    }

    @Override
    public void showIndentedError(String message) {
        mBankView.showIndentedError(message);
    }

    @Override
    public void showIndentedNetworkError() {
        mBankView.showIndentedNetworkError();
    }

    @Override
    public void setupLayout(boolean isNetwork) {
        HashMap<?, ?> map = mBankView.receiveArguments();
        if (EmptyUtil.isNotNull(map)) {
            mBankView.setUpToolbar(map.get("bankName").toString());
            Config config = DataHolder.getConfig();
            String data = "public_key=" + config.getPublicKey() + "&" +
                    "product_identity=" + config.getProductId() + "&" +
                    "product_name=" + config.getProductName() + "&" +
                    "amount=" + config.getAmount() + "&" +
                    "mobile=" + map.get("mobile") + "&" +
                    "bank=" + map.get("bankId") + "&" +
                    "via_mobile=" + true + "&" +
                    "product_url=" + config.getProductUrl() +
                    ApiUtil.getPostData(config.getAdditionalData());

            String url = ApiHelper.getUrl() + "ebanking/initiate/";
            mBankView.setupWebView(url, data);
        } else {
            mBankView.showIndentedError("Something went wsrong");
        }
    }

    @Override
    public void updateToolbarTitle(String title) {
        mBankView.updateToolbarTitle(title);
    }

    @Override
    public void confirmPayment(String htmlMessage) {
        DataHolder.getConfig().getOnCheckOutListener().onSuccess(HtmlUtil.getData(htmlMessage));
        mBankView.close();
    }
}
