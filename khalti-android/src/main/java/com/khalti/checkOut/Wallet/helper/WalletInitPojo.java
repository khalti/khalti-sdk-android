package com.khalti.checkOut.Wallet.helper;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WalletInitPojo {
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("pin_created")
    @Expose
    private boolean pinCreated;
    @SerializedName("pin_created_message")
    @Expose
    private String pinCreatedMessage;

    public String getToken() {
        return token;
    }

    public boolean isPinCreated() {
        return pinCreated;
    }

    public String getPinCreatedMessage() {
        return pinCreatedMessage;
    }
}
