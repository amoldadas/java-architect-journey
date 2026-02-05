package com.softwarearchitect.orderplatform.shared.domain.ids;

import java.util.Objects;
import java.util.UUID;

public record ProductId(UUID value) {

    public ProductId {
        Objects.requireNonNull(value, "ProductId is required");
    }

    public static ProductId newId() {
        return new ProductId(UUID.randomUUID());
    }
}

