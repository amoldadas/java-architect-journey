package com.softwarearchitect.orderplatform.shared.domain.ids;

import java.util.Objects;
import java.util.UUID;

public record OrderId(UUID value) {

    public OrderId {
        Objects.requireNonNull(value, "OrderId is required");
    }

    public static OrderId newId() {
        return new OrderId(UUID.randomUUID());
    }
}

