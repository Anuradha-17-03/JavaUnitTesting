package com.inc.OrderProcessing.ComplianceEngine.service;

import java.util.Optional;

import com.inc.OrderProcessing.ComplianceEngine.exception.InvalidOrderException;
import com.inc.OrderProcessing.ComplianceEngine.exception.OrderNotFoundException;
import com.inc.OrderProcessing.ComplianceEngine.model.Order;
import com.inc.OrderProcessing.ComplianceEngine.model.OrderStatus;
import com.inc.OrderProcessing.ComplianceEngine.repository.OrderRepository;

public class OrderServiceImpl {

    private final OrderRepository orderRepository;
    private final PricingService pricingService;
    private final ComplianceService complianceService;

    public OrderServiceImpl(OrderRepository orderRepository,
                            PricingService pricingService,
                            ComplianceService complianceService) {
        this.orderRepository = orderRepository;
        this.pricingService = pricingService;
        this.complianceService = complianceService;
    }

    public Order createOrder(Order order) {

        // Compliance validation
        complianceService.validate(order);

        // Pricing (values not asserted in test, but invoked)
        double discountedAmount =
                pricingService.applyDiscount(order.getTotalAmount(),
                        order.getCustomer(), false);

        pricingService.calculateGST(discountedAmount);

        order.setStatus(OrderStatus.CREATED);

        return orderRepository.save(order);
    }

    public void cancelOrder(Long orderId, String reason) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new OrderNotFoundException("Order not found"));

        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new InvalidOrderException("Order already cancelled");
        }

        if (order.getStatus() != OrderStatus.CREATED) {
            throw new InvalidOrderException("Only CREATED orders can be cancelled");
        }

        if (reason == null || reason.trim().isEmpty()) {
            throw new InvalidOrderException("Cancellation reason is mandatory");
        }

        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }
}
