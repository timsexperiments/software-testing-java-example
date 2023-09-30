package com.amigoscode.testing.notification.sms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SMSService {
    private final SMSSender smsSender;

    @Autowired
    public SMSService(SMSSender smsSender) {
        this.smsSender = smsSender;
    }

    public SMSSender getSmsSender() {
        return smsSender;
    }
}
