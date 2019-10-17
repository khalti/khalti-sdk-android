package com.khalti.checkOut.api;


import androidx.annotation.Keep;

import java.util.HashMap;

@Keep
public interface OnCheckOutListener{
    void onSuccess(HashMap<String, Object> data);

    void onError(String action, String message);
}
