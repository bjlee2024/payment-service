package com.medit.cloud.service.payments.application.port.out

import com.medit.cloud.service.payments.domain.Payment
import java.util.*

interface PaymentPersistencePort {
    fun savePayment(payment: Payment): Payment
    fun getPayment(paymentId: UUID): Payment?
    fun updatePayment(payment: Payment): Payment
    fun getPaymentsByCustomerId(customerId: UUID): List<Payment>
}