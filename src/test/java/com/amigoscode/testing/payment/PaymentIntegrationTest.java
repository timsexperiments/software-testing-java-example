package com.amigoscode.testing.payment;

import com.amigoscode.testing.customer.Customer;
import com.amigoscode.testing.customer.CustomerRegistrationRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PaymentIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void itShouldCreatePaymentSuccessfully() throws Exception {
        // Given
        // .. a customer
        UUID customerId = UUID.randomUUID();
        Customer customer = new Customer(customerId, "James", "(713) 685 - 7023");

        // .. register customer
        ResultActions customerRegistrationResultActions = mockMvc.perform(put("/api/customer-registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectToJson(new CustomerRegistrationRequest(customer))))
        );

        // .. payment request
        long paymentId = 1L;
        Payment payment = new Payment(
                paymentId,
                customer.getId(),
                BigDecimal.valueOf(123.45),
                Currency.GBP,
                "0000 0000 0000 0000",
                "Newest Payment"
        );

        // When
        // .. payment is sent
        ResultActions paymentResultActions = mockMvc.perform(post("/api/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectToJson(new PaymentRequest(payment))))
        );

        // Then
        // .. customer is registerd
        customerRegistrationResultActions.andExpect(status().isOk());
        // MockHttpServletResponse response = customerRegistrationResultActions.andReturn().getResponse();
        //assertThat(response.getContentAsString()).isNotNull();

        // .. payment is successfully taken
        paymentResultActions.andExpect(status().isCreated());
        // .. get paymentResponse
        MockHttpServletResponse paymentResponse = paymentResultActions.andReturn().getResponse();
        // .. compare to paymentId
        assertThat(paymentResponse.getContentAsString()).isEqualTo(payment.getId().toString());

        // .. get payment from db
        ResultActions getPaymentResultActions = mockMvc.perform(get(String.format("/api/payments/%d", payment.getId())));
        // .. check for status 200
        getPaymentResultActions.andExpect(status().isOk());
        // .. get response
        MockHttpServletResponse getPaymentResponse = getPaymentResultActions.andReturn().getResponse();
        // .. compare to payment
        assertThat(getPaymentResponse.getContentAsString()).isEqualTo(objectToJson(payment));

        // TODO: Validate that SMS was sent

    }

    private String objectToJson(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            fail(String.format("Failed to convert object [%s] to json", object.toString()));
            return null;
        }
    }
}
