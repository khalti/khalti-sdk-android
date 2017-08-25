package khalti.widget;


public enum ButtonStyle {
    BASIC(-1),
    EBANKING_DARK(0),
    EBANKING_LIGHT(1),
    KHALTI_DARK(2),
    KHALTI_LIGHT(3);

    private final int id;

    ButtonStyle(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
