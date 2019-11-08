package com.khalti.checkOut.card;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.appbar.AppBarLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.jakewharton.rxbinding.support.v7.widget.RxSearchView;
import com.jakewharton.rxbinding.view.RxView;

import java.util.HashMap;
import java.util.List;

import com.khalti.R;
import com.khalti.checkOut.card.contactForm.ContactFormFragment;
import com.khalti.checkOut.ebanking.helper.BankAdapter;
import com.khalti.checkOut.ebanking.helper.BankPojo;
import com.khalti.checkOut.ebanking.helper.BankingData;
import com.khalti.utils.EmptyUtil;
import com.khalti.utils.NetworkUtil;
import com.khalti.utils.ResourceUtil;
import rx.Observable;
import rx.subjects.PublishSubject;

public class Card extends Fragment implements CardContract.View {

    private RecyclerView rvList;
    private LinearLayout llIndented, llCardBranding;
    private FrameLayout flLoad;
    private AppCompatTextView tvMessage;
    private MaterialButton btnTryAgain;
    private AppBarLayout appBarLayout;
    private SearchView svBanks;
    private FrameLayout flSearchBank;

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
        flLoad = mainView.findViewById(R.id.flLoad);
        tvMessage = mainView.findViewById(R.id.tvMessage);
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
        flLoad.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void setUpList(List<BankPojo> bankList) {
       /* appBarLayout.setVisibility(View.VISIBLE);
        rvList.setVisibility(View.VISIBLE);
        bankAdapter = new BankAdapter(fragmentActivity, bankList);
        rvList.setAdapter(bankAdapter);
        rvList.setHasFixedSize(false);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(fragmentActivity, 3);
        rvList.setLayoutManager(layoutManager);*/
    }

    @Override
    public void showBranding() {
        llCardBranding.setVisibility(View.VISIBLE);
    }

    @Override
    public void showIndentedNetworkError() {
        flLoad.setVisibility(View.INVISIBLE);
        btnTryAgain.setVisibility(View.INVISIBLE);
        tvMessage.setVisibility(View.VISIBLE);
        tvMessage.setText(ResourceUtil.getString(fragmentActivity, R.string.network_error_body));
    }

    @Override
    public void showIndentedError(String error) {
        flLoad.setVisibility(View.INVISIBLE);
        btnTryAgain.setVisibility(View.VISIBLE);
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
//        return bankAdapter.getItemClickObservable();
        return null;
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
            count[0] = bankAdapter.filter(text);
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
