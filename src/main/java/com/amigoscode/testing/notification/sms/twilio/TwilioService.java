package com.amigoscode.testing.notification.sms.twilio;

import com.amigoscode.testing.notification.sms.SMSSender;
import com.amigoscode.testing.property.Properties;
import com.twilio.Twilio;
import com.twilio.base.ResourceSet;
import com.twilio.exception.ApiException;
import com.twilio.rest.api.v2010.account.IncomingPhoneNumber;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.net.URI;

@Service
@ConditionalOnProperty(value = "twilio.enabled", havingValue = "true")
public class TwilioService implements SMSSender {
    private final PhoneNumber sendPhoneNumber;

    public TwilioService(Properties properties) {
        Twilio.init(properties.getTwilio().getAccoundSid(), properties.getTwilio().getToken());
        PhoneNumber phoneNumber = null;
        try {
            ResourceSet<IncomingPhoneNumber> incomingPhoneNumbers = IncomingPhoneNumber.reader().read();
            for(IncomingPhoneNumber incomingPhoneNumber : incomingPhoneNumbers) {
                phoneNumber = incomingPhoneNumber.getPhoneNumber();
                break;
            }
        } catch (ApiException e) {
            System.out.println(e);
        } finally {
            this.sendPhoneNumber = phoneNumber;
        }
    }

    public Message send(String body, String toAddress, boolean send) {
        PhoneNumber toPhoneNumber = new PhoneNumber(toAddress);

        Message message = Message
                .creator(toPhoneNumber, sendPhoneNumber, body)
                .create();

        return message;
    }

    @Override
    public void sendFromMe(String body, String toPhoneNumber) {

    }

    @Override
    public void send(String body, String toAddress) {
        PhoneNumber toPhoneNumber = new PhoneNumber(toAddress);

        Message message = Message
                .creator(toPhoneNumber, sendPhoneNumber, body)
                .setStatusCallback(URI.create("/api/twilio/status"))
                .create();
    }

    public PhoneNumber getSendPhoneNumber() {
        return sendPhoneNumber;
    }
}
