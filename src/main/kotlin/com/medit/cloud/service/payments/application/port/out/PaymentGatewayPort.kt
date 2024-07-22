package com.medit.cloud.service.payments.application.port.out

import com.medit.cloud.service.payments.domain.Payment
import com.medit.cloud.service.payments.domain.PaymentStatus

interface PaymentGatewayPort {
    fun processPayment(payment: Payment): Payment
    fun authorizePayment(payment: Payment): Payment
    fun capturePayment(payment: Payment): Payment
    fun refundPayment(payment: Payment): Payment
    fun getPaymentStatus(gatewayTransactionId: String): PaymentStatus
}