package com.amigoscode.testing.payment.stripe;

import com.amigoscode.testing.payment.CardPaymentCharge;
import com.amigoscode.testing.payment.CardPaymentCharger;
import com.amigoscode.testing.payment.Currency;
import com.amigoscode.testing.property.Properties;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.net.RequestOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
@ConditionalOnProperty(value = "stripe.enabled", havingValue = "true")
public class StripeService implements CardPaymentCharger {
    private static RequestOptions requestOptions;

    private final StripeApi stripeApi;

    @Autowired
    public StripeService(StripeApi stripeApi, Properties properties) {
        this.stripeApi = stripeApi;
        requestOptions = RequestOptions.builder()
                .setApiKey(properties.getStripe().getKey())
                .build();
    }

    @Override
    public CardPaymentCharge chargeCard(String cardSource, BigDecimal amount, Currency currency, String description) {
        Map<String, Object> params = new HashMap<>();
        params.put("amount", amount);
        params.put("currency", currency);
        params.put("source", cardSource);
        params.put("description", description);

        try {
            Charge charge = stripeApi.create(params, requestOptions);
            return new CardPaymentCharge(charge.getPaid());
        } catch (StripeException e) {
            throw new IllegalStateException(String.format("Unable to charge card [%s]", cardSource), e);
        }
    }
}
