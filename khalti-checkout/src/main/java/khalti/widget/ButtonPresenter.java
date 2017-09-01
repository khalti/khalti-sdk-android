package khalti.widget;

import android.support.annotation.NonNull;

import khalti.checkOut.api.Config;
import khalti.utils.EmptyUtil;
import khalti.utils.GuavaUtil;

class ButtonPresenter implements ButtonContract.Listener {
    @NonNull
    private final ButtonContract.View mPayView;

    ButtonPresenter(@NonNull ButtonContract.View mPayView) {
        this.mPayView = GuavaUtil.checkNotNull(mPayView);
        mPayView.setListener(this);
    }

    @Override
    public String checkConfig(Config config) {
        if (EmptyUtil.isNotNull(config.getPublicKey())) {
            return "Public key cannot be null";
        }
        if (EmptyUtil.isNotEmpty(config.getPublicKey())) {
            return "Public key cannot be empty";
        }
        if (EmptyUtil.isNotNull(config.getProductId())) {
            return "Product identity cannot be null";
        }
        if (EmptyUtil.isNotEmpty(config.getProductId())) {
            return "Product identity cannot be empty";
        }
        if (EmptyUtil.isNotNull(config.getProductName())) {
            return "Product name cannot be null";
        }
        if (EmptyUtil.isNotEmpty(config.getProductName())) {
            return "Product name cannot be empty";
        }
        if (EmptyUtil.isNotNull(config.getProductUrl())) {
            return "Product url cannot be null";
        }
        if (EmptyUtil.isNotEmpty(config.getProductUrl())) {
            return "Product url cannot be empty";
        }
        if (EmptyUtil.isNotNull(config.getAmount())) {
            return "Product url cannot be null";
        }
        if (EmptyUtil.isNotEmpty(config.getAmount())) {
            return "Product url cannot be 0";
        }
        if (EmptyUtil.isNotNull(config.getOnCheckOutListener())) {
            return "Listener cannot be null";
        }
        return null;
    }

    @Override
    public void setCustomButtonView() {
        mPayView.setCustomButtonView();
    }

    @Override
    public void setButtonStyle(int id) {
        mPayView.setButtonStyle(id);
    }

    @Override
    public void setButtonText(String text) {
        text = (EmptyUtil.isNotNull(text) && EmptyUtil.isNotEmpty(text)) ? text : "PAY";
        mPayView.setButtonText(text);
    }

    @Override
    public void setButtonClick() {
        mPayView.setButtonClick();
    }

    @Override
    public void openForm() {
        mPayView.openForm();
    }

    @Override
    public void destroyCheckOut() {
        mPayView.destroyCheckOut();
    }
}
