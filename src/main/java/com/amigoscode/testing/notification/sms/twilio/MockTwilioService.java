package com.amigoscode.testing.notification.sms.twilio;

import com.amigoscode.testing.notification.sms.SMSSender;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(value = "twilio.enabled", havingValue = "false")
public class MockTwilioService implements SMSSender {

    public Message send(String message, String toAddress, boolean send) {
        return null;
    }

    @Override
    public void sendFromMe(String body, String toPhoneNumber) {

    }

    @Override
    public PhoneNumber getSendPhoneNumber() {
        return null;
    }

    @Override
    public void send(String message, String toAddress) {
        System.out.println("Message is sending");

    }
}
