package com.amigoscode.testing.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Service;

import java.util.Objects;

public class PaymentRequest {
    private final Payment payment;

    public PaymentRequest(@JsonProperty Payment payment) {
        this.payment = payment;
    }

    public Payment getPayment() {
        return payment;
    }

    @Override
    public String toString() {
        return "PaymentService{" +
                "payment=" + payment +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PaymentRequest)) return false;
        PaymentRequest that = (PaymentRequest) o;
        return Objects.equals(getPayment(), that.getPayment());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPayment());
    }
}
