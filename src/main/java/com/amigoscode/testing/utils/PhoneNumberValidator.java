package com.amigoscode.testing.utils;

import org.springframework.stereotype.Service;

import java.util.function.Predicate;

@Service
public class PhoneNumberValidator implements Predicate<String> {
    public boolean test(String phoneNumber) {
        String patterns
                = "^(\\+?\\d{1,3}[- .]?+)?((\\(\\d{3}\\))|\\d{3})[- .]?+\\d{3}[- .]?+\\d{4}$"
                + "|^(\\+?\\d{1,3}[- .]?+)?(\\d{3}[- .]?+){2}\\d{3}$"
                + "|^(\\+?\\d{1,3}[- .]?+)?(\\d{3}[- .]?+)(\\d{2}[- .]?+){2}\\d{2}$";


        return phoneNumber.replaceAll("\\s", "").matches(patterns);
    }
}
