package com.medit.cloud.service.payments.infrastructure.persistence.repository

import com.medit.cloud.service.payments.domain.SubscriptionStatus
import com.medit.cloud.service.payments.infrastructure.persistence.entity.SubscriptionEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.util.*

@Repository
interface SubscriptionRepository : JpaRepository<SubscriptionEntity, UUID> {
    fun findByCustomerId(customerId: UUID): List<SubscriptionEntity>
    fun findByStatus(status: SubscriptionStatus): List<SubscriptionEntity>
    fun findByEndDateBefore(date: LocalDate): List<SubscriptionEntity>
    fun findByCustomerIdAndStatus(customerId: UUID, status: SubscriptionStatus): List<SubscriptionEntity>
}