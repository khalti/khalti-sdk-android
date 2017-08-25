package khalti.checkOut.api;


import java.util.HashMap;

public interface OnCheckOutListener {
    void onSuccess(HashMap<String, Object> data);

    void onError(String action, String message);
}
