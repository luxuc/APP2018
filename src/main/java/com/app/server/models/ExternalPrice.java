package com.app.server.models;

public class ExternalPrice {

    public String getProductName() {
        return productName;
    }

    public String getQuantity() {
        return quantity;
    }

    public float getPrice() {
        return unitPrice;
    }

    public String getUrl() { return url; }

    String productName;
    String quantity;
    float unitPrice;
    String url;

    public ExternalPrice(String productName, String quantity, float unitPrice, String url) {
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.url = url;
    }

}
