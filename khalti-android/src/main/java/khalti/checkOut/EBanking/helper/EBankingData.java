package khalti.checkOut.EBanking.helper;

import android.support.annotation.Keep;

import java.io.Serializable;

import khalti.checkOut.api.Config;

@Keep
public class EBankingData implements Serializable {
    private String bankIdx;
    private String bankName;
    private String bankLogo;
    private String bankIcon;
    private Config config;

    public EBankingData(String bankIdx, String bankName, String bankLogo, String bankIcon, Config config) {
        this.bankIdx = bankIdx;
        this.bankName = bankName;
        this.bankLogo = bankLogo;
        this.bankIcon = bankIcon;
        this.config = config;
    }

    public String getBankIdx() {
        return bankIdx;
    }

    public String getBankName() {
        return bankName;
    }

    public Config getConfig() {
        return config;
    }

    public String getBankLogo() {
        return bankLogo;
    }

    public String getBankIcon() {
        return bankIcon;
    }
}
