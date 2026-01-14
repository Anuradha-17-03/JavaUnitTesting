package com.inc.OrderProcessing.ComplianceEngine.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.inc.OrderProcessing.ComplianceEngine.exception.InvalidOrderException;
import com.inc.OrderProcessing.ComplianceEngine.model.Order;
import com.inc.OrderProcessing.ComplianceEngine.model.OrderStatus;
import com.inc.OrderProcessing.ComplianceEngine.repository.OrderRepository;
import com.inc.OrderProcessing.ComplianceEngine.testData.OrderTestData;

@ExtendWith(MockitoExtension.class)
@DisplayName("Order Service Tests")
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private PricingService pricingService;

    @Mock
    private ComplianceService complianceService;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    @DisplayName("Should create order when all rules satisfied")
    void shouldCreateOrderSuccessfully() {

        // Arrange
        Order order = OrderTestData.validOrder();
        when(pricingService.applyDiscount(anyDouble(), any(), anyBoolean()))
                .thenReturn(9000.0);
        when(pricingService.calculateGST(anyDouble()))
                .thenReturn(1620.0);
        when(orderRepository.save(any(Order.class)))
                .thenReturn(order);

        // Act
        Order created = orderService.createOrder(order);

        // Assert
        assertNotNull(created, "Created order must not be null");
        assertEquals(OrderStatus.CREATED, created.getStatus(),
                "Order status must be CREATED");

        verify(complianceService).validate(order);
        verify(orderRepository).save(order);
    }

    @Test
    @DisplayName("Should cancel order in CREATED state")
    void shouldCancelOrder() {

        // Arrange
        Order order = OrderTestData.createdOrder();
        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(order));

        // Act
        orderService.cancelOrder(1L, "Customer request");

        // Assert
        assertEquals(OrderStatus.CANCELLED, order.getStatus(),
                "Order must be cancelled");
        verify(orderRepository).save(order);
    }

    @Test
    @DisplayName("Should fail cancelling already cancelled order")
    void shouldFailCancellingAgain() {

        // Arrange
        Order order = OrderTestData.cancelledOrder();
        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(order));

        // Act & Assert
        assertThrows(InvalidOrderException.class,
                () -> orderService.cancelOrder(1L, "Retry"),
                "Cancelled order must not be cancelled again");
    }
}
