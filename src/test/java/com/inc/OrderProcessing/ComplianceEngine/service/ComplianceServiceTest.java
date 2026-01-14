package com.inc.OrderProcessing.ComplianceEngine.service;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ComplianceService unit tests")
class ComplianceServiceTest {

    private ComplianceService complianceService;

    @BeforeEach
    void setUp() {
        complianceService = new ComplianceService();
    }

    @Test
    @DisplayName("should fail validation when customer is inactive")
    void shouldFailWhenCustomerInactive() {
        // Arrange
        Order order = TestDataFactory.validOrder();
        order.getCustomer().setActive(false);

        // Act + Assert
        ComplianceViolationException exception = assertThrows(
                ComplianceViolationException.class,
                () -> complianceService.validate(order),
                "Expected compliance violation for inactive customer"
        );

        assertTrue(
                exception.getMessage().contains("inactive"),
                "Error message should mention inactive customer"
        );
    }

    @Test
    @DisplayName("should fail validation when order has no items")
    void shouldFailWhenNoItems() {
        // Arrange
        Order order = TestDataFactory.validOrder();
        order.getItems().clear();

        // Act + Assert
        assertThrows(
                ComplianceViolationException.class,
                () -> complianceService.validate(order),
                "Order without items should not pass compliance"
        );
    }

    @Test
    @DisplayName("should pass validation for valid order")
    void shouldPassForValidOrder() {
        // Arrange
        Order order = TestDataFactory.validOrder();

        // Act + Assert
        assertDoesNotThrow(
                () -> complianceService.validate(order),
                "Valid order should pass compliance"
        );
    }
}
