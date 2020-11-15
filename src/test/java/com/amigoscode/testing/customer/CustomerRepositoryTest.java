package com.amigoscode.testing.customer;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CustomerRepositoryTest {

    private CustomerRepository customerRepository;

    @Test
    void itShouldSelectCustomerByPhoneNumber() {
        // Given
        // When
        // Then
    }
}