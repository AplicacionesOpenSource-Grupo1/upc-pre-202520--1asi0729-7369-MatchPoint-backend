package com.upc.matchpoint.bookings.interfaces.rest.transform;

import com.upc.matchpoint.bookings.domain.model.commands.CreateBookingCommand;
import com.upc.matchpoint.bookings.interfaces.rest.resources.CreateBookingResource;

public class CreateBookingCommandFromResourceAssembler {
    public static CreateBookingCommand toCommandFromResource(CreateBookingResource resource) {
        return new CreateBookingCommand(
                resource.startTime(),
                resource.endTime(),
                resource.userId(),
                resource.courtId()
        );
    }
}

