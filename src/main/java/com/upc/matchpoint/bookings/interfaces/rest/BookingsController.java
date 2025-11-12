package com.upc.matchpoint.bookings.interfaces.rest;

import com.upc.matchpoint.bookings.domain.model.commands.DeleteBookingCommand;
import com.upc.matchpoint.bookings.domain.model.queries.GetAllBookingsQuery;
import com.upc.matchpoint.bookings.domain.model.queries.GetBookingByIdQuery;
import com.upc.matchpoint.bookings.domain.services.BookingCommandService;
import com.upc.matchpoint.bookings.domain.services.BookingQueryService;
import com.upc.matchpoint.bookings.interfaces.rest.resources.BookingResource;
import com.upc.matchpoint.bookings.interfaces.rest.resources.CreateBookingResource;
import com.upc.matchpoint.bookings.interfaces.rest.resources.UpdateBookingResource;
import com.upc.matchpoint.bookings.interfaces.rest.transform.BookingResourceFromEntityAssembler;
import com.upc.matchpoint.bookings.interfaces.rest.transform.CreateBookingCommandFromResourceAssembler;
import com.upc.matchpoint.bookings.interfaces.rest.transform.UpdateBookingCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/bookings", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Bookings", description = "Booking Management Endpoints")
public class BookingsController {
    private final BookingCommandService bookingCommandService;
    private final BookingQueryService bookingQueryService;

    public BookingsController(BookingCommandService bookingCommandService, BookingQueryService bookingQueryService) {
        this.bookingCommandService = bookingCommandService;
        this.bookingQueryService = bookingQueryService;
    }

    @PostMapping
    public ResponseEntity<BookingResource> createBooking(@RequestBody CreateBookingResource resource) {
        var command = CreateBookingCommandFromResourceAssembler.toCommandFromResource(resource);
        var booking = bookingCommandService.handle(command);
        return booking.map(b -> new ResponseEntity<>(BookingResourceFromEntityAssembler.toResourceFromEntity(b), HttpStatus.CREATED))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping
    public ResponseEntity<List<BookingResource>> getAllBookings() {
        var query = new GetAllBookingsQuery();
        var bookings = bookingQueryService.handle(query);
        var bookingResources = bookings.stream()
                .map(BookingResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(bookingResources);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingResource> getBookingById(@PathVariable Long id) {
        var query = new GetBookingByIdQuery(id);
        var booking = bookingQueryService.handle(query);
        return booking.map(b -> ResponseEntity.ok(BookingResourceFromEntityAssembler.toResourceFromEntity(b)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookingResource> updateBooking(@PathVariable Long id, @RequestBody UpdateBookingResource resource) {
        var command = UpdateBookingCommandFromResourceAssembler.toCommandFromResource(id, resource);
        var updatedBooking = bookingCommandService.handle(command);
        return updatedBooking.map(b -> ResponseEntity.ok(BookingResourceFromEntityAssembler.toResourceFromEntity(b)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBooking(@PathVariable Long id) {
        var command = new DeleteBookingCommand(id);
        bookingCommandService.handle(command);
        return ResponseEntity.ok("Booking deleted successfully.");
    }
}
