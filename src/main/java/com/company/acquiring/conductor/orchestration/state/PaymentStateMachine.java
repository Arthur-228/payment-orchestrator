package com.company.acquiring.conductor.orchestration.state;

import com.company.acquiring.conductor.domain.model.PaymentStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Set;

@Slf4j
@Component
public class PaymentStateMachine {

    private static final Set<StatusTransition> ALLOWED_TRANSITIONS = Set.of(
            new StatusTransition(PaymentStatus.PENDING, PaymentStatus.ROUTING),
            new StatusTransition(PaymentStatus.ROUTING, PaymentStatus.THREE_DS_REQUIRED),
            new StatusTransition(PaymentStatus.ROUTING, PaymentStatus.PROCESSING),
            new StatusTransition(PaymentStatus.THREE_DS_REQUIRED, PaymentStatus.PROCESSING),
            new StatusTransition(PaymentStatus.PROCESSING, PaymentStatus.AUTHORIZED),
            new StatusTransition(PaymentStatus.AUTHORIZED, PaymentStatus.CAPTURED),
            new StatusTransition(PaymentStatus.PROCESSING, PaymentStatus.FAILED),
            new StatusTransition(PaymentStatus.AUTHORIZED, PaymentStatus.FAILED),
            new StatusTransition(PaymentStatus.CAPTURED, PaymentStatus.REFUNDED),
            new StatusTransition(PaymentStatus.PENDING, PaymentStatus.CANCELED)
    );

    public boolean canTransition(PaymentStatus from, PaymentStatus to) {
        boolean allowed = ALLOWED_TRANSITIONS.contains(new StatusTransition(from, to));

        if (!allowed) {
            log.warn("Invalid state transition attempted: {} -> {}", from, to);
        } else {
            log.debug("State transition allowed: {} -> {}", from, to);
        }

        return allowed;
    }

    private record StatusTransition(PaymentStatus from, PaymentStatus to) {
    }
}