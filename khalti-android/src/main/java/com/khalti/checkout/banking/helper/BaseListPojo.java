package com.khalti.checkout.banking.helper;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class BaseListPojo implements Parcelable {
    @SerializedName("total_pages")
    @Expose
    private Integer totalPages;
    @SerializedName("total_records")
    @Expose
    private Integer totalRecords;
    @SerializedName("next")
    @Expose
    private String next;
    @SerializedName("previous")
    @Expose
    private String previous;
    @SerializedName("record_range")
    @Expose
    private List<Integer> recordRange = new ArrayList<>();
    @SerializedName("current_page")
    @Expose
    private Integer currentPage;
    @SerializedName("records")
    @Expose
    private List<BankPojo> records = new ArrayList<>();

    public Integer getTotalPages() {
        return totalPages;
    }

    public Integer getTotalRecords() {
        return totalRecords;
    }

    public String getNext() {
        return next;
    }

    public String getPrevious() {
        return previous;
    }

    public List<Integer> getRecordRange() {
        return recordRange;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public List<BankPojo> getRecords() {
        return records;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.totalPages);
        dest.writeValue(this.totalRecords);
        dest.writeString(this.next);
        dest.writeString(this.previous);
        dest.writeList(this.recordRange);
        dest.writeValue(this.currentPage);
        dest.writeTypedList(this.records);
    }

    public BaseListPojo() {
    }

    protected BaseListPojo(Parcel in) {
        this.totalPages = (Integer) in.readValue(Integer.class.getClassLoader());
        this.totalRecords = (Integer) in.readValue(Integer.class.getClassLoader());
        this.next = in.readString();
        this.previous = in.readString();
        this.recordRange = new ArrayList<>();
        in.readList(this.recordRange, Integer.class.getClassLoader());
        this.currentPage = (Integer) in.readValue(Integer.class.getClassLoader());
        this.records = in.createTypedArrayList(BankPojo.CREATOR);
    }

    public static final Parcelable.Creator<BaseListPojo> CREATOR = new Parcelable.Creator<BaseListPojo>() {
        @Override
        public BaseListPojo createFromParcel(Parcel source) {
            return new BaseListPojo(source);
        }

        @Override
        public BaseListPojo[] newArray(int size) {
            return new BaseListPojo[size];
        }
    };
}