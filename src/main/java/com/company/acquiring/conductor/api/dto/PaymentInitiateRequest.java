package com.company.acquiring.conductor.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentInitiateRequest {

    @NotBlank
    private String idempotencyKey;

    @NotBlank
    private String merchantId;

    @NotNull
    @Positive
    private BigDecimal amount;

    @NotBlank
    private String currency;
}