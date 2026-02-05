package com.softwarearchitect.orderplatform.modules.ordering.domain;

import com.softwarearchitect.orderplatform.shared.domain.DomainException;
import com.softwarearchitect.orderplatform.shared.domain.Money;
import com.softwarearchitect.orderplatform.shared.domain.ids.OrderId;
import com.softwarearchitect.orderplatform.shared.domain.ids.ProductId;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class OrderStateMachineTest {

    @Test
    void created_to_paymentPending_to_paid_happyPath() {
        Order order = Order.createNew(OrderId.newId(), "INR");
        order.addItem(new OrderItem(ProductId.newId(), "Book", Money.of(new BigDecimal("100.00"), "INR"), 2));

        order.markPaymentPending();
        assertEquals(OrderStatus.PAYMENT_PENDING, order.status());

        order.markPaid("PAY-REF-1");
        assertEquals(OrderStatus.PAID, order.status());
        assertEquals("PAY-REF-1", order.paidPaymentReference());
    }

    @Test
    void cannot_pay_when_empty() {
        Order order = Order.createNew(OrderId.newId(), "INR");
        DomainException ex = assertThrows(DomainException.class, order::markPaymentPending);
        assertTrue(ex.getMessage().toLowerCase().contains("empty"));
    }

    @Test
    void paid_order_cannot_be_cancelled() {
        Order order = Order.createNew(OrderId.newId(), "INR");
        order.addItem(new OrderItem(ProductId.newId(), "Book", Money.of(new BigDecimal("10.00"), "INR"), 1));
        order.markPaymentPending();
        order.markPaid("PAY-REF-2");

        assertThrows(DomainException.class, order::cancel);
    }
}

