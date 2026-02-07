package com.softwarearchitect.orderplatform.modules.ordering.infrastructure.persistence;

import com.softwarearchitect.orderplatform.modules.ordering.application.port.OrderRepository;
import com.softwarearchitect.orderplatform.modules.ordering.domain.Order;
import com.softwarearchitect.orderplatform.shared.domain.ids.OrderId;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public class OrderRepositoryAdapter implements OrderRepository {

    private final OrderJpaRepository jpa;
    private final OrderPersistenceMapper mapper = new OrderPersistenceMapper();

    public OrderRepositoryAdapter(OrderJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    @Transactional
    public void save(Order order) {
        OrderEntity entity = mapper.toEntity(order);
        jpa.save(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Order> findById(OrderId id) {
        return jpa.findById(id.value()).map(mapper::toDomain);
    }
}
