package com.inc.OrderProcessing.ComplianceEngine.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.inc.OrderProcessing.ComplianceEngine.exception.ComplianceViolationException;
import com.inc.OrderProcessing.ComplianceEngine.model.Customer;
import com.inc.OrderProcessing.ComplianceEngine.model.Order;
import com.inc.OrderProcessing.ComplianceEngine.testData.OrderTestData;

@ExtendWith(MockitoExtension.class)
@DisplayName("Compliance Validation Tests")
class ComplianceServiceTest {

    private ComplianceService complianceService;

    @BeforeEach
    void setUp() {
        complianceService = new ComplianceService();
    }

    @Test
    @DisplayName("Should throw exception if customer is inactive")
    void shouldFailForInactiveCustomer() {

        // Arrange
        Customer customer = new Customer(false);
        Order order = OrderTestData.validOrder(customer);

        // Act & Assert
        ComplianceViolationException ex =
                assertThrows(ComplianceViolationException.class,
                        () -> complianceService.validate(order),
                        "Inactive customer should violate compliance");

        assertEquals("Customer is inactive", ex.getMessage());
    }

    @Test
    @DisplayName("Should pass for valid order")
    void shouldPassForValidOrder() {

        // Arrange
        Order order = OrderTestData.validOrder();

        // Act & Assert
        assertDoesNotThrow(() -> complianceService.validate(order),
                "Valid order should pass compliance");
    }
}
