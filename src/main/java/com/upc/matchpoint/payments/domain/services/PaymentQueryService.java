package com.upc.matchpoint.payments.domain.services;

import com.upc.matchpoint.payments.domain.model.aggregates.Payment;
import com.upc.matchpoint.payments.domain.model.queries.GetPaymentByIdQuery;
import java.util.Optional;

public interface PaymentQueryService {
    Optional<Payment> handle(GetPaymentByIdQuery query);
}

