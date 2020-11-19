package com.amigoscode.testing.payment;

import com.amigoscode.testing.customer.CustomerRepository;
import com.amigoscode.testing.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PaymentService {
    private final List<Currency> ACCEPTED_CURRENCIES = List.of(Currency.USD, Currency.EURO, Currency.GBP);

    private final CustomerRepository customerRepository;
    private final PaymentRepository paymentRepository;
    private final CardPaymentCharger cardPaymentCharger;

    @Autowired
    public PaymentService(CustomerRepository customerRepository, PaymentRepository paymentRepository, CardPaymentCharger cardPaymentCharger) {
        this.customerRepository = customerRepository;
        this.paymentRepository = paymentRepository;
        this.cardPaymentCharger = cardPaymentCharger;
    }

    public void chargeCard(UUID customerId, PaymentRequest paymentRequest) {
        Payment payment = paymentRequest.getPayment();

        // 1. Does customer exist
        boolean isCustomerFound = customerRepository.findById(customerId).isPresent();
        if (!isCustomerFound){
            throw new ResourceNotFoundException(String.format("Customer with the ID [%s] does not exist", customerId));
        }

        // 2. Do we support the currency
        boolean isCurrencySupported = ACCEPTED_CURRENCIES.stream()
                .anyMatch(currency -> currency.equals(payment.getCurrency()));

        if (!isCurrencySupported) {
            throw new IllegalStateException(String.format("The currency [%s] is not supported", payment.getCurrency()));
        }

        // 3. Charge card
        CardPaymentCharge cardPaymentCharge = cardPaymentCharger.chargeCard(payment.getSource(), payment.getAmount(), payment.getCurrency(), payment.getDescription());

        // 4. If not debited, throw error
        if (!cardPaymentCharge.wasCardDebited()) {
            throw new IllegalStateException(String.format("There was an issue processing the card [%s]", payment.getSource()));
        }

        // 5. Insert payment
        paymentRepository.save(payment);

        // 6. TODO: send sms
    }
}
