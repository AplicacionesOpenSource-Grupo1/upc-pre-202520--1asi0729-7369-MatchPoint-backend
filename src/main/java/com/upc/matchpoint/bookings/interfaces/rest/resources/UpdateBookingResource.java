package com.upc.matchpoint.bookings.interfaces.rest.resources;

import java.time.LocalDateTime;

public record UpdateBookingResource(LocalDateTime startTime, LocalDateTime endTime) {
}

