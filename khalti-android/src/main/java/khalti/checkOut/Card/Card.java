package khalti.checkOut.Card;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.transition.ChangeBounds;
import android.support.transition.Transition;
import android.support.transition.TransitionManager;
import android.support.transition.TransitionSet;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import khalti.R;
import khalti.carbonX.widget.Button;
import khalti.carbonX.widget.FrameLayout;
import khalti.carbonX.widget.ProgressBar;
import khalti.checkOut.CheckOutActivity;
import khalti.checkOut.EBanking.chooseBank.BankChooserActivity;
import khalti.checkOut.api.Config;
import khalti.utils.FileStorageUtil;
import khalti.utils.NetworkUtil;
import khalti.utils.ResourceUtil;
import khalti.utils.UserInterfaceUtil;
import rx.Observable;

public class Card extends Fragment implements CardContract.View {

    private EditText etMobile;
    private khalti.carbonX.widget.TextInputLayout tilMobile;
    private Button btnPay;
    private ProgressBar pdLoad;
    private LinearLayout llCardBranding, llMobile;
    private FrameLayout flBankLogo, flBank;
    private ImageView ivBankLogo;
    private AppCompatTextView tvBankName, tvBankId;

    private FragmentActivity fragmentActivity;
    private CardContract.Presenter presenter;
    private Dialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.payment_form, container, false);
        fragmentActivity = getActivity();
        presenter = new CardPresenter(this);

        etMobile = mainView.findViewById(R.id.etMobile);
        tilMobile = mainView.findViewById(R.id.tilMobile);
        btnPay = mainView.findViewById(R.id.btnPay);
        pdLoad = mainView.findViewById(R.id.pdLoad);
        llCardBranding = mainView.findViewById(R.id.llCardBranding);
        llMobile = mainView.findViewById(R.id.llMobile);
        flBankLogo = mainView.findViewById(R.id.flBankLogo);
        flBank = mainView.findViewById(R.id.flBank);
        ivBankLogo = mainView.findViewById(R.id.ivBankLogo);
        tvBankName = mainView.findViewById(R.id.tvBank);
        tvBankId = mainView.findViewById(R.id.tvBankId);

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
        pdLoad.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void toggleButton(boolean enabled) {
        btnPay.setEnabled(enabled);
    }

    @Override
    public void showCardFields() {
        llCardBranding.setVisibility(View.VISIBLE);
    }

    @Override
    public void setBankItem(String logo, String name, String shortName, String bankId) {
        Picasso.with(fragmentActivity)
                .load(logo)
                .noFade()
                .into(ivBankLogo, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        flBankLogo.setVisibility(View.VISIBLE);
                        tvBankName.setText(shortName);
                    }

                    @Override
                    public void onError() {
                        flBankLogo.setVisibility(View.GONE);
                        tvBankName.setText(name);
                    }
                });
        tvBankName.setText(bankId);
    }

    @Override
    public void setButtonText(String text) {
        btnPay.setText(text);
    }

    @Override
    public Observable<CharSequence> setEditTextListener() {
        return RxTextView.textChanges(etMobile);
    }

    @Override
    public void setErrorAnimation() {
        TransitionSet transitionSet = new TransitionSet();

        Transition errorTransition = new ChangeBounds();
        errorTransition.setInterpolator(new AccelerateDecelerateInterpolator());
        errorTransition.setDuration(400);
        errorTransition.addTarget(tilMobile);

        transitionSet.addTransition(errorTransition);

        TransitionManager.beginDelayedTransition(llMobile, transitionSet);
    }

    @Override
    public void setMobileError(String error) {
        tilMobile.setError(error);
    }

    @Override
    public void showNetworkError() {
        UserInterfaceUtil.showSnackBar(fragmentActivity, ((CheckOutActivity) this.fragmentActivity).cdlMain, ResourceUtil.getString(fragmentActivity, R.string.network_error_body),
                false, "", 0, 0, null);
    }

    @Override
    public void showError(String message) {
        /*UserInterfaceUtil.showSnackBar(fragmentActivity, ((CheckOutActivity) this.fragmentActivity).cdlMain, message,
                true, ResourceUtil.getString(fragmentActivity, R.string.try_again), Snackbar.LENGTH_INDEFINITE, R.color.khaltiAccent, () ->
                        listener.setUpLayout(NetworkUtil.isNetworkAvailable(fragmentActivity)));*/
    }

    @Override
    public void showMessageDialog(String title, String message) {
        FrameLayout flButton = (FrameLayout) fragmentActivity.getLayoutInflater().inflate(R.layout.component_flat_button, null);
        AppCompatTextView tvButton = flButton.findViewById(R.id.tvButton);
        tvButton.setText(ResourceUtil.getString(fragmentActivity, R.string.got_it));

        UserInterfaceUtil.showInfoDialog(fragmentActivity, title, message, true, true, ResourceUtil.getString(fragmentActivity, R.string.got_it), null,
                new UserInterfaceUtil.DialogAction() {
                    @Override
                    public void onPositiveAction(Dialog dialog) {
                        dialog.dismiss();
                    }

                    @Override
                    public void onNegativeAction(Dialog dialog) {

                    }
                });
    }

    @Override
    public void openBankList(HashMap<String, Object> dataMap) {
        Intent intent = new Intent(fragmentActivity, BankChooserActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("map", dataMap);
        intent.putExtras(bundle);
        startActivityForResult(intent, 1007);
    }

    @Override
    public void openCardBanking(String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    @Override
    public void saveConfigInFile(String fileName, Config config) {
        FileStorageUtil.writeIntoFile(fragmentActivity, fileName, config);
    }

    @Override
    public String getPackageName() {
        return fragmentActivity.getPackageName();
    }

    @Override
    public boolean hasNetwork() {
        return NetworkUtil.isNetworkAvailable(fragmentActivity);
    }

    @Override
    public HashMap<String, Observable<Void>> setClickListeners() {
        return new HashMap<String, Observable<Void>>() {{
            put("pay", RxView.clicks(btnPay));
            put("open_bank_list", RxView.clicks(flBank));
        }};
    }

    @Override
    public void setPresenter(CardContract.Presenter presenter) {
        this.presenter = presenter;
    }
}
