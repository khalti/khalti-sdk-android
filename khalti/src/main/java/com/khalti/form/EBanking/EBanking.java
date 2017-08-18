package com.khalti.form.EBanking;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.khalti.R;

import butterknife.ButterKnife;

public class EBanking extends Fragment implements EBankingContract.View {

    private FragmentActivity fragmentActivity;
    private EBankingContract.Listener listener;

    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.payment_form, container, false);
        ButterKnife.bind(this, mainView);
        fragmentActivity = getActivity();
        listener = new EBankingPresenter(this);

        return mainView;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void setListener(EBankingContract.Listener listener) {
        this.listener = listener;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
