package com.company.acquiring.conductor.api.controller;

import com.company.acquiring.conductor.api.dto.PaymentInitiateRequest;
import com.company.acquiring.conductor.api.dto.PaymentResponse;
import com.company.acquiring.conductor.idempotency.IdempotencyRequest;
import com.company.acquiring.conductor.idempotency.IdempotencyResponse;
import com.company.acquiring.conductor.orchestration.PaymentOrchestrator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentOrchestrator paymentOrchestrator;

    @PostMapping("/initiate")
    public ResponseEntity<PaymentResponse> initiatePayment(
            @Valid @RequestBody PaymentInitiateRequest request) {

        log.info("Received payment initiation request: idempotencyKey={}, merchantId={}",
                request.getIdempotencyKey(), request.getMerchantId());

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

        log.info("Payment processed successfully. paymentId={}, isNew={}",
                response.getPaymentId(), response.isNew());

        return ResponseEntity.ok(paymentResponse);
    }
}