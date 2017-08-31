package khalti.checkOut.Wallet;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WalletConfirmPojo {
    @SerializedName("detail")
    @Expose
    private String detail;

    public String getDetail() {
        return detail;
    }
}
