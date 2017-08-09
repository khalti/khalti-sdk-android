package com.khalti.form.EBanking;

import android.support.annotation.NonNull;

import com.utila.GuavaUtil;

class EBankingPresenter implements EBankingContract.Listener {
    @NonNull
    private final EBankingContract.View mEBankingView;

    EBankingPresenter(@NonNull EBankingContract.View mEBankingView) {
        this.mEBankingView = GuavaUtil.checkNotNull(mEBankingView);
        mEBankingView.setListener(this);
    }

    @Override
    public void setUpLayout() {
    }
}
