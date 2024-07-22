package com.medit.cloud.service.payments.domain

import java.util.*

data class PaymentMethod(
    val id: UUID,
    val type: String,
    val token: String
)