package com.upc.matchpoint.payments.interfaces.rest;

import com.upc.matchpoint.payments.domain.model.queries.GetPaymentByIdQuery;
import com.upc.matchpoint.payments.domain.services.PaymentCommandService;
import com.upc.matchpoint.payments.domain.services.PaymentQueryService;
import com.upc.matchpoint.payments.interfaces.rest.resources.CreatePaymentResource;
import com.upc.matchpoint.payments.interfaces.rest.resources.PaymentResource;
import com.upc.matchpoint.payments.interfaces.rest.transform.CreatePaymentCommandFromResourceAssembler;
import com.upc.matchpoint.payments.interfaces.rest.transform.PaymentResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/payments", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Payments", description = "Payment Management Endpoints")
public class PaymentsController {
    private final PaymentCommandService paymentCommandService;
    private final PaymentQueryService paymentQueryService;

    public PaymentsController(PaymentCommandService paymentCommandService, PaymentQueryService paymentQueryService) {
        this.paymentCommandService = paymentCommandService;
        this.paymentQueryService = paymentQueryService;
    }

    @PostMapping
    public ResponseEntity<PaymentResource> createPayment(@RequestBody CreatePaymentResource resource) {
        var command = CreatePaymentCommandFromResourceAssembler.toCommandFromResource(resource);
        var payment = paymentCommandService.handle(command);
        return payment.map(p -> new ResponseEntity<>(PaymentResourceFromEntityAssembler.toResourceFromEntity(p), HttpStatus.CREATED))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResource> getPaymentById(@PathVariable Long id) {
        var query = new GetPaymentByIdQuery(id);
        var payment = paymentQueryService.handle(query);
        return payment.map(p -> ResponseEntity.ok(PaymentResourceFromEntityAssembler.toResourceFromEntity(p)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
