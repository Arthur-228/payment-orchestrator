package com.company.acquiring.conductor.domain.service;

import com.company.acquiring.conductor.domain.model.Payment;
import com.company.acquiring.conductor.domain.model.PaymentStatus;
import com.company.acquiring.conductor.domain.repository.PaymentRepository;
import com.company.acquiring.conductor.orchestration.state.PaymentStateMachine;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

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
                    String.format("Invalid state transition: %s -> %s for payment %s",
                            payment.getStatus(), newStatus, paymentId));
        }

        payment.setStatus(newStatus);
        if (failureReason != null) {
            payment.setFailureReason(failureReason);
        }

        return paymentRepository.save(payment);
    }
}