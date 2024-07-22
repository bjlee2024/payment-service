package com.medit.cloud.service.payments.infrastructure.persistence.entity

import com.medit.cloud.service.payments.domain.PaymentStatus
import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "payments")
data class PaymentEntity(
    @Id
    @Column(name = "id")
    val id: UUID = UUID.randomUUID(),

    @Column(name = "amount", nullable = false)
    val amount: BigDecimal,

    @Column(name = "currency", nullable = false)
    val currency: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    var status: PaymentStatus,

    @Column(name = "customer_id", nullable = false)
    val customerId: UUID,

    @Column(name = "subscription_id")
    val subscriptionId: UUID?,

    @Column(name = "payment_method_id", nullable = false)
    val paymentMethodId: UUID,

    @Column(name = "gateway_transaction_id")
    val gatewayTransactionId: String?,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()
)
