package com.inc.OrderProcessing.ComplianceEngine.model;

import java.util.List;

public class Order {

    private Long id;
    private Customer customer;
    private OrderStatus status;
    private double totalAmount;

    public Order(Long id, Customer customer, double totalAmount) {
        this.id = id;
        this.customer = customer;
        this.totalAmount = totalAmount;
    }
    
    public Order(Long id,
            Customer customer,
            List<OrderItem> items,
            OrderStatus status) {
    	
    }

    public Long getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public double getTotalAmount() {
        return totalAmount;
    }
}
