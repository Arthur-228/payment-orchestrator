package com.company.acquiring.conductor.api.controller;

import com.company.acquiring.conductor.api.dto.PaymentInitiateRequest;
import com.company.acquiring.conductor.api.dto.PaymentResponse;
import com.company.acquiring.conductor.idempotency.IdempotencyRequest;
import com.company.acquiring.conductor.idempotency.IdempotencyResponse;
import com.company.acquiring.conductor.orchestration.PaymentOrchestrator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentOrchestrator paymentOrchestrator;

    @PostMapping("/initiate")
    public ResponseEntity<PaymentResponse> initiatePayment(
            @Valid @RequestBody PaymentInitiateRequest request) {

        IdempotencyRequest idempotencyRequest = IdempotencyRequest.builder()
                .idempotencyKey(request.getIdempotencyKey())
                .merchantId(request.getMerchantId())
                .amount(request.getAmount())
                .currency(request.getCurrency())
                .build();

        IdempotencyResponse response = paymentOrchestrator.initiatePayment(idempotencyRequest);

        PaymentResponse paymentResponse = PaymentResponse.builder()
                .paymentId(response.getPaymentId())
                .status(response.getStatus())
                .isNew(response.isNew())
                .build();

        return ResponseEntity.ok(paymentResponse);
    }
}
