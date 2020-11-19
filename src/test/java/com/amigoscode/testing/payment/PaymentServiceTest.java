package com.amigoscode.testing.payment;

import com.amigoscode.testing.customer.Customer;
import com.amigoscode.testing.customer.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

class PaymentServiceTest {
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private CardPaymentCharger cardPaymentCharger;

    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        paymentService = new PaymentService(customerRepository, paymentRepository, cardPaymentCharger);
    }

    @Test
    void itShouldChargeCardSuccesfully() {
        // Given
        UUID customerId = UUID.randomUUID();
        // ... Mock findCustomerById
        given(customerRepository.findById(customerId)).willReturn(Optional.of(mock(Customer.class)));

        // ... Make payment request
        PaymentRequest paymentRequest = new PaymentRequest(
                new Payment(
                        null,
                        null,
                        BigDecimal.valueOf(150.32),
                        Currency.USD,
                        "4993-4039-3996-0594",
                        "Membership"
                )
        );

        // ... Card is charged succesfully
        given(cardPaymentCharger.chargeCard(
           paymentRequest.getPayment().getSource(),
           paymentRequest.getPayment().getAmount(),
           paymentRequest.getPayment().getCurrency(),
           paymentRequest.getPayment().getDescription()
        ));

        // When
        paymentService.chargeCard(customerId, paymentRequest);

        // Then
        ArgumentCaptor<Payment> paymentArgumentCaptor = ArgumentCaptor.forClass(Payment.class);
        then(paymentRepository).should().save(paymentArgumentCaptor.capture());

        Payment paymentArgumentCaptorValue = paymentArgumentCaptor.getValue();
        assertThat(paymentArgumentCaptorValue)
                .isEqualToIgnoringGivenFields(paymentRequest.getPayment(), "customerId");

        assertThat(paymentArgumentCaptorValue.getCustomerId()).isEqualTo(customerId);
    }
}