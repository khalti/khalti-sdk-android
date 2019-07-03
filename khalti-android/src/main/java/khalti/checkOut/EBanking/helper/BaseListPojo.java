package khalti.checkOut.EBanking.helper;


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
    private List<Integer> recordRange = new ArrayList<Integer>();
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

    public static Creator<BaseListPojo> getCREATOR() {
        return CREATOR;
    }

    protected BaseListPojo(Parcel in) {
        if (in.readByte() == 0) {
            totalPages = null;
        } else {
            totalPages = in.readInt();
        }
        if (in.readByte() == 0) {
            totalRecords = null;
        } else {
            totalRecords = in.readInt();
        }
        next = in.readString();
        previous = in.readString();
        if (in.readByte() == 0) {
            currentPage = null;
        } else {
            currentPage = in.readInt();
        }
        records = in.createTypedArrayList(BankPojo.CREATOR);
    }

    public static final Creator<BaseListPojo> CREATOR = new Creator<BaseListPojo>() {
        @Override
        public BaseListPojo createFromParcel(Parcel in) {
            return new BaseListPojo(in);
        }

        @Override
        public BaseListPojo[] newArray(int size) {
            return new BaseListPojo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (totalPages == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(totalPages);
        }
        if (totalRecords == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(totalRecords);
        }
        parcel.writeString(next);
        parcel.writeString(previous);
        if (currentPage == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(currentPage);
        }
        parcel.writeTypedList(records);
    }
}