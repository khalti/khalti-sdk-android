package com.khalti.widget.basic;

import android.support.annotation.NonNull;

import com.utila.GuavaUtil;
import com.utila.NumberUtil;

class PayPresenter implements PayContract.Listener {
    @NonNull
    private final PayContract.View mPayView;

    private Long amount = 0L;

    PayPresenter(@NonNull PayContract.View mPayView) {
        this.mPayView = GuavaUtil.checkNotNull(mPayView);
        mPayView.setListener(this);
    }

    @Override
    public void setButtonText(String text) {
        mPayView.setButtonText(text);
    }

    @Override
    public void setAmount(Double value) {
        amount = NumberUtil.convertToPaisa(value);
    }

    @Override
    public void openForm() {
        mPayView.openForm();
    }
}
