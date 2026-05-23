package com.banking.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

@Data
public class LoanRequest {

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private BigDecimal amount;

    @NotBlank(message = "Purpose is required")
    private String purpose;

    @NotNull(message = "Term months is required")
    @Positive(message = "Term months must be positive")
    private Integer termMonths;
}
