package com.khalti.form.api;

import java.util.HashMap;

public class Config {
    private String publicKey;
    private String productId;
    private String productName;
    private String productUrl;
    private Long amount;
    private HashMap<String, Object> additionalData;

    public Config(String publicKey, String productId, String productName, String productUrl, Long amount) {
        this.publicKey = publicKey;
        this.productId = productId;
        this.productName = productName;
        this.productUrl = productUrl;
        this.amount = amount;
    }

    public Config(String publicKey, String productId, String productName, String productUrl, Long amount, HashMap<String, Object> additionalData) {
        this.publicKey = publicKey;
        this.productId = productId;
        this.productName = productName;
        this.productUrl = productUrl;
        this.amount = amount;
        this.additionalData = additionalData;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public Long getAmount() {
        return amount;
    }

    public HashMap<String, Object> getAdditionalData() {
        return additionalData;
    }
}
