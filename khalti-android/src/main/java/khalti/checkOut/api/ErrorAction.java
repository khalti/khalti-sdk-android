package khalti.checkOut.api;

public enum ErrorAction {
    WALLET_INITIATE("wallet_initiate"),
    WALLET_CONFIRM("wallet_confirm"),
    FETCH_BANK_LIST("fetch_bank_list"),
    FETCH_CARD_BANK_LIST("fetch_card_bank_list");

    private final String action;

    ErrorAction(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }
}
