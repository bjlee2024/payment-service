package com.medit.cloud.service.payments.infrastructure.persistence.adapter

import com.medit.cloud.service.payments.application.port.out.SubscriptionPersistencePort
import com.medit.cloud.service.payments.domain.Subscription
import com.medit.cloud.service.payments.domain.SubscriptionPlan
import com.medit.cloud.service.payments.domain.SubscriptionStatus
import com.medit.cloud.service.payments.infrastructure.persistence.entity.SubscriptionEntity
import com.medit.cloud.service.payments.infrastructure.persistence.entity.SubscriptionPlanEntity
import com.medit.cloud.service.payments.infrastructure.persistence.repository.SubscriptionPlanRepository
import com.medit.cloud.service.payments.infrastructure.persistence.repository.SubscriptionRepository
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.util.*
import kotlin.NoSuchElementException

@Component
class SubscriptionPersistenceAdapter(
    private val subscriptionRepository: SubscriptionRepository,
    private val subscriptionPlanRepository: SubscriptionPlanRepository
) : SubscriptionPersistencePort {

    override fun saveSubscription(subscription: Subscription): Subscription {
        val subscriptionEntity = subscription.toEntity()
        val savedEntity = subscriptionRepository.save(subscriptionEntity)
        return savedEntity.toDomain()
    }

    override fun getSubscription(subscriptionId: UUID): Subscription? {
        return subscriptionRepository.findById(subscriptionId).orElse(null)?.toDomain()
    }

    override fun updateSubscription(subscription: Subscription): Subscription {
        val existingEntity = subscriptionRepository.findById(subscription.id)
            .orElseThrow { NoSuchElementException("Subscription not found with id: ${subscription.id}") }

        val updatedEntity = existingEntity.copy(
            status = subscription.status,
            endDate = subscription.endDate
        )

        return subscriptionRepository.save(updatedEntity).toDomain()
    }

    override fun getSubscriptionsByCustomerId(customerId: UUID): List<Subscription> {
        return subscriptionRepository.findByCustomerId(customerId).map { it.toDomain() }
    }

    override fun getActiveSubscriptions(): List<Subscription> {
        return subscriptionRepository.findByStatus(SubscriptionStatus.ACTIVE).map { it.toDomain() }
    }

    override fun getSubscriptionsEndingBefore(date: LocalDate): List<Subscription> {
        return subscriptionRepository.findByEndDateBefore(date).map { it.toDomain() }
    }

    private fun Subscription.toEntity(): SubscriptionEntity {
        return SubscriptionEntity(
            id = this.id,
            customerId = this.customerId,
            planId = this.plan.id,
            status = this.status,
            startDate = this.startDate,
            endDate = this.endDate
        )
    }

    private fun SubscriptionEntity.toDomain(): Subscription {
        val plan = subscriptionPlanRepository.findById(this.planId)
            .orElseThrow { NoSuchElementException("SubscriptionPlan not found with id: ${this.planId}") }

        return Subscription(
            id = this.id,
            customerId = this.customerId,
            plan = plan.toDomain(),
            status = this.status,
            startDate = this.startDate,
            endDate = this.endDate
        )
    }

    private fun SubscriptionPlanEntity.toDomain(): SubscriptionPlan {
        return SubscriptionPlan(
            id = this.id,
            name = this.name,
            amount = this.amount,
            currency = this.currency,
            billingCycle = this.billingCycle
        )
    }
}