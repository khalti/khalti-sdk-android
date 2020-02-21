package com.khalti.checkout.helper;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

import java.util.Map;

@Keep
public interface OnCheckOutListener {

    void onSuccess(@NonNull Map<String, Object> data);

    void onError(@NonNull String action, @NonNull Map<String, String> errorMap);
}