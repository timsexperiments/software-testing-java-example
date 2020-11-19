package com.amigoscode.testing.payment;

import java.util.Objects;

public class CardPaymentCharge {
    private final boolean wasCardDebited;

    public CardPaymentCharge(boolean wasCardDebited) {
        this.wasCardDebited = wasCardDebited;
    }

    public boolean wasCardDebited() {
        return wasCardDebited;
    }

    @Override
    public String toString() {
        return "CardPaymentCharge{" +
                "wasCardDebited=" + wasCardDebited +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CardPaymentCharge)) return false;
        CardPaymentCharge that = (CardPaymentCharge) o;
        return wasCardDebited == that.wasCardDebited;
    }

    @Override
    public int hashCode() {
        return Objects.hash(wasCardDebited);
    }
}
