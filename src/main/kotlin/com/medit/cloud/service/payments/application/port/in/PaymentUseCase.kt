package com.medit.cloud.service.payments.application.port.`in`

import com.medit.cloud.service.payments.domain.Payment
import com.medit.cloud.service.payments.domain.PaymentStatus
import java.util.*

interface PaymentUseCase {
    fun processPayment(payment: Payment): Payment
    fun authorizePayment(payment: Payment): Payment
    fun capturePayment(paymentId: UUID): Payment
    fun refundPayment(paymentId: UUID): Payment
    fun getPaymentStatus(paymentId: UUID): PaymentStatus
}