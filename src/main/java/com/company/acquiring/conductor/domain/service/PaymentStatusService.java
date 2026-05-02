package com.company.acquiring.conductor.domain.service;

import com.company.acquiring.conductor.domain.model.Payment;
import com.company.acquiring.conductor.domain.model.PaymentStatus;
import com.company.acquiring.conductor.domain.repository.PaymentRepository;
import com.company.acquiring.conductor.orchestration.state.PaymentStateMachine;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentStatusService {

    private final PaymentRepository paymentRepository;
    private final PaymentStateMachine stateMachine;

    @Transactional
    public Payment changeStatus(UUID paymentId, PaymentStatus newStatus, String failureReason) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found: " + paymentId));

        if (!stateMachine.canTransition(payment.getStatus(), newStatus)) {
            throw new IllegalStateException(
                    String.format("Invalid state transition from %s to %s for payment %s",
                            payment.getStatus(), newStatus, paymentId));
        }

        PaymentStatus oldStatus = payment.getStatus();
        payment.setStatus(newStatus);

        if (failureReason != null && !failureReason.isBlank()) {
            payment.setFailureReason(failureReason);
        }

        Payment saved = paymentRepository.save(payment);

        log.info("Payment status changed: {} -> {} (paymentId={})", oldStatus, newStatus, paymentId);

        return saved;
    }
}