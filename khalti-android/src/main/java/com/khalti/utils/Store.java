package com.khalti.utils;

import androidx.annotation.Keep;

import com.khalti.checkout.helper.BaseComm;
import com.khalti.checkout.helper.Config;
import com.khalti.checkout.helper.CheckoutEventListener;

@Keep
public class Store {
    private static Config config;
    private static CheckoutEventListener checkoutEventListener;
    private static BaseComm baseComm;

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

    public static BaseComm getBaseComm() {
        return baseComm;
    }

    public static void setBaseComm(BaseComm baseComm) {
        Store.baseComm = baseComm;
    }
}