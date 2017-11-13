package khalti.widget;


import android.support.annotation.Keep;
import android.view.View;

import khalti.checkOut.api.Config;

@Keep
public interface KhaltiButtonInterface {
    void setText(String buttonText);

    void setCheckOutConfig(Config config);

    void setCustomView(View view);

    void setButtonStyle(ButtonStyle style);

    void setCustomClickListener(View.OnClickListener onClickListener);

    void showCheckOut();

    void destroyCheckOut();
}
