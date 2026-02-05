package com.softwarearchitect.orderplatform.shared.domain.ids;

import java.util.Objects;
import java.util.UUID;

public record PaymentAttemptId(UUID value) {

    public PaymentAttemptId {
        Objects.requireNonNull(value, "PaymentAttemptId is required");
    }

    public static PaymentAttemptId newId() {
        return new PaymentAttemptId(UUID.randomUUID());
    }
}

