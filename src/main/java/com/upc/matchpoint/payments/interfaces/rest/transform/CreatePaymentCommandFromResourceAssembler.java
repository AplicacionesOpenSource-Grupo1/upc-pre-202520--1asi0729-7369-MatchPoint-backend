package com.upc.matchpoint.payments.interfaces.rest.transform;

import com.upc.matchpoint.payments.domain.model.commands.CreatePaymentCommand;
import com.upc.matchpoint.payments.interfaces.rest.resources.CreatePaymentResource;

public class CreatePaymentCommandFromResourceAssembler {
    public static CreatePaymentCommand toCommandFromResource(CreatePaymentResource resource) {
        return new CreatePaymentCommand(
                resource.amount(),
                resource.userId()
        );
    }
}

