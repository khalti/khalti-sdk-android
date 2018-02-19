package khalti.utils;

import android.support.v4.app.Fragment;

import java.util.HashMap;

import khalti.R;
import khalti.checkOut.Card.Card;
import khalti.checkOut.EBanking.EBanking;
import khalti.checkOut.Wallet.Wallet;

public class MerchantUtil {

    public static final String WALLET = "wallet";
    public static final String CARD = "card";
    public static final String EBANKING = "ebanking";

    private static final HashMap<String, Fragment> CHECKOUT_FRAGMENTS = new HashMap<String, Fragment>() {{
        put(EBANKING, new EBanking());
        put(CARD, new Card());
        put(WALLET, new Wallet());
    }};

    private static final HashMap<String, String> CHECKOUT_TITLES = new HashMap<String, String>() {{
        put(EBANKING, "E-Banking");
        put(CARD, "Card");
        put(WALLET, "Wallet");
    }};

    private static final HashMap<String, Integer> CHECKOUT_ICONS = new HashMap<String, Integer>() {{
        put("ebanking", R.drawable.ic_bank);
        put("card", R.drawable.ic_credit_card);
        put("wallet", R.drawable.ic_wallet);
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
