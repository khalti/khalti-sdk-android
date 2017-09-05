package khalti.checkOut;

import android.support.annotation.NonNull;

import java.util.HashMap;

import khalti.checkOut.api.Config;
import khalti.utils.GuavaUtil;


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
        HashMap<String, Config> map = new HashMap<>();
        map.put("config", mCheckOutView.getConfig());
        mCheckOutView.setupViewPager(map);
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
