package com.company.acquiring.conductor.idempotency;

import com.company.acquiring.conductor.domain.model.Payment;
import com.company.acquiring.conductor.domain.model.PaymentStatus;
import com.company.acquiring.conductor.domain.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IdempotencyService {

    private final PaymentRepository paymentRepository;

    /**
     * Проверяет идемпотентность запроса.
     * Если платеж с таким idempotencyKey уже существует — возвращает его.
     * Если нет — создаёт новый в статусе PENDING.
     */
    @Transactional
    public IdempotencyResponse processIdempotentRequest(IdempotencyRequest request) {
        Optional<Payment> existingPayment = paymentRepository.findByPaymentId(request.getIdempotencyKey());

        if (existingPayment.isPresent()) {
            Payment payment = existingPayment.get();
            return IdempotencyResponse.builder()
                    .paymentId(payment.getId())
                    .status(payment.getStatus())
                    .isNew(false)
                    .build();
        }

        Payment newPayment = Payment.builder()
                .paymentId(request.getIdempotencyKey())
                .merchantId(request.getMerchantId())
                .amount(request.getAmount())
                .currency(request.getCurrency())
                .status(PaymentStatus.PENDING)
                .build();

        Payment savedPayment = paymentRepository.save(newPayment);

        return IdempotencyResponse.builder()
                .paymentId(savedPayment.getId())
                .status(savedPayment.getStatus())
                .isNew(true)
                .build();
    }
}