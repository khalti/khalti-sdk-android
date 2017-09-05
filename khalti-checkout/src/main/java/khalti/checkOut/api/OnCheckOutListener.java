package khalti.checkOut.api;


import android.os.Parcelable;
import android.support.annotation.Keep;

import java.util.HashMap;

@Keep
public interface OnCheckOutListener extends Parcelable{
    void onSuccess(HashMap<String, Object> data);

    void onError(String action, String message);
}
