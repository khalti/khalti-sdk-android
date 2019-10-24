package com.khalti.checkOut.EBanking.deepLinkReceiver;

import androidx.annotation.NonNull;

import com.khalti.checkOut.helper.Config;
import com.khalti.checkOut.api.OnCheckOutListener;
import com.khalti.utils.EmptyUtil;
import com.khalti.utils.GuavaUtil;
import com.khalti.utils.Store;

import java.util.HashMap;

class DeepLinkPresenter implements DeepLinkContract.Listener {
    @NonNull
    private final DeepLinkContract.View mDeepLinkView;

    DeepLinkPresenter(@NonNull DeepLinkContract.View mDeepLinkView) {
        this.mDeepLinkView = GuavaUtil.checkNotNull(mDeepLinkView);
        mDeepLinkView.setListener(this);
    }

    @Override
    public void receiveEBankingData() {
        HashMap<String, Object> map = mDeepLinkView.receiveEBankingData();
        Config config = EmptyUtil.isNotNull(Store.getConfig()) ? Store.getConfig() : mDeepLinkView.getConfigFromFile();

        if (EmptyUtil.isNotNull(map) && EmptyUtil.isNotNull(config)) {
            OnCheckOutListener onCheckOutListener = config.getOnCheckOutListener();
            onCheckOutListener.onSuccess(map);
        }
        mDeepLinkView.closeDeepLink();
//        RxBus.getInstance().post("close_check_out", null);
        Store.getCheckoutEventListener().closeCheckout();
    }
}