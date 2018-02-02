package khalti.checkOut.Wallet.helper;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WalletInitPojo {
    @SerializedName("token")
    @Expose
    private String token;

    public String getToken() {
        return token;
    }
}
