package com.khalti.checkout.helper;

import androidx.annotation.NonNull;

import java.util.Map;

public interface OnCheckOutListener {

    void onSuccess(@NonNull Map<String, Object> data);

    void onError(@NonNull String action, @NonNull String message);
}