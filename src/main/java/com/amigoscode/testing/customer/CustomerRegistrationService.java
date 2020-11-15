package com.amigoscode.testing.customer;

import com.amigoscode.testing.exception.DuplicateCustomerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class CustomerRegistrationService {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerRegistrationService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public UUID registerCustomer(CustomerRegistrationRequest customerRegistrationRequest) {
        String phoneNumber = customerRegistrationRequest.getCustomer().getPhoneNumber();

        // TODO: Validate that phone number is valid

        Optional<Customer> optionalCustomer = customerRepository.findByPhoneNumber(phoneNumber);
        if (optionalCustomer.isPresent()) {
            Customer customer = optionalCustomer.get();
            if (customer.getName().equals(customerRegistrationRequest.getCustomer().getName())) {
                return customer.getId();
            }

            throw new DuplicateCustomerException(String.format("Phone number {} is already in use by another user", phoneNumber));
        }

        Customer savedCustomer = customerRepository.save(customerRegistrationRequest.getCustomer());
        return savedCustomer.getId();
    }
}
