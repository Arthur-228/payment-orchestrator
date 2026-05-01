package com.company.acquiring.conductor.idempotency;

import com.company.acquiring.conductor.domain.model.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IdempotencyResponse {

    private UUID paymentId;
    private PaymentStatus status;
    private boolean isNew;
    private String failureReason;
}