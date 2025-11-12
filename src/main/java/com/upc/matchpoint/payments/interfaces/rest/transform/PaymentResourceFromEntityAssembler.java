package com.upc.matchpoint.payments.interfaces.rest.transform;

import com.upc.matchpoint.payments.domain.model.aggregates.Payment;
import com.upc.matchpoint.payments.interfaces.rest.resources.PaymentResource;

public class PaymentResourceFromEntityAssembler {
    public static PaymentResource toResourceFromEntity(Payment entity) {
        var userSummary = new PaymentResource.UserSummaryResource(
                entity.getUser().getId(),
                entity.getUser().getName()
        );
        return new PaymentResource(
                entity.getId(),
                entity.getAmount(),
                entity.getPaymentDate(),
                entity.getStatus(),
                userSummary
        );
    }
}

