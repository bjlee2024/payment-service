package com.medit.cloud.service.payments.infrastructure.persistence.entity

import com.medit.cloud.service.payments.domain.BillingCycle
import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "subscription_plans")
data class SubscriptionPlanEntity(
    @Id
    @Column(name = "id")
    val id: UUID = UUID.randomUUID(),

    @Column(name = "name", nullable = false)
    val name: String,

    @Column(name = "amount", nullable = false)
    val amount: BigDecimal,

    @Column(name = "currency", nullable = false)
    val currency: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "billing_cycle", nullable = false)
    val billingCycle: BillingCycle,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()
)