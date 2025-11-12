package com.upc.matchpoint.payments.domain.model.aggregates;

import com.upc.matchpoint.payments.domain.model.valueobjects.PaymentStatus;
import com.upc.matchpoint.users.domain.model.aggregates.UserProfile;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserProfile user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus paymentStatus;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public Payment(BigDecimal amount, UserProfile user) {
        this.amount = amount;
        this.user = user;
        this.paymentStatus = PaymentStatus.PENDING; // Status por defecto
    }

    // Método para obtener la fecha de pago (usa createdAt)
    public LocalDateTime getPaymentDate() {
        return this.createdAt;
    }

    // Método para obtener el status como String (requerido por el ResourceAssembler)
    public String getStatus() {
        return this.paymentStatus.getStatus();
    }

    // Método para actualizar el status del pago
    public void updateStatus(PaymentStatus newStatus) {
        this.paymentStatus = newStatus;
    }
}