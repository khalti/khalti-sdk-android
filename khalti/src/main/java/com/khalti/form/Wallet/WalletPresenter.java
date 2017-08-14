package com.khalti.form.Wallet;

import android.support.annotation.NonNull;

import com.utila.EmptyUtil;
import com.utila.GuavaUtil;
import com.utila.RegexUtil;

class WalletPresenter implements WalletContract.Listener {
    @NonNull
    private final WalletContract.View mWalletView;

    WalletPresenter(@NonNull WalletContract.View mWalletView) {
        this.mWalletView = GuavaUtil.checkNotNull(mWalletView);
        mWalletView.setListener(this);
    }

    @Override
    public void setButtonClickListener() {
        mWalletView.setButtonClickListener();
    }

    @Override
    public void toggleEditTextListener(boolean set) {
        mWalletView.toggleEditTextListener(set);
    }

    @Override
    public void toggleConfirmationLayout(boolean show) {
        mWalletView.toggleConfirmationLayout(show);
    }

    @Override
    public void continuePayment(boolean isNetwork, String mobile) {
        if (EmptyUtil.isNotEmpty(mobile) && RegexUtil.isMobileNumberValid(mobile)) {
            mWalletView.toggleConfirmationLayout(true);
        } else {
            if (EmptyUtil.isEmpty(mobile)) {
                mWalletView.setEditTextError("mobile", "This field is required");
            } else {
                mWalletView.setEditTextError("mobile", "Invalid mobile number");
            }
        }
    }
}
