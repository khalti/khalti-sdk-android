package khalti.checkOut.api;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;

import java.util.HashMap;

@Keep
public class Config implements Parcelable {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.publicKey);
        dest.writeString(this.productId);
        dest.writeString(this.productName);
        dest.writeString(this.productUrl);
        dest.writeValue(this.amount);
        dest.writeSerializable(this.additionalData);
        dest.writeParcelable(this.onCheckOutListener, flags);
    }

    protected Config(Parcel in) {
        this.publicKey = in.readString();
        this.productId = in.readString();
        this.productName = in.readString();
        this.productUrl = in.readString();
        this.amount = (Long) in.readValue(Long.class.getClassLoader());
        this.additionalData = (HashMap<String, Object>) in.readSerializable();
        this.onCheckOutListener = in.readParcelable(OnCheckOutListener.class.getClassLoader());
    }

    public static final Parcelable.Creator<Config> CREATOR = new Parcelable.Creator<Config>() {
        @Override
        public Config createFromParcel(Parcel source) {
            return new Config(source);
        }

        @Override
        public Config[] newArray(int size) {
            return new Config[size];
        }
    };
}
