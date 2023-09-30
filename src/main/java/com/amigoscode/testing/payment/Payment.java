package com.amigoscode.testing.payment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Entity
@JsonIgnoreProperties(allowGetters = true)
public class Payment {
    @Id
    @GeneratedValue
    private Long id;

    private UUID customerId;

    private BigDecimal amount;

    private Currency currency;

    private String source;

    private String description;

    public Payment() {
    }

    public Payment(Long id, UUID customerId, BigDecimal amount, Currency currency, String source, String description) {
        this.id = id;
        this.customerId = customerId;
        this.amount = amount;
        this.currency = currency;
        this.source = source;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "paymentId=" + id +
                ", customerId=" + customerId +
                ", amount=" + amount +
                ", currency=" + currency +
                ", source='" + source + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Payment)) return false;
        Payment payment = (Payment) o;
        return Objects.equals(getId(), payment.getId()) &&
                Objects.equals(getCustomerId(), payment.getCustomerId()) &&
                Objects.equals(getAmount(), payment.getAmount()) &&
                getCurrency() == payment.getCurrency() &&
                Objects.equals(getSource(), payment.getSource()) &&
                Objects.equals(getDescription(), payment.getDescription());
    }
}
