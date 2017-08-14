package com.khalti.form.api;

import java.util.HashMap;

public class Config {
    private String publicKey;
    private String returnUrl;
    private String productId;
    private String productName;
    private String productUrl;
    private Long amount;
    private String mobile;
    private HashMap<String, Object> additionalData;

    public Config(String publicKey, String returnUrl, String productId, String productName, String productUrl, Long amount, String mobile) {
        this.publicKey = publicKey;
        this.returnUrl = returnUrl;
        this.productId = productId;
        this.productName = productName;
        this.productUrl = productUrl;
        this.amount = amount;
        this.mobile = mobile;
    }

    public Config(String publicKey, String returnUrl, String productId, String productName, String productUrl, Long amount, String mobile, HashMap<String, Object> additionalData) {
        this.publicKey = publicKey;
        this.returnUrl = returnUrl;
        this.productId = productId;
        this.productName = productName;
        this.productUrl = productUrl;
        this.amount = amount;
        this.mobile = mobile;
        this.additionalData = additionalData;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public String getReturnUrl() {
        return returnUrl;
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

    public String getMobile() {
        return mobile;
    }

    public HashMap<String, Object> getAdditionalData() {
        return additionalData;
    }
}
