package khalti.checkOut.Wallet;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WalletConfirmPojo {
    @SerializedName("product_identity")
    @Expose
    private String productIdentity;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("amount")
    @Expose
    private Integer amount;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("product_url")
    @Expose
    private String productUrl;
    @SerializedName("mobile")
    @Expose
    private String mobile;

    public String getProductIdentity() {
        return productIdentity;
    }

    public String getToken() {
        return token;
    }

    public Integer getAmount() {
        return amount;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public String getMobile() {
        return mobile;
    }
}
