package com.upc.matchpoint.payments.application.internal.commandservices;

import com.upc.matchpoint.payments.domain.model.aggregates.Payment;
import com.upc.matchpoint.payments.domain.model.commands.CreatePaymentCommand;
import com.upc.matchpoint.payments.domain.services.PaymentCommandService;
import com.upc.matchpoint.payments.infrastructure.persistence.jpa.repositories.PaymentRepository;
import com.upc.matchpoint.users.infrastructure.persistence.jpa.repositories.UserProfileRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class PaymentCommandServiceImpl implements PaymentCommandService {
    private final PaymentRepository paymentRepository;
    private final UserProfileRepository userProfileRepository;

    public PaymentCommandServiceImpl(PaymentRepository paymentRepository, UserProfileRepository userProfileRepository) {
        this.paymentRepository = paymentRepository;
        this.userProfileRepository = userProfileRepository;
    }

    @Override
    public Optional<Payment> handle(CreatePaymentCommand command) {
        var user = userProfileRepository.findById(command.userId()).orElseThrow(() -> new IllegalArgumentException("User with id " + command.userId() + " not found"));
        var payment = new Payment(command.amount(), user);
        var createdPayment = paymentRepository.save(payment);
        return Optional.of(createdPayment);
    }
}

