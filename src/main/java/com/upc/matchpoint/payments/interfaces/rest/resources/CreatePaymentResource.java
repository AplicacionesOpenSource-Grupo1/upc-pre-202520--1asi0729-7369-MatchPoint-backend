package com.upc.matchpoint.payments.interfaces.rest.resources;

import java.math.BigDecimal;

public record CreatePaymentResource(BigDecimal amount, Long userId) {
}

