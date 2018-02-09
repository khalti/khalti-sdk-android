package khalti.checkOut.EBanking.contactForm;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.squareup.picasso.Picasso;

import khalti.R;
import khalti.carbonX.widget.FrameLayout;
import khalti.checkOut.CheckOutActivity;
import khalti.checkOut.EBanking.helper.BankingData;
import khalti.checkOut.api.Config;
import khalti.utils.EmptyUtil;
import khalti.utils.FileStorageUtil;
import khalti.utils.NetworkUtil;
import khalti.utils.ResourceUtil;
import khalti.utils.UserInterfaceUtil;
import rx.Observable;

public class ContactFormFragment extends BottomSheetDialogFragment implements ContactFormContract.View {

    private FragmentActivity fragmentActivity;
    private ContactFormContract.Presenter presenter;

    private TextInputLayout tilContact;
    private EditText etContact;
    private FrameLayout btnPay, flBankLogo, flBankTextIcon;
    private AppCompatTextView tvButton;
    private ImageView ivBankLogo;
    private AppCompatTextView tvBankIcon, tvBankName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.banking_contact, container, false);
        fragmentActivity = getActivity();

        tilContact = mainView.findViewById(R.id.tilContact);
        etContact = mainView.findViewById(R.id.etContact);
        btnPay = mainView.findViewById(R.id.btnPay);
        tvButton = btnPay.findViewById(R.id.tvButton);
        flBankLogo = mainView.findViewById(R.id.flBankLogo);
        flBankTextIcon = mainView.findViewById(R.id.flBankTextIcon);
        ivBankLogo = mainView.findViewById(R.id.ivBankLogo);
        tvBankIcon = mainView.findViewById(R.id.tvBankIcon);
        tvBankName = mainView.findViewById(R.id.tvBankName);

        presenter = new ContactFormPresenter(this);
        presenter.onCreate();

        return mainView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroy();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        dialog.setOnShowListener(dialog1 -> {
            BottomSheetDialog d = (BottomSheetDialog) dialog1;

            android.widget.FrameLayout bottomSheet = d.findViewById(R.id.design_bottom_sheet);
            if (EmptyUtil.isNotNull(bottomSheet)) {
                BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
        return dialog;
    }

    @Override
    public BankingData receiveData() {
        Bundle bundle = this.getArguments();
        if (EmptyUtil.isNotNull(bundle)) {
            return (BankingData) bundle.getSerializable("data");
        }
        return null;
    }

    @Override
    public void setBankData(String logo, String name, String icon) {
        tvBankName.setText(name);
        if (EmptyUtil.isNotNull(logo) && EmptyUtil.isNotEmpty(logo)) {
            Picasso.with(fragmentActivity)
                    .load(logo)
                    .noFade()
                    .into(ivBankLogo, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            flBankLogo.setVisibility(View.VISIBLE);
                            flBankTextIcon.setVisibility(View.GONE);
                            tvBankIcon.setText(icon);
                        }

                        @Override
                        public void onError() {
                            flBankLogo.setVisibility(View.GONE);
                            flBankTextIcon.setVisibility(View.VISIBLE);
                            tvBankIcon.setText(icon);
                        }
                    });

        } else {
            flBankLogo.setVisibility(View.GONE);
            flBankTextIcon.setVisibility(View.VISIBLE);
            tvBankIcon.setText(icon);
        }
    }

    @Override
    public void setButtonText(String text) {
        tvButton.setText(text);
    }

    @Override
    public void setEditTextError(String error) {
        tilContact.setError(error);
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
    public void showError(String message) {
        UserInterfaceUtil.showSnackBar(fragmentActivity, ((CheckOutActivity) this.fragmentActivity).cdlMain, message,
                false, null, Snackbar.LENGTH_LONG, 0, null);
    }

    @Override
    public void showNetworkError() {
        UserInterfaceUtil.showSnackBar(fragmentActivity, ((khalti.checkOut.CheckOutActivity) this.fragmentActivity).cdlMain,
                ResourceUtil.getString(fragmentActivity, R.string.network_error_body), false, "", 0, 0, null);
    }

    @Override
    public void openEBanking(String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        dismiss();
        startActivity(browserIntent);
    }

    @Override
    public void saveConfigInFile(Config config) {
        FileStorageUtil.writeIntoFile(fragmentActivity, "khalti_config", config);
    }

    @Override
    public String getPackageName() {
        return fragmentActivity.getPackageName();
    }

    @Override
    public String getContactNumber() {
        return etContact.getText() + "";
    }

    @Override
    public Observable<Void> setClickListener() {
        return RxView.clicks(btnPay);
    }

    @Override
    public Observable<CharSequence> setEditTextListener() {
        return RxTextView.textChanges(etContact);
    }

    @Override
    public boolean isNetworkAvailable() {
        return NetworkUtil.isNetworkAvailable(fragmentActivity);
    }

    @Override
    public void setPresenter(ContactFormContract.Presenter presenter) {
        this.presenter = presenter;
    }
}
