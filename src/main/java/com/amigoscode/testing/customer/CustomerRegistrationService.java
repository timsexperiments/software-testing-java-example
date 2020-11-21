package com.amigoscode.testing.customer;

import com.amigoscode.testing.exception.DuplicateCustomerException;
import com.amigoscode.testing.utils.PhoneNumberValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class CustomerRegistrationService {

    private final CustomerRepository customerRepository;
    private final PhoneNumberValidator phoneNumberValidator;

    @Autowired
    public CustomerRegistrationService(CustomerRepository customerRepository, PhoneNumberValidator phoneNumberValidator) {
        this.customerRepository = customerRepository;
        this.phoneNumberValidator = phoneNumberValidator;
    }

    public UUID registerCustomer(CustomerRegistrationRequest customerRegistrationRequest) {
        String phoneNumber = customerRegistrationRequest.getCustomer().getPhoneNumber();

        // TODO: Validate that phone number is valid
        if (!phoneNumberValidator.test(phoneNumber)) {
            throw new IllegalStateException(String.format("Phone number [%s] is not valid", phoneNumber));
        }

        Optional<Customer> optionalCustomer = customerRepository.findByPhoneNumber(phoneNumber);
        if (optionalCustomer.isPresent()) {
            Customer customer = optionalCustomer.get();
            if (customer.getName().equals(customerRegistrationRequest.getCustomer().getName())) {
                return customer.getId();
            }

            throw new DuplicateCustomerException(String.format("Phone number %s is already taken by another user", phoneNumber));
        }

        if (customerRegistrationRequest.getCustomer().getId() == null) {
            customerRegistrationRequest.getCustomer().setId(UUID.randomUUID());
        }

        Customer savedCustomer = customerRepository.save(customerRegistrationRequest.getCustomer());
        return savedCustomer.getId();
    }
}
