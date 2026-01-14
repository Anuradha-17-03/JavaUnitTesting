package com.inc.OrderProcessing.ComplianceEngine.service;

import com.company.order.exception.OrderNotFoundException;
import com.company.order.model.Order;
import com.company.order.model.OrderStatus;
import com.company.order.repository.OrderRepository;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("OrderServiceImpl unit tests")
@ExtendWith(MockitoExtension.class)
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
    @DisplayName("should create order successfully when all rules pass")
    void shouldCreateOrderSuccessfully() {
        // Arrange
        Order order = TestDataFactory.validOrder();

        when(pricingService.calculateDiscountPercentage(order)).thenReturn(10.0);
        when(pricingService.calculateGst(anyDouble())).thenReturn(180.0);
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        // Act
        Order createdOrder = orderService.createOrder(order);

        // Assert
        assertNotNull(createdOrder, "Created order should not be null");
        assertEquals(
                OrderStatus.CREATED,
                createdOrder.getStatus(),
                "Order status should be CREATED"
        );

        verify(complianceService).validate(order);
        verify(pricingService).calculateDiscountPercentage(order);
        verify(orderRepository).save(order);
    }

    @Test
    @DisplayName("should cancel order in CREATED state")
    void shouldCancelOrderSuccessfully() {
        // Arrange
        Order order = TestDataFactory.createdOrder();

        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(order));

        // Act
        orderService.cancelOrder(1L, "Customer changed mind");

        // Assert
        assertEquals(
                OrderStatus.CANCELLED,
                order.getStatus(),
                "Order status should be CANCELLED"
        );

        verify(orderRepository).save(order);
    }

    @Test
    @DisplayName("should throw exception when cancelling non-existing order")
    void shouldThrowExceptionWhenOrderNotFound() {
        // Arrange
        when(orderRepository.findById(99L))
                .thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(
                OrderNotFoundException.class,
                () -> orderService.cancelOrder(99L, "Invalid order"),
                "Expected OrderNotFoundException"
        );
    }
}
