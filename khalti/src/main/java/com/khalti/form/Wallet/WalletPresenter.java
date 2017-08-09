package com.khalti.form.Wallet;

import android.support.annotation.NonNull;

import com.utila.GuavaUtil;

class WalletPresenter implements WalletContract.Listener {
    @NonNull
    private final WalletContract.View mWalletView;

    WalletPresenter(@NonNull WalletContract.View mWalletView) {
        this.mWalletView = GuavaUtil.checkNotNull(mWalletView);
        mWalletView.setListener(this);
    }

    @Override
    public void setUpLayout() {
    }
}
