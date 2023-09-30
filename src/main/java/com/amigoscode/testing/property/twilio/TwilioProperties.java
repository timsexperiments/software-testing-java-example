package com.amigoscode.testing.property.twilio;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties("twilio.api")
public class TwilioProperties {
    private final String sid;
    private final String token;

    public TwilioProperties(String accountSid, String token) {
        this.sid = accountSid;
        this.token = token;
    }

    public String getAccoundSid() {
        return sid;
    }

    public String getToken() {
        return token;
    }
}
