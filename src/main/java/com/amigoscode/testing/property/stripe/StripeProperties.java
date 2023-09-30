package com.amigoscode.testing.property.stripe;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties("stripe.api")
public class StripeProperties {
    private final String key;

    public StripeProperties(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
