package com.softwarearchitect.orderplatform.modules.ordering.domain;

import com.softwarearchitect.orderplatform.shared.domain.DomainException;
import com.softwarearchitect.orderplatform.shared.domain.Money;
import com.softwarearchitect.orderplatform.shared.domain.ids.OrderId;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Order {

    private final OrderId id;
    private final Instant createdAt;
    private OrderStatus status;
    private final List<OrderItem> items = new ArrayList<>();
    private final String currency;

    // For idempotency/audit later (Week 9+), keep fields ready but unused now
    private String paidPaymentReference;

    private Order(OrderId id, String currency) {
        this.id = Objects.requireNonNull(id, "OrderId required");
        if (currency == null || currency.isBlank()) {
            throw new DomainException("Currency required");
        }
        this.currency = currency;
        this.createdAt = Instant.now();
        this.status = OrderStatus.CREATED;
    }

    public static Order createNew(OrderId id, String currency) {
        return new Order(id, currency);
    }

    public void addItem(OrderItem item) {
        requireNotFinalized();
        Objects.requireNonNull(item, "item required");
        if (!Objects.equals(currency, item.unitPriceSnapshot().currency())) {
            throw new DomainException("Order currency mismatch with item currency");
        }
        items.add(item);
    }

    public Money total() {
        Money sum = Money.zero(currency);
        for (OrderItem i : items) {
            sum = sum.plus(i.lineTotal());
        }
        return sum;
    }

    public void markPaymentPending() {
        if (status != OrderStatus.CREATED && status != OrderStatus.PAYMENT_FAILED) {
            throw new DomainException("Cannot mark payment pending from status " + status);
        }
        if (items.isEmpty()) {
            throw new DomainException("Cannot proceed to payment with empty order");
        }
        status = OrderStatus.PAYMENT_PENDING;
    }

    public void markPaid(String paymentReference) {
        if (status != OrderStatus.PAYMENT_PENDING) {
            throw new DomainException("Cannot mark paid from status " + status);
        }
        if (paymentReference == null || paymentReference.isBlank()) {
            throw new DomainException("Payment reference required");
        }
        this.paidPaymentReference = paymentReference;
        status = OrderStatus.PAID;
    }

    public void markPaymentFailed(String reason) {
        if (status != OrderStatus.PAYMENT_PENDING) {
            throw new DomainException("Cannot mark payment failed from status " + status);
        }
        if (reason == null || reason.isBlank()) {
            throw new DomainException("Failure reason required");
        }
        status = OrderStatus.PAYMENT_FAILED;
    }

    public void cancel() {
        if (status == OrderStatus.PAID) {
            throw new DomainException("Paid order cannot be cancelled");
        }
        if (status == OrderStatus.CANCELLED) {
            return; // idempotent
        }
        status = OrderStatus.CANCELLED;
    }

    private void requireNotFinalized() {
        if (status == OrderStatus.PAID || status == OrderStatus.CANCELLED) {
            throw new DomainException("Order is finalized (" + status + ")");
        }
    }

    public OrderId id() { return id; }
    public Instant createdAt() { return createdAt; }
    public OrderStatus status() { return status; }
    public List<OrderItem> items() { return List.copyOf(items); }
    public String currency() { return currency; }
    public String paidPaymentReference() { return paidPaymentReference; }
}

