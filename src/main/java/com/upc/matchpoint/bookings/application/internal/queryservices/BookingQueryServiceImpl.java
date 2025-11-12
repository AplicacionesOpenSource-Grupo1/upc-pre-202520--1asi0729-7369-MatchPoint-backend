package com.upc.matchpoint.bookings.application.internal.queryservices;

import com.upc.matchpoint.bookings.domain.model.aggregates.Booking;
import com.upc.matchpoint.bookings.domain.model.queries.GetAllBookingsQuery;
import com.upc.matchpoint.bookings.domain.model.queries.GetBookingByIdQuery;
import com.upc.matchpoint.bookings.domain.services.BookingQueryService;
import com.upc.matchpoint.bookings.infrastructure.persistence.jpa.repositories.BookingRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class BookingQueryServiceImpl implements BookingQueryService {
    private final BookingRepository bookingRepository;

    public BookingQueryServiceImpl(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Override
    public List<Booking> handle(GetAllBookingsQuery query) {
        return bookingRepository.findAll();
    }

    @Override
    public Optional<Booking> handle(GetBookingByIdQuery query) {
        return bookingRepository.findById(query.bookingId());
    }
}

