package com.softwarearchitect.orderplatform.shared.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public final class Money {
    private final BigDecimal amount;
    private final String currency; // keep simple: "INR"

    private Money(BigDecimal amount, String currency) {
        if (currency == null || currency.isBlank()) throw new DomainException("Currency is required");
        if (amount == null) throw new DomainException("Amount is required");
        // normalize to 2 decimals
        this.amount = amount.setScale(2, RoundingMode.HALF_UP);
        this.currency = currency;
        if (this.amount.compareTo(BigDecimal.ZERO) < 0) throw new DomainException("Amount cannot be negative");
    }

    public static Money of(BigDecimal amount, String currency) {
        return new Money(amount, currency);
    }

    public static Money zero(String currency) {
        return new Money(BigDecimal.ZERO, currency);
    }

    public Money plus(Money other) {
        requireSameCurrency(other);
        return new Money(this.amount.add(other.amount), this.currency);
    }

    public Money times(int multiplier) {
        if (multiplier < 0) throw new DomainException("Multiplier cannot be negative");
        return new Money(this.amount.multiply(BigDecimal.valueOf(multiplier)), this.currency);
    }

    private void requireSameCurrency(Money other) {
        if (other == null) throw new DomainException("Money is required");
        if (!Objects.equals(this.currency, other.currency)) {
            throw new DomainException("Currency mismatch: " + this.currency + " vs " + other.currency);
        }
    }

    public BigDecimal amount() { return amount; }
    public String currency() { return currency; }

    @Override public String toString() { return amount + " " + currency; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Money money)) return false;
        return amount.equals(money.amount) && currency.equals(money.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, currency);
    }
}

