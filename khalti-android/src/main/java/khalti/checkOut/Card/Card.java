package khalti.checkOut.Card;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.jakewharton.rxbinding.support.v7.widget.RxSearchView;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;

import java.util.HashMap;
import java.util.List;

import khalti.R;
import khalti.carbonX.widget.Button;
import khalti.carbonX.widget.FrameLayout;
import khalti.carbonX.widget.ProgressBar;
import khalti.checkOut.Card.contactForm.ContactFormFragment;
import khalti.checkOut.EBanking.helper.BankAdapter;
import khalti.checkOut.EBanking.helper.BankPojo;
import khalti.checkOut.EBanking.helper.BankingData;
import khalti.utils.EmptyUtil;
import khalti.utils.NetworkUtil;
import khalti.utils.ResourceUtil;
import rx.Observable;
import rx.subjects.PublishSubject;

public class Card extends Fragment implements CardContract.View {

    private RecyclerView rvList;
    private LinearLayout llIndented, llCardBranding;
    private ProgressBar pdLoad;
    private AppCompatTextView tvMessage;
    private FrameLayout flTryAgain;
    private Button btnTryAgain;
    private AppBarLayout appBarLayout;
    private SearchView svBanks;
    private android.widget.FrameLayout flSearchBank;

    private FragmentActivity fragmentActivity;
    private CardContract.Presenter presenter;
    private BankAdapter bankAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.banking, container, false);
        fragmentActivity = getActivity();
        presenter = new CardPresenter(this);

        rvList = mainView.findViewById(R.id.rvList);
        llIndented = mainView.findViewById(R.id.llIndented);
        llCardBranding = mainView.findViewById(R.id.llCardBranding);
        pdLoad = mainView.findViewById(R.id.pdLoad);
        tvMessage = mainView.findViewById(R.id.tvMessage);
        flTryAgain = mainView.findViewById(R.id.flTryAgain);
        btnTryAgain = mainView.findViewById(R.id.btnTryAgain);
        appBarLayout = mainView.findViewById(R.id.appBar);
        svBanks = mainView.findViewById(R.id.svBank);
        flSearchBank = mainView.findViewById(R.id.flSearchBank);

        presenter.onCreate();

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
        pdLoad.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
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
    public void showBranding() {
        llCardBranding.setVisibility(View.VISIBLE);
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
        if (EmptyUtil.isNotNull(getFragmentManager())) {
            contactFormFragment.show(getFragmentManager(), contactFormFragment.getTag());
        }
    }

    @Override
    public Observable<HashMap<String, String>> getItemClickObservable() {
        return bankAdapter.getItemClickObservable();
    }

    @Override
    public HashMap<String, Observable<Void>> setOnClickListener() {
        return new HashMap<String, Observable<Void>>() {{
            put("try_again", RxView.clicks(btnTryAgain));
        }};
    }

    @Override
    public Observable<CharSequence> setSearchListener() {
        return RxSearchView.queryTextChanges(svBanks);
    }

    @Override
    public Observable<Integer> filterList(String text) {
        PublishSubject<Integer> publishSubject = PublishSubject.create();
        final Integer[] count = new Integer[1];
        fragmentActivity.runOnUiThread(() -> {
            count[0] = bankAdapter.setFilter(text);
            publishSubject.onNext(count[0]);
        });
        return publishSubject;
    }

    @Override
    public void toggleSearch(boolean show) {
        flSearchBank.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void toggleSearchError(boolean show) {
        rvList.setVisibility(show ? View.GONE : View.VISIBLE);
        llIndented.setVisibility(show ? View.VISIBLE : View.GONE);
        tvMessage.setVisibility(View.VISIBLE);
        tvMessage.setText(ResourceUtil.getString(fragmentActivity, R.string.no_banks));
    }

    @Override
    public boolean hasNetwork() {
        return NetworkUtil.isNetworkAvailable(fragmentActivity);
    }

    @Override
    public void setPresenter(CardContract.Presenter presenter) {
        this.presenter = presenter;
    }
}
