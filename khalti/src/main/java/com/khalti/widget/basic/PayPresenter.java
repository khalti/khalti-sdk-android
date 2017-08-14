package com.khalti.widget.basic;

import android.support.annotation.NonNull;

import com.khalti.form.api.Config;
import com.khalti.utils.DataHolder;
import com.utila.GuavaUtil;

class PayPresenter implements PayContract.Listener {
    @NonNull
    private final PayContract.View mPayView;

    PayPresenter(@NonNull PayContract.View mPayView) {
        this.mPayView = GuavaUtil.checkNotNull(mPayView);
        mPayView.setListener(this);
    }

    @Override
    public void setButtonText(String text) {
        mPayView.setButtonText(text);
    }

    @Override
    public void setConfig(Config config) {
        DataHolder.setConfig(config);
    }

    @Override
    public void openForm() {
        mPayView.openForm();
    }
}
