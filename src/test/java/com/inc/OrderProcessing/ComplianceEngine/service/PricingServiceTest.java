package com.inc.OrderProcessing.ComplianceEngine.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;

import com.inc.OrderProcessing.ComplianceEngine.model.Customer;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pricing Service Tests")
class PricingServiceTest {

    private PricingService pricingService;

    @BeforeEach
    void setUp() {
        pricingService = new PricingService();
    }

    @ParameterizedTest(name = "Total={0}, Premium={1}, Festival={2}")
    @MethodSource("discountDataProvider")
    @DisplayName("Should apply correct discount with cap")
    void shouldApplyCorrectDiscount(
            double total,
            boolean isPremium,
            boolean isFestival,
            double expectedDiscountPercent) {

        // Arrange
        Customer customer = new Customer(isPremium);
        double discountedAmount;

        // Act
        discountedAmount = pricingService.applyDiscount(
                total, customer, isFestival);

        // Assert
        double expected = total * (1 - expectedDiscountPercent / 100);
        assertEquals(expected, discountedAmount, 0.01,
                "Discounted amount mismatch");
    }

    static Stream<Arguments> discountDataProvider() {
        return Stream.of(
                Arguments.of(10000, false, false, 5),
                Arguments.of(25000, false, false, 10),
                Arguments.of(25000, true, false, 15),
                Arguments.of(25000, true, true, 20),
                Arguments.of(50000, true, true, 25) // cap applied
        );
    }

    @Test
    @DisplayName("Should calculate GST after discount")
    void shouldCalculateGSTAfterDiscount() {

        // Arrange
        double discountedAmount = 1000;

        // Act
        double gst = pricingService.calculateGST(discountedAmount);

        // Assert
        assertEquals(180, gst, 0.01,
                "GST must be 18% of discounted amount");
    }
}
