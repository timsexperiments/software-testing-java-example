package com.amigoscode.testing.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CustomerRegistrationService {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerRegistrationService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public UUID registerCustomer(CustomerRegistrationRequest customerRegistrationRequest) {
        Customer savedCustomer = customerRepository.save(customerRegistrationRequest.getCustomer());
        return savedCustomer.getId();
    }
}
