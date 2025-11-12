package com.upc.matchpoint.bookings.interfaces.rest.transform;

import com.upc.matchpoint.bookings.domain.model.commands.UpdateBookingCommand;
import com.upc.matchpoint.bookings.interfaces.rest.resources.UpdateBookingResource;

public class UpdateBookingCommandFromResourceAssembler {
    public static UpdateBookingCommand toCommandFromResource(Long bookingId, UpdateBookingResource resource) {
        return new UpdateBookingCommand(
                bookingId,
                resource.startTime(),
                resource.endTime()
        );
    }
}

