package com.khalti.utils;

import androidx.annotation.Keep;

import com.khalti.checkOut.api.Config;
import com.khalti.checkOut.helper.CheckoutEventListener;

@Keep
public class Store {
    private static Config config;
    private static CheckoutEventListener checkoutEventListener;

    public static Config getConfig() {
        return config;
    }

    public static void setConfig(Config config) {
        Store.config = config;
    }

    public static CheckoutEventListener getCheckoutEventListener() {
        return checkoutEventListener;
    }

    public static void setCheckoutEventListener(CheckoutEventListener checkoutEventListener) {
        Store.checkoutEventListener = checkoutEventListener;
    }
}
