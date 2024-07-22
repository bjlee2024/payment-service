package com.medit.cloud.service.payments.domain

import java.math.BigDecimal
import java.util.*

data class PaymentResult(
    val paymentId: UUID,
    val status: PaymentStatus,
    val amount: BigDecimal,
    val currency: String,
    val customerId: UUID,
    val subscriptionId: UUID?,
    val gatewayTransactionId: String?,
    val errorMessage: String? = null
)
