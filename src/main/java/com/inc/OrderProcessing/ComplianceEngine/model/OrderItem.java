package com.inc.OrderProcessing.ComplianceEngine.model;

public class OrderItem {

    private String productName;
    private int quantity;
    private double price;

    public OrderItem(String productName, int quantity, double price) {
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }
}

