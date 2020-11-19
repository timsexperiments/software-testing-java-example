package com.amigoscode.testing.payment;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest(properties = {
        "spring.jpa.properties.javax.persistence.validation.mode=none"
})
public class PaymentRepositoryTest {
    @Autowired
    private PaymentRepository paymentRepository;

    @Test
    void itShouldInsertPayment() {
        // Given
        Long id = 1L;
        Payment payment = new Payment(
                id,
                UUID.randomUUID(),
                BigDecimal.valueOf(10.00),
                Currency.EURO,
                "card123",
                "Donation");

        // When
        paymentRepository.save(payment);

        // Then
        Optional<Payment> optionalPayment = paymentRepository.findById(id);
        assertThat(optionalPayment).isPresent().get().isEqualTo(payment);
    }
}
