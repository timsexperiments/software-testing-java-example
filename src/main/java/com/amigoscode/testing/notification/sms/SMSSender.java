package com.amigoscode.testing.notification.sms;

import com.amigoscode.testing.notification.NotificationSender;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public interface SMSSender extends NotificationSender {
    Message send(String body, String toPhoneNumber, boolean something);
    void sendFromMe(String body, String toPhoneNumber);
    PhoneNumber getSendPhoneNumber();
}
