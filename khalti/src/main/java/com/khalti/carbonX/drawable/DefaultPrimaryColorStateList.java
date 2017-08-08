package com.khalti.carbonX.drawable;

import android.content.Context;
import android.content.res.ColorStateList;

import com.khalti.R;
import com.khalti.carbonX.CarbonX;

public class DefaultPrimaryColorStateList extends ColorStateList {
    public DefaultPrimaryColorStateList(Context context) {
        super(new int[][]{
                new int[]{R.attr.carbonX_state_invalid},
                new int[]{-android.R.attr.state_enabled},
                new int[]{}
        }, new int[]{
                CarbonX.getThemeColor(context, R.attr.carbonX_colorError),
                CarbonX.getThemeColor(context, R.attr.carbonX_colorDisabled),
                CarbonX.getThemeColor(context, R.attr.colorPrimary)
        });
    }
}
