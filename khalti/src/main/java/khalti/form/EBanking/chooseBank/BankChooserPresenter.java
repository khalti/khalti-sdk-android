package khalti.form.EBanking.chooseBank;

import android.support.annotation.NonNull;

import com.utila.EmptyUtil;
import com.utila.GuavaUtil;

import java.util.HashMap;
import java.util.List;

class BankChooserPresenter implements BankChooserContract.Listener {
    @NonNull
    private final BankChooserContract.View mBankChooserView;

    BankChooserPresenter(@NonNull BankChooserContract.View mBankChooserView) {
        this.mBankChooserView = GuavaUtil.checkNotNull(mBankChooserView);
        mBankChooserView.setListener(this);
    }

    @Override
    public void setUpLayout() {
        HashMap<?, ?> map = mBankChooserView.receiveArgument();
        mBankChooserView.setUpToolbar();
        if (EmptyUtil.isNotNull(map)) {
            mBankChooserView.setUpList((List<BankPojo>) map.get("banks"));
        }
    }
}
