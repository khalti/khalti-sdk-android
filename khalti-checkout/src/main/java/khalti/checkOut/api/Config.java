package khalti.checkOut.api;

import android.support.annotation.Keep;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.HashMap;

@Keep
public class Config implements Serializable {
    private String publicKey;
    private String productId;
    private String productName;
    private String productUrl;
    private Long amount;
    private HashMap<String, Object> additionalData;
    private OnCheckOutListener onCheckOutListener;

    public Config(@NonNull String publicKey, @NonNull String productId, @NonNull String productName, @NonNull String productUrl, @NonNull Long amount,
                  @NonNull OnCheckOutListener onCheckOutListener) {
        this.publicKey = publicKey;
        this.productId = productId;
        this.productName = productName;
        this.productUrl = productUrl;
        this.amount = amount;
        this.onCheckOutListener = onCheckOutListener;
    }

    public Config(@NonNull String publicKey, @NonNull String productId, @NonNull String productName, @NonNull String productUrl, @NonNull Long amount,
                  @NonNull OnCheckOutListener onCheckOutListener, @NonNull HashMap<String, Object> additionalData) {
        this.publicKey = publicKey;
        this.productId = productId;
        this.productName = productName;
        this.productUrl = productUrl;
        this.amount = amount;
        this.onCheckOutListener = onCheckOutListener;
        this.additionalData = additionalData;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public Long getAmount() {
        return amount;
    }

    public OnCheckOutListener getOnCheckOutListener() {
        return onCheckOutListener;
    }

    public HashMap<String, Object> getAdditionalData() {
        return additionalData;
    }
}
