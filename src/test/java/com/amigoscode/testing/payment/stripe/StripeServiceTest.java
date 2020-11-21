package com.amigoscode.testing.payment.stripe;

import com.amigoscode.testing.payment.CardPaymentCharge;
import com.amigoscode.testing.payment.Currency;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.net.RequestOptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

class StripeServiceTest {
    @Mock
    private StripeApi stripeApi;

    private StripeService stripeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        stripeService = new StripeService(stripeApi);
    }

    @Test
    void itShouldChargeCard() throws StripeException {
        // Given
        String cardSource = "0x0x0x";
        BigDecimal amount = BigDecimal.valueOf(100.23);
        Currency currency = Currency.USD;
        String description = "test";

        given(stripeApi.create(any(), any())).willReturn(mock(Charge.class));

        // When
        stripeService.chargeCard(cardSource, amount, currency, description);

        // Then
        ArgumentCaptor<Map<String, Object>> mapArgumentCaptor = ArgumentCaptor.forClass(Map.class);
        ArgumentCaptor<RequestOptions> requestOptionsArgumentCaptor = ArgumentCaptor.forClass(RequestOptions.class);

        then(stripeApi).should().create(mapArgumentCaptor.capture(), requestOptionsArgumentCaptor.capture());
        Map<String, Object> mapArgumentCaptorValue = mapArgumentCaptor.getValue();
        RequestOptions requestOptionsArgumentCaptorValue = requestOptionsArgumentCaptor.getValue();

        assertThat(mapArgumentCaptorValue.keySet()).hasSize(4);
        assertThat(mapArgumentCaptorValue.get("amount")).isEqualTo(amount);
        assertThat(mapArgumentCaptorValue.get("source")).isEqualTo(cardSource);
        assertThat(mapArgumentCaptorValue.get("currency")).isEqualTo(currency);
        assertThat(mapArgumentCaptorValue.get("description")).isEqualTo(description);

        assertThat(requestOptionsArgumentCaptorValue).isNotNull();
    }

    @Test
    void itShouldThrowWhenCreatChargeFails() throws StripeException {
        // Given
        String cardSource = "0x0x0x";
        BigDecimal amount = BigDecimal.valueOf(100.23);
        Currency currency = Currency.USD;
        String description = "test";

        given(stripeApi.create(any(), any())).willThrow(StripeException.class);

        // When
        // Then
        assertThatThrownBy(() -> stripeService.chargeCard(cardSource, amount, currency, description))
                .hasMessageContaining(String.format("Unable to charge card [%s]", cardSource))
                .isInstanceOf(IllegalStateException.class);
    }
}