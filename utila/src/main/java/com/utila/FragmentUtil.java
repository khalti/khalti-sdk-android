package com.utila;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.util.HashMap;

public class FragmentUtil {
    public static void openFragment(Context context, Fragment fragment, boolean replace, boolean addToBackStack, boolean clearBackStack, boolean animate,
                                       HashMap<String, ?> data) {
        FragmentChange fragmentChange = (FragmentChange) context;
        if (EmptyUtil.isNotNull(data)) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("map", data);
            fragment.setArguments(bundle);
        }
        fragmentChange.changeFragment(fragment, replace, addToBackStack, clearBackStack, animate);
    }

    public interface FragmentChange {
        void changeFragment(Fragment newFragment, boolean replace, boolean addToBackStack, boolean clearBackStack, boolean animate);
    }
}
