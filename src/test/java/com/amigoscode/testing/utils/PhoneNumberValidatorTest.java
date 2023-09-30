package com.amigoscode.testing.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

public class PhoneNumberValidatorTest {
    private PhoneNumberValidator phoneNumberValidator;

    @BeforeEach
    void setUp() {
        phoneNumberValidator = new PhoneNumberValidator();
    }

    @ParameterizedTest
    @CsvSource({
            "2055550125,TRUE", "202 555 0125,TRUE",
            "+1 (202) 555-0125,TRUE", "+111 (202) 555-0125,TRUE",
            "636 856 789,TRUE", "+111 636 856 789,TRUE",
            "636 85 67 89,TRUE", "+111 636 85 67 89,TRUE",
            "+1 800-495-0234,TRUE", "12825567890,TRUE",
            "447      423 3    4 5  669     9,TRUE", "+123-345-23-4958,TRUE",
            "+14958,FALSE", "1800 ABCD EFG STREET,FALSE",
            "+-1 800 928 4958,FALSE", "+33 (493) 432-2945-,FALSE",
    })
    void itShouldValidatePhoneNumber(String phoneNumber, boolean expected) {
        // Given
        // When
        boolean isValidPhoneNumber = phoneNumberValidator.test(phoneNumber);

        // Then
        assertThat(isValidPhoneNumber).isEqualTo(expected);
    }
}
