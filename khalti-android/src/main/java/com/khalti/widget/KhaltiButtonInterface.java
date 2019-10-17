package com.khalti.widget;


import androidx.annotation.Keep;
import android.view.View;

import com.khalti.checkOut.api.Config;

@Keep
public interface KhaltiButtonInterface {
    void setText(String buttonText);

    void setCheckOutConfig(Config config);

    void setCustomView(View view);

    void setButtonStyle(ButtonStyle style);

    void showCheckOut();

    void showCheckOut(Config config);

    void destroyCheckOut();
}
