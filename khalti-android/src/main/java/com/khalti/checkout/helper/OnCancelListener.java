package com.khalti.checkout.helper;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

import java.util.Map;

@Keep
public interface OnCancelListener {
    void onCancel();
}