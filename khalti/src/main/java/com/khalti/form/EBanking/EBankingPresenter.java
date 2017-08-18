package com.khalti.form.EBanking;

import android.support.annotation.NonNull;

import com.khalti.form.EBanking.chooseBank.BankPojo;
import com.khalti.utils.DataHolder;
import com.utila.EmptyUtil;
import com.utila.GuavaUtil;
import com.utila.NumberUtil;
import com.utila.StringUtil;
import com.utila.ValidationUtil;

import java.util.HashMap;
import java.util.List;

class EBankingPresenter implements EBankingContract.Listener {
    @NonNull
    private final EBankingContract.View mEBankingView;
    private EBankingModel eBankingModel;
    private List<BankPojo> bankLists;

    EBankingPresenter(@NonNull EBankingContract.View mEBankingView) {
        this.mEBankingView = GuavaUtil.checkNotNull(mEBankingView);
        mEBankingView.setListener(this);
        eBankingModel = new EBankingModel();
    }

    @Override
    public void setUpLayout(boolean hasNetwork) {
        mEBankingView.showBankField();
        mEBankingView.setButtonText("Pay Rs " + StringUtil.formatNumber(NumberUtil.convertToRupees(DataHolder.getConfig().getAmount())));
        if (hasNetwork) {
            mEBankingView.toggleProgressBar(true);
            eBankingModel.fetchBankList(new EBankingModel.BankAction() {
                @Override
                public void onCompleted(Object bankList) {
                    mEBankingView.toggleProgressBar(false);
                    if (bankList instanceof HashMap) {
                        HashMap<?, ?> map = (HashMap<?, ?>) bankList;
                        mEBankingView.setUpSpinner(map.get("name"), map.get("idx"));
                    } else {
                        List<BankPojo> banks = (List<BankPojo>) bankList;
                        bankLists = banks;
                        mEBankingView.setUpBankItem(banks.get(0).getName(), banks.get(0).getIdx());
                    }
                }

                @Override
                public void onError(String message) {
                    mEBankingView.toggleProgressBar(false);
                    mEBankingView.showError(message);
                }
            });
        } else {
            mEBankingView.showNetworkError();
        }
    }

    @Override
    public void toggleEditTextListener(boolean set) {
        mEBankingView.toggleEditTextListener(set);
    }

    @Override
    public void setErrorAnimation() {
        mEBankingView.setErrorAnimation();
    }

    @Override
    public void openBankList() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("banks", bankLists);
        mEBankingView.openBankList(map);
    }

    @Override
    public void updateBankItem(String bankName, String bankId) {
        mEBankingView.setUpBankItem(bankName, bankId);
    }

    @Override
    public void continuePayment(boolean isNetwork, String mobile, String bankId) {
        if (EmptyUtil.isNotEmpty(mobile) && ValidationUtil.isMobileNumberValid(mobile)) {
            if (isNetwork) {
                mEBankingView.showMessageDialog("Success", "Something");
            } else {
                mEBankingView.showNetworkError();
            }
        } else {
            if (EmptyUtil.isEmpty(mobile)) {
                mEBankingView.setMobileError("This field is required");
            } else {
                mEBankingView.setMobileError("Invalid mobile number");
            }
        }
    }
}
