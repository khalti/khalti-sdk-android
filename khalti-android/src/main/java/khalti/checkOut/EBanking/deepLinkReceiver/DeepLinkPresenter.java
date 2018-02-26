package khalti.checkOut.EBanking.deepLinkReceiver;

import android.support.annotation.NonNull;

import java.util.HashMap;

import khalti.checkOut.api.Config;
import khalti.checkOut.api.OnCheckOutListener;
import khalti.rxBus.RxBus;
import khalti.utils.EmptyUtil;
import khalti.utils.GuavaUtil;
import khalti.utils.LogUtil;
import khalti.utils.Store;

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
        RxBus.getInstance().post("close_check_out", null);
    }
}