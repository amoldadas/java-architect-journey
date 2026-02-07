package com.softwarearchitect.orderplatform.modules.ordering.application.port;

import com.softwarearchitect.orderplatform.modules.ordering.domain.Order;
import com.softwarearchitect.orderplatform.shared.domain.ids.OrderId;

import java.util.Optional;

public interface OrderRepository {
    void save(Order order);
    Optional<Order> findById(OrderId id);
}
