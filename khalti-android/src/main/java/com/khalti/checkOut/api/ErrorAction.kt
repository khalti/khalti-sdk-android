package com.khalti.checkOut.api

enum class ErrorAction private constructor(val action: String) {
    WALLET_INITIATE("wallet_initiate"),
    WALLET_CONFIRM("wallet_confirm"),
    FETCH_BANK_LIST("fetch_bank_list"),
    FETCH_CARD_BANK_LIST("fetch_card_bank_list")
}
