package khalti.widget;


public enum ButtonStyle {
    BASIC(-1),
    FULL_BUTTON(0);

    private final int id;

    ButtonStyle(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
