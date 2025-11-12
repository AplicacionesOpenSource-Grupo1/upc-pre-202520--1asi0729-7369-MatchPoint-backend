package com.upc.matchpoint.payments.domain.model.commands;

import java.math.BigDecimal;

public record CreatePaymentCommand(BigDecimal amount, Long userId) {
}

