package com.amigoscode.testing.payment.stripe;

import com.amigoscode.testing.payment.CardPaymentCharge;
import com.amigoscode.testing.payment.Currency;
import com.stripe.model.Charge;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;

public class MockStripeServiceTest {
    private MockStripeService mockStripeService;

    @BeforeEach
    void setUp() {
        mockStripeService = new MockStripeService();
    }

    @Test
    void itShouldChargeCard() {
        // Given
        String cardSource = "4920 0394 0394 5899";
        BigDecimal amount = BigDecimal.valueOf(100);
        Currency currency = Currency.CAD;
        String description = "A very expensive charge";

        // When
        CardPaymentCharge result = mockStripeService.chargeCard(cardSource, amount, currency, description);

        // Then
        assertThat(result).isEqualTo(new CardPaymentCharge(true));
    }
}
