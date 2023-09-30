package com.amigoscode.testing.notification.sms.twilio;

import com.amigoscode.testing.model.MessageRequestDto;
import com.amigoscode.testing.notification.sms.SMSService;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/twilio")
public class TwilioTestingController {
    private final SMSService smsService;

    @Autowired
    public TwilioTestingController(SMSService smsService) {
        this.smsService = smsService;
    }

    @GetMapping("/phone-number")
    public PhoneNumber getSendPhoneNumber() {
        System.out.println(smsService);
        System.out.println(smsService.getSmsSender().getSendPhoneNumber().toString());
        return smsService.getSmsSender().getSendPhoneNumber();
    }

    @PostMapping("/{toPhoneNumber}")
    public Message sendMessage(@PathVariable String toPhoneNumber,
                               @RequestBody MessageRequestDto messageRequestDto) {
        Message message = smsService.getSmsSender().send(messageRequestDto.body, toPhoneNumber, false);
        System.out.println(message);
        System.out.println(message.getStatus());
        System.out.println(message.getStatus().toString());
        return message;
    }
}
