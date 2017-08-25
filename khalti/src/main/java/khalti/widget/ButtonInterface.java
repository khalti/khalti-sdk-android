package khalti.widget;


import android.view.View;

import khalti.checkOut.api.Config;

public interface ButtonInterface {
    void setText(String buttonText);

    void setCheckOutConfig(Config config);

    void setCustomView(View view);

    void setButtonStyle(ButtonStyle style);

    void destroyCheckOut();
}
