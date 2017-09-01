package khalti.checkOut.EBanking.chooseBank;

import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.List;

import khalti.utils.EmptyUtil;
import khalti.utils.GuavaUtil;

class BankChooserPresenter implements BankChooserContract.Listener {
    @NonNull
    private final BankChooserContract.View mBankChooserView;

    BankChooserPresenter(@NonNull BankChooserContract.View mBankChooserView) {
        this.mBankChooserView = GuavaUtil.checkNotNull(mBankChooserView);
        mBankChooserView.setListener(this);
    }

    @Override
    public void setUpLayout() {
        mBankChooserView.setStatusBarColor();
        HashMap<?, ?> map = mBankChooserView.receiveArgument();
        mBankChooserView.setUpToolbar();
        if (EmptyUtil.isNotNull(map)) {
            mBankChooserView.setUpList((List<BankPojo>) map.get("banks"));
        }
    }
}
