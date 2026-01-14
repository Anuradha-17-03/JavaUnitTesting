package com.inc.OrderProcessing.ComplianceEngine.repository;

import java.util.Optional;

import com.inc.OrderProcessing.ComplianceEngine.model.Order;

public interface OrderRepository {

    Order save(Order order);

    Optional<Order> findById(Long id);
}
