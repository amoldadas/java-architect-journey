package com.softwarearchitect.orderplatform.modules.ordering.domain;

import com.softwarearchitect.orderplatform.shared.domain.DomainException;
import com.softwarearchitect.orderplatform.shared.domain.Money;
import com.softwarearchitect.orderplatform.shared.domain.ids.ProductId;

import java.util.Objects;

public final class OrderItem {

    private final ProductId productId;
    private final String productNameSnapshot;
    private final Money unitPriceSnapshot;
    private final int quantity;

    public OrderItem(ProductId productId, String productNameSnapshot, Money unitPriceSnapshot, int quantity) {
        this.productId = Objects.requireNonNull(productId, "productId required");
        if (productNameSnapshot == null || productNameSnapshot.isBlank()) {
            throw new DomainException("Product name snapshot required");
        }
        this.productNameSnapshot = productNameSnapshot;
        this.unitPriceSnapshot = Objects.requireNonNull(unitPriceSnapshot, "unitPriceSnapshot required");
        if (quantity <= 0) {
            throw new DomainException("Quantity must be > 0");
        }
        this.quantity = quantity;
    }

    public Money lineTotal() {
        return unitPriceSnapshot.times(quantity);
    }

    public ProductId productId() { return productId; }
    public String productNameSnapshot() { return productNameSnapshot; }
    public Money unitPriceSnapshot() { return unitPriceSnapshot; }
    public int quantity() { return quantity; }
}

