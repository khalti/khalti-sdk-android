package com.khalti.utils;

import androidx.fragment.app.Fragment;

import java.util.HashMap;

import com.khalti.R;
import com.khalti.checkOut.banking.Banking;
import com.khalti.checkOut.helper.PaymentPreference;
import com.khalti.checkOut.form.Form;

public class MerchantUtil {

    private static final HashMap<String, Fragment> CHECKOUT_FRAGMENTS = new HashMap<String, Fragment>() {{
        put(PaymentPreference.EBANKING.getValue(), new Banking());
        put(PaymentPreference.MOBILE_BANKING.getValue(), new Banking());
        put(PaymentPreference.SCT.getValue(), new Banking());
        put(PaymentPreference.WALLET.getValue(), new Form());
        put(PaymentPreference.CONNECT_IPS.getValue(), new Form());
    }};

    private static final HashMap<String, String> CHECKOUT_TITLES = new HashMap<String, String>() {{
        put(PaymentPreference.EBANKING.getValue(), "E-Banking");
        put(PaymentPreference.MOBILE_BANKING.getValue(), "Mobile Banking");
        put(PaymentPreference.SCT.getValue(), "SCT");
        put(PaymentPreference.WALLET.getValue(), "Wallet");
        put(PaymentPreference.CONNECT_IPS.getValue(), "Connect IPS");
    }};

    private static final HashMap<String, Integer> CHECKOUT_ICONS = new HashMap<String, Integer>() {{
        put(PaymentPreference.EBANKING.getValue(), R.drawable.ic_ebanking);
        put(PaymentPreference.MOBILE_BANKING.getValue(), R.drawable.ic_mobile_banking);
        put(PaymentPreference.SCT.getValue(), R.drawable.ic_sct_card);
        put(PaymentPreference.WALLET.getValue(), R.drawable.ic_wallet);
        put(PaymentPreference.CONNECT_IPS.getValue(), R.drawable.ic_connect_ips);
    }};

    private static final HashMap<String, Integer> CHECKOUT_ICONS_ACTIVE = new HashMap<String, Integer>() {{
        put(PaymentPreference.EBANKING.getValue(), R.drawable.ic_ebanking_active);
        put(PaymentPreference.MOBILE_BANKING.getValue(), R.drawable.ic_mobile_banking_active);
        put(PaymentPreference.SCT.getValue(), R.drawable.ic_sct_card_active);
        put(PaymentPreference.WALLET.getValue(), R.drawable.ic_wallet_active);
        put(PaymentPreference.CONNECT_IPS.getValue(), R.drawable.ic_connect_ips_active);
    }};

    public static HashMap<String, Object> getTab(String key) {
        return CHECKOUT_FRAGMENTS.containsKey(key) && CHECKOUT_TITLES.containsKey(key) && CHECKOUT_ICONS.containsKey(key) ?
                new HashMap<String, Object>() {{
                    put("fragment", CHECKOUT_FRAGMENTS.get(key));
                    put("title", CHECKOUT_TITLES.get(key));
                    put("icon", CHECKOUT_ICONS.get(key));
                    put("icon_active", CHECKOUT_ICONS_ACTIVE.get(key));
                }} : null;
    }
}