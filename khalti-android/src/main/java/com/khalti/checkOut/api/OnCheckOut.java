package com.khalti.checkOut.api;

import java.io.Serializable;
import java.util.Map;

public interface OnCheckOut extends Serializable {

    interface OnCheckOutListener extends Serializable {
        void onSuccess(Map<String, Object> data);

        void onError(String action, String message);
    }
}