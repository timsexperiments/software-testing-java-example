package com.amigoscode.testing.customer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

@DataJpaTest(properties = {
        "spring.jpa.properties.javax.persistence.validation.mode=none"
})
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void itShouldSelectCustomerByPhoneNumber() {
        // Given
        String phoneNumber = "8002";

        // When
        Optional<Customer> optionalCustomer = customerRepository.findByPhoneNumber(phoneNumber);

        // Then
        assertTrue(!optionalCustomer.isPresent());
    }

    @Test
    void itShouldSaveCustomer() {
        // Given
        UUID id = UUID.randomUUID();
        Customer customer = new Customer(id, "John Snow", "+55 (555) 555-5555");

        // When
        customerRepository.save(customer);

        // Then
        Optional<Customer> optionalCustomer = customerRepository.findById(id);
        assertThat(optionalCustomer.isPresent() && customer.equals(optionalCustomer.get()));
    }

    @Test
    void itShouldNotSaveWhenNameIsNull() {
        // Given
        UUID id = UUID.randomUUID();
        Customer customer = new Customer(id, null, "80000");

        // When
        // Then
        assertThrows(DataIntegrityViolationException.class, () -> customerRepository.save(customer));
    }

    @Test
    void itShouldNotSaveWhenPhoneNumberIsNull() {
        // Given
        UUID id = UUID.randomUUID();
        Customer customer = new Customer(id, null, null);

        // When
        // Then
        assertThatThrownBy(() -> customerRepository.save(customer))
                .hasSameClassAs(DataIntegrityViolationException.class);
    }
}