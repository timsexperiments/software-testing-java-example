package com.amigoscode.testing.property;

import com.amigoscode.testing.property.stripe.StripeProperties;
import com.amigoscode.testing.property.twilio.TwilioProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({ StripeProperties.class, TwilioProperties.class })
public class Properties {
    private final TwilioProperties twilioProperties;
    private final StripeProperties stripeProperties;

    @Autowired
    public Properties(TwilioProperties twilioProperties, StripeProperties stripeProperties) {
        this.twilioProperties = twilioProperties;
        this.stripeProperties = stripeProperties;
    }

    public StripeProperties getStripe() {
        return stripeProperties;
    }

    public TwilioProperties getTwilio() {
        return twilioProperties;
    }
}
