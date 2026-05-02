package com.company.acquiring.conductor.orchestration;

import com.company.acquiring.conductor.domain.service.PaymentStatusService;
import com.company.acquiring.conductor.idempotency.IdempotencyRequest;
import com.company.acquiring.conductor.idempotency.IdempotencyResponse;
import com.company.acquiring.conductor.idempotency.IdempotencyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentOrchestrator {

    private final IdempotencyService idempotencyService;
    private final PaymentStatusService paymentStatusService;

    /**
     * Основная точка входа для обработки платежа
     */
    @Transactional
    public IdempotencyResponse initiatePayment(IdempotencyRequest request) {
        log.info("Starting payment orchestration for merchantId={}, idempotencyKey={}",
                request.getMerchantId(), request.getIdempotencyKey());

        IdempotencyResponse response = idempotencyService.processIdempotentRequest(request);

        if (!response.isNew()) {
            log.info("Idempotent request detected. Returning existing payment: {}", response.getPaymentId());
            return response;
        }

        // Здесь нужно будет добавить:
        // - Routing
        // - Anti-fraud
        // - 3DS проверку
        // - Вызов адаптеров через Kafka / синхронно

        log.info("New payment created and ready for further processing: {}", response.getPaymentId());

        return response;
    }
}