package khalti.checkOut;

public enum PaymentType {
    EBANKING("ebanking"),
    WALLET("wallet"),
    CARD("card");

    private final String id;

    PaymentType(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
