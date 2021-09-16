package com.khalti.checkout.banking.helper;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BankPojo implements Parcelable {

    @SerializedName("idx")
    @Expose
    private final String idx;
    @SerializedName("name")
    @Expose
    private final String name;
    @SerializedName("short_name")
    @Expose
    private final String shortName;
    @SerializedName("logo")
    @Expose
    private final String logo;

    protected BankPojo(Parcel in) {
        idx = in.readString();
        name = in.readString();
        shortName = in.readString();
        logo = in.readString();
    }

    public static final Creator<BankPojo> CREATOR = new Creator<BankPojo>() {
        @Override
        public BankPojo createFromParcel(Parcel in) {
            return new BankPojo(in);
        }

        @Override
        public BankPojo[] newArray(int size) {
            return new BankPojo[size];
        }
    };

    public String getIdx() {
        return idx;
    }

    public String getName() {
        return name;
    }

    public String getShortName() {
        return shortName;
    }

    public String getLogo() {
        return logo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(idx);
        parcel.writeString(name);
        parcel.writeString(shortName);
        parcel.writeString(logo);
    }
}
