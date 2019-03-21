package com.khalti.checkOut.helper;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MerchantPreferencePojo {

    @SerializedName("sdk_position")
    @Expose
    private List<String> sdkPosition = new ArrayList<>();

    public List<String> getSdkPosition() {
        return sdkPosition;
    }
}