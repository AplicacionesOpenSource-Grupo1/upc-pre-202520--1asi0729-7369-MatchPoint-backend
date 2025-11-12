package com.upc.matchpoint.bookings.interfaces.rest.transform;

import com.upc.matchpoint.bookings.domain.model.aggregates.Booking;
import com.upc.matchpoint.bookings.interfaces.rest.resources.BookingResource;

public class BookingResourceFromEntityAssembler {
    public static BookingResource toResourceFromEntity(Booking entity) {
        var userSummary = new BookingResource.UserSummaryResource(
                entity.getUser().getId(),
                entity.getUser().getName()
        );
        var courtSummary = new BookingResource.CourtSummaryResource(
                entity.getCourt().getId(),
                entity.getCourt().getName()
        );
        return new BookingResource(
                entity.getId(),
                entity.getStartTime(),
                entity.getEndTime(),
                userSummary,
                courtSummary
        );
    }
}

