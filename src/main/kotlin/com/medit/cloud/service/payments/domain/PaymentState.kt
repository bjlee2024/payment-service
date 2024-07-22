package com.medit.cloud.service.payments.domain

enum class PaymentStatus {
    PENDING, AUTHORIZED, CAPTURED, FAILED, REFUNDED, COMPLETED
}