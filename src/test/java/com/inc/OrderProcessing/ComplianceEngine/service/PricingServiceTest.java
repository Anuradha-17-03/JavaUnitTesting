package com.inc.OrderProcessing.ComplianceEngine.service;


import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PricingService unit tests")
@ExtendWith(MockitoExtension.class)
class PricingServiceTest {

    private PricingService pricingService;

    @BeforeEach
    void init() {
        pricingService = new PricingService();
    }

    @ParameterizedTest(name = "amount={0}, premium={1}, festival={2} => discount={3}%")
    @CsvFileSource(resources = "/pricing-discount-rules.csv", numLinesToSkip = 1)
    @DisplayName("should calculate discount percentage correctly")
    void shouldCalculateDiscountPercentage(
            double orderTotal,
            boolean premiumCustomer,
            boolean festivalEnabled,
            double expectedDiscount
    ) {
        // Arrange
        Customer customer = new Customer();
        customer.setPremium(premiumCustomer);

        Order order = TestDataFactory.createOrder(orderTotal, customer, festivalEnabled);

        // Act
        double discount = pricingService.calculateDiscountPercentage(order);

        // Assert
        assertEquals(
                expectedDiscount,
                discount,
                0.01,
                "Discount percentage mismatch"
        );
    }

    @Test
    @DisplayName("should apply GST after discount with rounding")
    void shouldCalculateGstAfterDiscount() {
        // Arrange
        double discountedAmount = 10000.75;

        // Act
        double gst = pricingService.calculateGst(discountedAmount);

        // Assert
        assertEquals(
                1800.14,
                gst,
                0.01,
                "GST calculation is incorrect"
        );
    }
}
