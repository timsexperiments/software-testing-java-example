package com.amigoscode.testing.customer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest(properties = {
        "spring.jpa.properties.javax.persistence.validation.mode=none"
})
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void itShouldNotSelectCustomerByPhoneNumberWhenNumberDoesNotExist() {
        // Given
        String phoneNumber = "8002";

        // When
        // Then
        Optional<Customer> optionalCustomer = customerRepository.findByPhoneNumber(phoneNumber);
        assertTrue(!optionalCustomer.isPresent());
    }

    @Test
    void itShouldFindCustomerByPhoneNumber() {
        // Given
        String phoneNumber = "8002";
        Customer customer = new Customer(UUID.randomUUID(), "Theon (Reek) Greyjoy", phoneNumber);

        // When
        customerRepository.save(customer);

        // Then
        Optional<Customer> optionalCustomer = customerRepository.findByPhoneNumber(phoneNumber);
        assertThat(optionalCustomer).isPresent().get().isEqualTo(customer);
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
        assertThat(optionalCustomer).isPresent().get().isEqualTo(customer);
//        assertTrue(optionalCustomer.isPresent() && customer.equals(optionalCustomer.get()));
    }

    @Test
    void itShouldShouldNotSaveWhenNameIsNull() {
        // Given
        UUID id = UUID.randomUUID();
        Customer customer = new Customer(id, null, "+1 (847) 339-4837");

        // When
        // Then
        assertThatThrownBy(() -> customerRepository.save(customer))
                .hasMessageContaining("not-null property references a null or transient value : com.amigoscode.testing.customer.Customer.name")
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void itShouldThrowWhenPhoneNumberIsNull() {
        // Given
        UUID id = UUID.randomUUID();
        Customer customer = new Customer(id, "Ser Davos", null);

        // When
        // Then
        assertThatThrownBy(() -> customerRepository.save(customer))
                .hasMessageContaining("not-null property references a null or transient value : com.amigoscode.testing.customer.Customer.phoneNumber")
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}