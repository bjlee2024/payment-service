package com.medit.cloud.service.payments.domain

import java.time.LocalDate
import java.util.*

data class Subscription(
    val id: UUID,
    val customerId: UUID,
    val plan: SubscriptionPlan,
    val status: SubscriptionStatus,
    val startDate: LocalDate,
    val endDate: LocalDate? = null
)