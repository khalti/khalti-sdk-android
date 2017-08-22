package khalti.form.EBanking.chooseBank;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BankPojo implements Parcelable {
    @SerializedName("idx")
    @Expose
    private String idx;
    @SerializedName("created_on")
    @Expose
    private String createdOn;
    @SerializedName("modified_on")
    @Expose
    private String modifiedOn;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("address")
    @Expose
    private String address;

    public String getIdx() {
        return idx;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public String getModifiedOn() {
        return modifiedOn;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.idx);
        dest.writeString(this.createdOn);
        dest.writeString(this.modifiedOn);
        dest.writeString(this.name);
        dest.writeString(this.address);
    }

    public BankPojo() {
    }

    protected BankPojo(Parcel in) {
        this.idx = in.readString();
        this.createdOn = in.readString();
        this.modifiedOn = in.readString();
        this.name = in.readString();
        this.address = in.readString();
    }

    public static final Parcelable.Creator<BankPojo> CREATOR = new Parcelable.Creator<BankPojo>() {
        @Override
        public BankPojo createFromParcel(Parcel source) {
            return new BankPojo(source);
        }

        @Override
        public BankPojo[] newArray(int size) {
            return new BankPojo[size];
        }
    };
}
