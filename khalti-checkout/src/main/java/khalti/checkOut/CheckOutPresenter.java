package khalti.checkOut;

import android.support.annotation.NonNull;

import com.utila.GuavaUtil;

class CheckOutPresenter implements CheckOutContract.Listener {
    @NonNull
    private final CheckOutContract.View mCheckOutView;

    CheckOutPresenter(@NonNull CheckOutContract.View mCheckOutView) {
        this.mCheckOutView = GuavaUtil.checkNotNull(mCheckOutView);
        mCheckOutView.setListener(this);
    }

    @Override
    public void setUpLayout() {
        mCheckOutView.setStatusBarColor();
        mCheckOutView.setUpToolbar();
        mCheckOutView.setupViewPager();
        mCheckOutView.setUpTabLayout();
    }

    @Override
    public void toggleTab(int position, boolean selected) {
        mCheckOutView.toggleTab(position, selected);
    }

    @Override
    public void dismissAllDialogs() {
        mCheckOutView.dismissAllDialogs();
    }
}
