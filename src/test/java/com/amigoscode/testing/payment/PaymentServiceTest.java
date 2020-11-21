package com.amigoscode.testing.payment;

import com.amigoscode.testing.customer.Customer;
import com.amigoscode.testing.customer.CustomerRepository;
import com.amigoscode.testing.exception.ResourceNotFoundException;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;

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
    void itShouldChargeCardSuccessfully() {
        // Given
        UUID customerId = UUID.randomUUID();

        // ... Make payment request
        PaymentRequest paymentRequest = new PaymentRequest(
                new Payment(
                        null,
                        null,
                        BigDecimal.valueOf(150.32),
                        Currency.USD,
                        "4993 4039 3996 0594",
                        "Membership"
                )
        );

        // ... Mock findCustomerById
        given(customerRepository.findById(customerId)).willReturn(Optional.of(mock(Customer.class)));

        // ... Card is charged successfully
        given(cardPaymentCharger.chargeCard(
           paymentRequest.getPayment().getSource(),
           paymentRequest.getPayment().getAmount(),
           paymentRequest.getPayment().getCurrency(),
           paymentRequest.getPayment().getDescription()
        )).willReturn(new CardPaymentCharge(true));

        // .. Payment is saved
        given(paymentRepository.save(paymentRequest.getPayment())).willReturn(paymentRequest.getPayment());

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

    @Test
    void itShouldThrowWhenCustomerIsNotFound() {
        // Given
        UUID customerId = UUID.randomUUID();

        // ... Mock findCustomerById
        given(customerRepository.findById(customerId)).willReturn(Optional.empty());

        // When
        // Then
        then(cardPaymentCharger).shouldHaveNoInteractions();

        assertThatThrownBy(() -> paymentService.chargeCard(customerId, new PaymentRequest(new Payment())))
                .hasMessageContaining(String.format("Customer with the ID [%s] does not exist", customerId))
                .isInstanceOf(ResourceNotFoundException.class);

        then(paymentRepository).should(never()).save(any(Payment.class));
    }

    @Test
    void itShouldThrowWhenCurrencyIsNotSupported() {
        // Given
        UUID customerId = UUID.randomUUID();

        // ... Make payment request
        PaymentRequest paymentRequest = new PaymentRequest(
                new Payment(
                        null,
                        null,
                        BigDecimal.valueOf(150.32),
                        Currency.CAD,
                        "4993-4039-3996-0594",
                        "Membership"
                )
        );

        // ... Mock findCustomerById
        given(customerRepository.findById(customerId)).willReturn(Optional.of(mock(Customer.class)));

        // When
        // Then
        then(cardPaymentCharger).shouldHaveNoInteractions();

        assertThatThrownBy(() -> paymentService.chargeCard(customerId, paymentRequest))
                .hasMessageContaining(String.format("The currency [%s] is not supported", paymentRequest.getPayment().getCurrency()))
                .isInstanceOf(IllegalStateException.class);

        then(paymentRepository).should(never()).save(any(Payment.class));
    }

    @Test
    void itShouldThrowWhenCardIsNotDebited() {
        // Given
        UUID customerId = UUID.randomUUID();

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

        // ... Mock findCustomerById
        given(customerRepository.findById(customerId)).willReturn(Optional.of(mock(Customer.class)));

        // ... Card is charged unsuccessfully
        given(cardPaymentCharger.chargeCard(
                paymentRequest.getPayment().getSource(),
                paymentRequest.getPayment().getAmount(),
                paymentRequest.getPayment().getCurrency(),
                paymentRequest.getPayment().getDescription()
        )).willReturn(new CardPaymentCharge(false));

        // When
        // Then
        assertThatThrownBy(() -> paymentService.chargeCard(customerId, paymentRequest))
                .hasMessageContaining(String.format("There was an issue processing the card [%s]", paymentRequest.getPayment().getSource()))
                .isInstanceOf(IllegalStateException.class);

        then(paymentRepository).should(never()).save(any(Payment.class));
    }
}
