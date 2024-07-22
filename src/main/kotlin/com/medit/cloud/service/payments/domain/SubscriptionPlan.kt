package com.medit.cloud.service.payments.domain

import java.math.BigDecimal
import java.util.*

data class SubscriptionPlan(
    val id: UUID,
    val name: String,
    val amount: BigDecimal,
    val currency: String,
    val billingCycle: BillingCycle
)