package com.company.acquiring.conductor.idempotency;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IdempotencyRequest {

    @NotBlank
    private String idempotencyKey;

    @NotBlank
    private String merchantId;

    @NotNull
    private BigDecimal amount;

    @NotBlank
    private String currency;
}