package com.inc.OrderProcessing.ComplianceEngine.service;

import com.inc.OrderProcessing.ComplianceEngine.model.Customer;

public class PricingService {

    public double applyDiscount(double totalAmount,
                                Customer customer,
                                boolean festivalEnabled) {
        return totalAmount; // mocked in test
    }

    public double calculateGST(double discountedAmount) {
        return 0; // mocked in test
    }
}
