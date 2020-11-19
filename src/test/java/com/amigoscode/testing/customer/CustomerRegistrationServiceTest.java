package com.amigoscode.testing.customer;

import com.amigoscode.testing.exception.DuplicateCustomerException;
import org.h2.value.ValueUuid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.assertj.core.api.Assertions.*;

class CustomerRegistrationServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Captor
    private ArgumentCaptor<Customer> customerArgumentCaptor;

    private CustomerRegistrationService customerRegistrationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        customerRegistrationService = new CustomerRegistrationService(customerRepository);
    }

    @Test
    void itShouldSaveNewCustomer() {
        // Given
        String phoneNumber = "+32 (590) 650-9960";
        Customer customer = new Customer(UUID.randomUUID(), "Dany Targarian", phoneNumber);

        CustomerRegistrationRequest customerRegistrationRequest = new CustomerRegistrationRequest(customer);

        given(customerRepository.findByPhoneNumber(phoneNumber))
                .willReturn(Optional.empty());
        given(customerRepository.save(customer))
                .willReturn(customer);

        // When
        customerRegistrationService.registerCustomer(customerRegistrationRequest);

        // Then
        then(customerRepository).should().save(customerArgumentCaptor.capture());
        Customer customerArgumentValue = customerArgumentCaptor.getValue();
        assertTrue(customerArgumentValue.equals(customer));
    }

    @Test
    void itShouldSaveNewCustomerWhenIdIsNull() {
        // Given
        String phoneNumber = "+32 (590) 650-9960";
        Customer customer = new Customer(null, "Dany Targarian", phoneNumber);

        CustomerRegistrationRequest customerRegistrationRequest = new CustomerRegistrationRequest(customer);

        given(customerRepository.findByPhoneNumber(phoneNumber))
                .willReturn(Optional.empty());
        given(customerRepository.save(customer))
                .willReturn(customer);

        // When
        customerRegistrationService.registerCustomer(customerRegistrationRequest);

        // Then
        then(customerRepository).should().save(customerArgumentCaptor.capture());
        Customer customerArgumentValue = customerArgumentCaptor.getValue();
        assertThat(customerArgumentValue).isEqualToIgnoringGivenFields(customer, "id");
        assertThat(customerArgumentValue.getId()).isNotNull();
    }

    @Test
    void itShouldNotSaveWhenCustomerExists() {
        // Given
        String phoneNumber = "+32 (590) 650-9960";
        Customer customer = new Customer(UUID.randomUUID(), "Rob Stark", phoneNumber);

        CustomerRegistrationRequest customerRegistrationRequest = new CustomerRegistrationRequest(customer);

        given(customerRepository.findByPhoneNumber(phoneNumber))
                .willReturn(Optional.of(customer));

        // When
        customerRegistrationService.registerCustomer(customerRegistrationRequest);

        // Then
        then(customerRepository).should(never()).save(any());
//        then(customerRepository).should().findByPhoneNumber(phoneNumber);
//        then(customerRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void itShouldNotSaveWhenPhoneNumberIsInUse() {
        // Given
        String phoneNumber = "+32 (590) 650-9960";
        Customer customer = new Customer(UUID.randomUUID(), "Sansa Stark", phoneNumber);
        CustomerRegistrationRequest customerRegistrationRequest = new CustomerRegistrationRequest(customer);

        Customer customer2 = new Customer(UUID.randomUUID(), "Tyrion Lannister", phoneNumber);

        given(customerRepository.findByPhoneNumber(phoneNumber))
                .willReturn(Optional.of(customer2));

        // When
        assertThatThrownBy(() -> customerRegistrationService.registerCustomer(customerRegistrationRequest))
                .hasMessageContaining(String.format("Phone number %s is already taken by another user", phoneNumber))
                .isInstanceOf(DuplicateCustomerException.class);

        // Then
        then(customerRepository)
                .should(never())
                .save(any(Customer.class));
    }
}