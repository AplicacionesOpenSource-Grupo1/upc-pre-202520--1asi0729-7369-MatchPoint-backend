package com.upc.matchpoint.bookings.domain.services;

import com.upc.matchpoint.bookings.domain.model.aggregates.Booking;
import com.upc.matchpoint.bookings.domain.model.commands.CreateBookingCommand;
import com.upc.matchpoint.bookings.domain.model.commands.DeleteBookingCommand;
import com.upc.matchpoint.bookings.domain.model.commands.UpdateBookingCommand;
import java.util.Optional;

public interface BookingCommandService {
    Optional<Booking> handle(CreateBookingCommand command);
    Optional<Booking> handle(UpdateBookingCommand command);
    void handle(DeleteBookingCommand command);
}

