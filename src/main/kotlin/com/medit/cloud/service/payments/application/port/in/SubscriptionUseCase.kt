package com.medit.cloud.service.payments.application.port.`in`

import com.medit.cloud.service.payments.domain.Payment
import com.medit.cloud.service.payments.domain.Subscription
import java.util.*

interface SubscriptionUseCase {
    fun createSubscription(customerId: UUID, planId: UUID): Subscription
    fun cancelSubscription(subscriptionId: UUID): Subscription
    fun getSubscription(subscriptionId: UUID): Subscription
    fun processRecurringPayment(subscriptionId: UUID): Payment
}