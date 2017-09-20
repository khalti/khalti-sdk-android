package khalti.widget;


import android.support.annotation.Keep;

@Keep
public enum ButtonStyle {
    BASIC(-1),
    EBANKING_DARK(0),
    EBANKING_LIGHT(1);

    private final int id;

    ButtonStyle(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
