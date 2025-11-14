package com.upc.matchpoint.bookings.interfaces.rest.resources;

import java.time.LocalDateTime;

public record CreateBookingResource(LocalDateTime startTime, LocalDateTime endTime, Long userId, Long courtId) {
}

