package com.upc.matchpoint.bookings.domain.services;

import com.upc.matchpoint.bookings.domain.model.aggregates.Booking;
import com.upc.matchpoint.bookings.domain.model.queries.GetAllBookingsQuery;
import com.upc.matchpoint.bookings.domain.model.queries.GetBookingByIdQuery;
import java.util.List;
import java.util.Optional;

public interface BookingQueryService {
    List<Booking> handle(GetAllBookingsQuery query);
    Optional<Booking> handle(GetBookingByIdQuery query);
}

