package com.amigoscode.testing.customer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.validation.constraints.AssertTrue;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
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
        assertTrue(optionalCustomer.isPresent() && customer.equals(optionalCustomer.get()));
    }

    @Test
    void itShouldNotSaveWhenPhoneNumberIsNull() {
        // Given
        UUID id = UUID.randomUUID();
        Customer customer = new Customer(id, null, null);

        // When
        customerRepository.save(customer);

        Optional<Customer> optionalCustomer = customerRepository.findById(id);
        System.out.println(optionalCustomer.toString());

        // Then
//        assertThrows()
//        (customerRepository.save(customer))
    }
}