package com.khalti.form.EBanking.loadBank;

import android.support.annotation.NonNull;

import com.khalti.form.ApiHelper;
import com.khalti.form.api.Config;
import com.khalti.utils.DataHolder;
import com.utila.ApiUtil;
import com.utila.EmptyUtil;
import com.utila.GuavaUtil;

import java.util.HashMap;

class BankPresenter implements BankContract.Listener {

    @NonNull
    private final BankContract.View mBankView;
    private HashMap<?, ?> map;

    BankPresenter(@NonNull BankContract.View mBankView) {
        this.mBankView = GuavaUtil.checkNotNull(mBankView);
        mBankView.setListener(this);
    }

    @Override
    public void toggleIndentedProgress(boolean show) {
        mBankView.toggleIndentedProgress(show);
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
        if (isNetwork) {
            map = mBankView.receiveArguments();
            mBankView.setUpToolbar(EmptyUtil.isNotNull(map) ? map.get("bankName") + "" : "");
            Config config = DataHolder.getConfig();
            String data = "public_key=" + config.getPublicKey() + "&" +
                    "product_identity=" + config.getProductId() + "&" +
                    "product_name=" + config.getProductName() + "&" +
                    "product_url=" + config.getProductUrl() + "&" +
                    "amount=" + config.getAmount() + "&" +
                    "mobile=" + map.get("mobile") + "&" +
                    "bank=" + map.get("bankId") +
                    ApiUtil.getPostData(config.getAdditionalData());

            String url = ApiHelper.getUrl() + "ebanking/initiate/";
            mBankView.setupWebView("https://khalti.com/", data);
        } else {
            mBankView.showIndentedNetworkError();
        }
    }
}
