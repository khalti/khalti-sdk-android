package khalti.checkOut.EBanking;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;

import java.util.HashMap;
import java.util.List;

import khalti.R;
import khalti.carbonX.widget.Button;
import khalti.carbonX.widget.FrameLayout;
import khalti.carbonX.widget.ProgressBar;
import khalti.checkOut.EBanking.contactForm.ContactFormFragment;
import khalti.checkOut.EBanking.helper.BankAdapter;
import khalti.checkOut.EBanking.helper.BankPojo;
import khalti.checkOut.EBanking.helper.BankingData;
import khalti.utils.EmptyUtil;
import khalti.utils.NetworkUtil;
import khalti.utils.ResourceUtil;
import rx.Observable;

public class EBanking extends Fragment implements EBankingContract.View {

    private RecyclerView rvList;
    private LinearLayout llIndented;
    private ProgressBar pdLoad;
    private AppCompatTextView tvMessage, tvHeader;
    private FrameLayout flTryAgain, flCloseSearch, flSearch;
    private Button btnTryAgain;
    private AppBarLayout appBarLayout;
    private TextInputLayout tilSearch;
    private EditText etSearch;

    private FragmentActivity fragmentActivity;
    private EBankingContract.Presenter presenter;
    private BankAdapter bankAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.ebanking, container, false);
        fragmentActivity = getActivity();
        presenter = new EBankingPresenter(this);

        rvList = mainView.findViewById(R.id.rvList);
        llIndented = mainView.findViewById(R.id.llIndented);
        pdLoad = mainView.findViewById(R.id.pdLoad);
        tvMessage = mainView.findViewById(R.id.tvMessage);
        flTryAgain = mainView.findViewById(R.id.flTryAgain);
        flCloseSearch = mainView.findViewById(R.id.flCloseSearch);
        flSearch = mainView.findViewById(R.id.flSearch);
        btnTryAgain = mainView.findViewById(R.id.btnTryAgain);
        appBarLayout = mainView.findViewById(R.id.appBar);
        tilSearch = mainView.findViewById(R.id.tilSearch);
        etSearch = mainView.findViewById(R.id.etSearch);
        tvHeader = mainView.findViewById(R.id.tvHeader);

        presenter.onCreate(NetworkUtil.isNetworkAvailable(fragmentActivity));

        return mainView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    @Override
    public void toggleIndented(boolean show) {
        llIndented.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setUpList(List<BankPojo> bankList) {
        appBarLayout.setVisibility(View.VISIBLE);
        rvList.setVisibility(View.VISIBLE);
        bankAdapter = new BankAdapter(fragmentActivity, bankList);
        rvList.setAdapter(bankAdapter);
        rvList.setHasFixedSize(false);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(fragmentActivity, 3);
        rvList.setLayoutManager(layoutManager);
    }

    @Override
    public void showIndentedNetworkError() {
        pdLoad.setVisibility(View.INVISIBLE);
        flTryAgain.setVisibility(View.INVISIBLE);
        tvMessage.setVisibility(View.VISIBLE);
        tvMessage.setText(ResourceUtil.getString(fragmentActivity, R.string.network_error_body));
    }

    @Override
    public void showIndentedError(String error) {
        pdLoad.setVisibility(View.INVISIBLE);
        flTryAgain.setVisibility(View.VISIBLE);
        tvMessage.setVisibility(View.VISIBLE);
        tvMessage.setText(error);
    }

    @Override
    public void openMobileForm(BankingData bankingData) {
        ContactFormFragment contactFormFragment = new ContactFormFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", bankingData);
        contactFormFragment.setArguments(bundle);
        contactFormFragment.show(getFragmentManager(), contactFormFragment.getTag());
    }

    @Override
    public Observable<HashMap<String, String>> getItemClickObservable() {
        return bankAdapter.getItemClickObservable();
    }

    @Override
    public HashMap<String, Observable<Void>> setOnClickListener() {
        return new HashMap<String, Observable<Void>>() {{
            put("try_again", RxView.clicks(btnTryAgain));
            put("open_search", RxView.clicks(flSearch));
            put("close_search", RxView.clicks(flCloseSearch));
        }};
    }

    @Override
    public Observable<CharSequence> setEditTextListener() {
        return RxTextView.textChanges(etSearch);
    }

    @Override
    public void filterList(String text) {
        fragmentActivity.runOnUiThread(() -> bankAdapter.setFilter(text));
    }

    @Override
    public void flushList() {
        etSearch.setText("");
        bankAdapter.setFilter("");
    }

    @Override
    public void toggleSearch(boolean show) {
        if (show) {
            etSearch.requestFocus();
        }
        flCloseSearch.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
        tilSearch.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
        tvHeader.setVisibility(!show ? View.VISIBLE : View.INVISIBLE);
        flSearch.setEnabled(!show);

        android.widget.FrameLayout.LayoutParams lp = (android.widget.FrameLayout.LayoutParams) flSearch.getLayoutParams();
        lp.gravity = show ? Gravity.CENTER_VERTICAL | Gravity.START : Gravity.CENTER_VERTICAL | Gravity.END;
        flSearch.setLayoutParams(lp);
    }

    @Override
    public void toggleKeyboard(boolean show) {
        InputMethodManager inputManager = (InputMethodManager) fragmentActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (EmptyUtil.isNotNull(inputManager)) {
            if (show) {
                inputManager.showSoftInput(etSearch, InputMethodManager.SHOW_IMPLICIT);
            } else {
                inputManager.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
            }
        }
    }

    @Override
    public void setPresenter(EBankingContract.Presenter presenter) {
        this.presenter = presenter;
    }
}
