package khalti.checkOut.Card;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;

import khalti.R;
import khalti.checkOut.EBanking.helper.BankAdapter;
import khalti.checkOut.api.Config;
import rx.Observable;

public class Card extends Fragment implements CardContract.View {

    private FragmentActivity fragmentActivity;
    private CardContract.Presenter presenter;
    private BankAdapter bankAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.payment_form, container, false);
        fragmentActivity = getActivity();
        presenter = new CardPresenter(this);

        presenter.onCreate();

        return mainView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    @Override
    public void toggleProgressBar(boolean show) {

    }

    @Override
    public void toggleEditTextListener(boolean set) {

    }

    @Override
    public void toggleButton(boolean enabled) {

    }

    @Override
    public void showCardFields() {

    }

    @Override
    public void setBankItemWithLogo(String logo, String shortName, String bankId) {

    }

    @Override
    public void setBankItemWithIcon(String icon, String name, String bankId) {

    }

    @Override
    public void setButtonText(String text) {

    }

    @Override
    public void setErrorAnimation() {

    }

    @Override
    public void setMobileError(String error) {

    }

    @Override
    public void showNetworkError() {

    }

    @Override
    public void showError(String message) {

    }

    @Override
    public void showMessageDialog(String title, String message) {

    }

    @Override
    public void openBankList(HashMap<String, Object> dataMap) {

    }

    @Override
    public void openCardBanking(String url) {

    }

    @Override
    public void saveConfigInFile(String fileName, Config config) {

    }

    @Override
    public String getPackageName() {
        return null;
    }

    @Override
    public boolean hasNetwork() {
        return false;
    }

    @Override
    public HashMap<String, Observable<Void>> setClickListeners() {
        return null;
    }

    @Override
    public void setPresenter(CardContract.Presenter presenter) {
        this.presenter = presenter;
    }
}
