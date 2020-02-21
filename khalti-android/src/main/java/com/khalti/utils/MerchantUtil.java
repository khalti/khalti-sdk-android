package com.khalti.utils;

import androidx.fragment.app.Fragment;

import java.util.HashMap;

import com.khalti.R;
import com.khalti.checkout.banking.Banking;
import com.khalti.checkout.helper.PaymentPreference;
import com.khalti.checkout.form.Form;

public class MerchantUtil {

    private static final HashMap<String, Fragment> CHECKOUT_FRAGMENTS = new HashMap<String, Fragment>() {{
        put(PaymentPreference.EBANKING.getValue(), new Banking());
        put(PaymentPreference.MOBILE_BANKING.getValue(), new Banking());
        put(PaymentPreference.SCT.getValue(), new Form());
        put(PaymentPreference.KHALTI.getValue(), new Form());
        put(PaymentPreference.CONNECT_IPS.getValue(), new Form());
    }};

    private static final HashMap<String, String> CHECKOUT_TITLES = new HashMap<String, String>() {{
        put(PaymentPreference.EBANKING.getValue(), "E-Banking");
        put(PaymentPreference.MOBILE_BANKING.getValue(), "Mobile Banking");
        put(PaymentPreference.SCT.getValue(), "SCT");
        put(PaymentPreference.KHALTI.getValue(), "Khalti");
        put(PaymentPreference.CONNECT_IPS.getValue(), "Connect IPS");
    }};

    private static final HashMap<String, Integer> CHECKOUT_ICONS = new HashMap<String, Integer>() {{
        put(PaymentPreference.EBANKING.getValue(), R.drawable.ic_ebanking);
        put(PaymentPreference.MOBILE_BANKING.getValue(), R.drawable.ic_mobile_banking);
        put(PaymentPreference.SCT.getValue(), R.drawable.ic_sct_card);
        put(PaymentPreference.KHALTI.getValue(), R.drawable.ic_wallet);
        put(PaymentPreference.CONNECT_IPS.getValue(), R.drawable.ic_connect_ips);
    }};

    public static HashMap<String, Object> getTab(String key) {
        return CHECKOUT_FRAGMENTS.containsKey(key) && CHECKOUT_TITLES.containsKey(key) && CHECKOUT_ICONS.containsKey(key) ?
                new HashMap<String, Object>() {{
                    put("fragment", CHECKOUT_FRAGMENTS.get(key));
                    put("title", CHECKOUT_TITLES.get(key));
                    put("icon", CHECKOUT_ICONS.get(key));
                }} : null;
    }
}