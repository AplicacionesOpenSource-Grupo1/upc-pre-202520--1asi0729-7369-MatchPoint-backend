package com.upc.matchpoint.bookings.interfaces.rest.resources;

import java.time.LocalDateTime;

public record BookingResource(Long id, LocalDateTime startTime, LocalDateTime endTime, UserSummaryResource user, CourtSummaryResource court) {
    public record UserSummaryResource(Long id, String name) {}
    public record CourtSummaryResource(Long id, String name) {}
}

