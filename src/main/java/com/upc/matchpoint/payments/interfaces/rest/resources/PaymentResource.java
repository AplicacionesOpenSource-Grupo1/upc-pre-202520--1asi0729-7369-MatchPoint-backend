package com.upc.matchpoint.payments.interfaces.rest.resources;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentResource(Long id, BigDecimal amount, LocalDateTime paymentDate, String status, UserSummaryResource user) {
    public record UserSummaryResource(Long id, String name) {}
}

