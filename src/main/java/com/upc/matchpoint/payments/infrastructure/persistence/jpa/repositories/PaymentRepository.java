package com.upc.matchpoint.payments.infrastructure.persistence.jpa.repositories;

import com.upc.matchpoint.payments.domain.model.aggregates.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
}

