package com.inc.OrderProcessing.ComplianceEngine.testData;

import java.util.List;

import com.inc.OrderProcessing.ComplianceEngine.model.Customer;
import com.inc.OrderProcessing.ComplianceEngine.model.Order;
import com.inc.OrderProcessing.ComplianceEngine.model.OrderItem;
import com.inc.OrderProcessing.ComplianceEngine.model.OrderStatus;

public class OrderTestData {

    public static Order validOrder() {
        return validOrder(new Customer(true));
    }
    
    public static Order validOrder(Customer customer) {
        return new Order(
                1L,
                customer,
                List.of(new OrderItem("Laptop", 1, 10000)),
                OrderStatus.CREATED
        );
    }

    public static Order createdOrder() {
        return validOrder();
    }

    public static Order cancelledOrder() {
        Order order = validOrder();
        order.setStatus(OrderStatus.CANCELLED);
        return order;
    }
}
