package com.medit.cloud.service.payments.application.port.out

import com.medit.cloud.service.payments.domain.Subscription
import java.time.LocalDate
import java.util.*

interface SubscriptionPersistencePort {
    fun saveSubscription(subscription: Subscription): Subscription
    fun getSubscription(subscriptionId: UUID): Subscription?
    fun updateSubscription(subscription: Subscription): Subscription
    fun getSubscriptionsByCustomerId(customerId: UUID): List<Subscription>
    fun getActiveSubscriptions(): List<Subscription>
    fun getSubscriptionsEndingBefore(date: LocalDate): List<Subscription>
}