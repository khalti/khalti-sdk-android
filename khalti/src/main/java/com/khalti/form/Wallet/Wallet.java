package com.khalti.form.Wallet;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.khalti.R;


public class Wallet extends Fragment implements WalletContract.View {
    private FragmentActivity fragmentActivity;
    private WalletContract.Listener listener;

    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.payment_form, container, false);
        fragmentActivity = getActivity();
        listener = new WalletPresenter(this);

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
    public void setListener(WalletContract.Listener listener) {
        this.listener = listener;
    }
}
