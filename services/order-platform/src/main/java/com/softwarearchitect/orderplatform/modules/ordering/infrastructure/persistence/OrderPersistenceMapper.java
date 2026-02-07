package com.softwarearchitect.orderplatform.modules.ordering.infrastructure.persistence;

import com.softwarearchitect.orderplatform.modules.ordering.domain.Order;
import com.softwarearchitect.orderplatform.modules.ordering.domain.OrderItem;
import com.softwarearchitect.orderplatform.modules.ordering.domain.OrderStatus;
import com.softwarearchitect.orderplatform.shared.domain.Money;
import com.softwarearchitect.orderplatform.shared.domain.ids.OrderId;
import com.softwarearchitect.orderplatform.shared.domain.ids.ProductId;

import java.util.UUID;

public final class OrderPersistenceMapper {

    public OrderEntity toEntity(Order domain) {
        OrderEntity e = new OrderEntity();
        e.setId(domain.id().value());
        e.setStatus(domain.status().name());
        e.setCurrency(domain.currency());
        e.setCreatedAt(domain.createdAt());
        e.setPaidPaymentReference(domain.paidPaymentReference());

        // Replace items fully (simple + safe for Week 2)
        e.clearItems();
        for (OrderItem item : domain.items()) {
            OrderItemEntity ie = new OrderItemEntity();
            ie.setId(UUID.randomUUID());
            ie.setProductId(item.productId().value());
            ie.setProductNameSnapshot(item.productNameSnapshot());
            ie.setUnitPriceAmount(item.unitPriceSnapshot().amount());
            ie.setUnitPriceCurrency(item.unitPriceSnapshot().currency());
            ie.setQuantity(item.quantity());
            e.addItem(ie);
        }
        return e;
    }

    public Order toDomain(OrderEntity e) {
        var items = e.getItems().stream()
                .map(ie -> new OrderItem(
                        productIdOf(ie),
                        ie.getProductNameSnapshot(),
                        moneyOf(ie),
                        ie.getQuantity()
                ))
                .toList();
    
        Order domain = Order.rehydrate(
                new OrderId(e.getId()),
                e.getCurrency(),
                e.getCreatedAt(),
                statusOf(e),
                e.getPaidPaymentReference(),
                items
        );
    
        return domain;
    }
    

    public static Money moneyOf(OrderItemEntity ie) {
        return Money.of(ie.getUnitPriceAmount(), ie.getUnitPriceCurrency());
    }

    public static ProductId productIdOf(OrderItemEntity ie) {
        return new ProductId(ie.getProductId());
    }

    public static OrderStatus statusOf(OrderEntity e) {
        return OrderStatus.valueOf(e.getStatus());
    }
}
