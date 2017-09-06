package khalti.checkOut.EBanking;

import java.util.HashMap;

import khalti.checkOut.api.Config;

public interface EBankingContract {
    interface View {

        void toggleProgressBar(boolean show);

        void toggleEditTextListener(boolean set);

        void toggleButton(boolean enabled);

        void showBankField();

        void setUpSpinner(Object banks, Object bankIds);

        void setUpBankItem(String bankName, String bankId);

        void setButtonText(String text);

        void setErrorAnimation();

        void setMobileError(String error);

        void showNetworkError();

        void showError(String message);

        void showMessageDialog(String title, String message);

        void openBankList(HashMap<String, Object> dataMap);

        void openEBanking(String url);

        void saveConfigInFile(String fileName, Config config);

        void setListener(Listener listener);
    }

    interface Listener {

        void setUpLayout(boolean hasNetwork);

        void toggleEditTextListener(boolean set);

        void setErrorAnimation();

        void openBankList();

        void updateBankItem(String bankName, String bankId);

        void initiatePayment(boolean isNetwork, String mobile, String bankId, String bankName);

        void unSubscribe();
    }
}
