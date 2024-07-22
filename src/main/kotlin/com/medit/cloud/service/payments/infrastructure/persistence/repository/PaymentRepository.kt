package com.medit.cloud.service.payments.infrastructure.persistence.repository

import com.medit.cloud.service.payments.domain.PaymentStatus
import com.medit.cloud.service.payments.infrastructure.persistence.entity.PaymentEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface PaymentRepository : JpaRepository<PaymentEntity, UUID> {
    fun findByCustomerId(customerId: UUID): List<PaymentEntity>
    fun findBySubscriptionId(subscriptionId: UUID): List<PaymentEntity>
    fun findByStatus(status: PaymentStatus): List<PaymentEntity>
    fun findByCustomerIdAndStatus(customerId: UUID, status: PaymentStatus): List<PaymentEntity>
}