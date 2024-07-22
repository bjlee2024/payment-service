package com.medit.cloud.service.payments.domain

import java.math.BigDecimal
import java.util.*

data class Payment(
    val id: UUID,
    val amount: BigDecimal,
    val currency: String,
    val status: PaymentStatus,
    val customerId: UUID,
    val subscriptionId: UUID? = null,
    val paymentMethod: PaymentMethod,
    val gatewayTransactionId: String?
)