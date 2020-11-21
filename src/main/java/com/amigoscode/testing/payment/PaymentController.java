package com.amigoscode.testing.payment;

import com.amigoscode.testing.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("api/payments")
public class PaymentController {
    private PaymentService paymentService;
    private PaymentRepository paymentRepository;

    @Autowired
    public PaymentController(PaymentService paymentService, PaymentRepository paymentRepository) {
        this.paymentService = paymentService;
        this.paymentRepository = paymentRepository;
    }

    @PostMapping
    private ResponseEntity<Long> makePayment(@RequestBody @Valid PaymentRequest paymentRequest) {

        return paymentService.makePayment(paymentRequest);
    }

    @GetMapping("/{paymentId}")
    private Payment findPayment(@PathVariable @NotNull Long paymentId) {
        return paymentRepository.findById(paymentId).orElseThrow(
                () -> new ResourceNotFoundException(String.format("Payment with ID [%d] was not found", paymentId)));
    }
}
