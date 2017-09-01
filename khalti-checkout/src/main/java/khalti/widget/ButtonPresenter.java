package khalti.widget;

import android.support.annotation.NonNull;

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
