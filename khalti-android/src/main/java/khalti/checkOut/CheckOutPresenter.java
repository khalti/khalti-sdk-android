package khalti.checkOut;

import android.support.annotation.NonNull;

import khalti.utils.GuavaUtil;


class CheckOutPresenter implements CheckOutContract.Presenter {
    @NonNull
    private final CheckOutContract.View mCheckOutView;

    CheckOutPresenter(@NonNull CheckOutContract.View mCheckOutView) {
        this.mCheckOutView = GuavaUtil.checkNotNull(mCheckOutView);
        mCheckOutView.setPresenter(this);
    }

    @Override
    public void onCreate() {
        mCheckOutView.setStatusBarColor();
        mCheckOutView.setUpToolbar();
        mCheckOutView.setupViewPager();
        mCheckOutView.setUpTabLayout();
    }

    @Override
    public void onTabSelected(int position, boolean selected) {
        mCheckOutView.toggleTab(position, selected);
    }

    @Override
    public void dismissAllDialogs() {
        mCheckOutView.dismissAllDialogs();
    }
}
