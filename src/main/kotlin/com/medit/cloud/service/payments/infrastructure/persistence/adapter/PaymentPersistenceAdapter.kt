package com.medit.cloud.service.payments.infrastructure.persistence.adapter

import com.medit.cloud.service.payments.application.port.out.PaymentPersistencePort
import com.medit.cloud.service.payments.domain.Payment
import com.medit.cloud.service.payments.domain.PaymentMethod
import com.medit.cloud.service.payments.infrastructure.persistence.entity.PaymentEntity
import com.medit.cloud.service.payments.infrastructure.persistence.repository.PaymentRepository
import org.springframework.stereotype.Component
import java.util.*
import kotlin.NoSuchElementException

@Component
class PaymentPersistenceAdapter(private val paymentRepository: PaymentRepository) : PaymentPersistencePort {

    override fun savePayment(payment: Payment): Payment {
        val paymentEntity = payment.toEntity()
        val savedEntity = paymentRepository.save(paymentEntity)
        return savedEntity.toDomain()
    }

    override fun getPayment(paymentId: UUID): Payment? {
        return paymentRepository.findById(paymentId).orElse(null)?.toDomain()
    }

    override fun updatePayment(payment: Payment): Payment {
        val existingEntity = paymentRepository.findById(payment.id)
            .orElseThrow { NoSuchElementException("Payment not found with id: ${payment.id}") }

        val updatedEntity = existingEntity.copy(
            amount = payment.amount,
            currency = payment.currency,
            status = payment.status,
            gatewayTransactionId = payment.gatewayTransactionId
        )

        return paymentRepository.save(updatedEntity).toDomain()
    }

    override fun getPaymentsByCustomerId(customerId: UUID): List<Payment> {
        return paymentRepository.findByCustomerId(customerId).map { it.toDomain() }
    }

    private fun Payment.toEntity(): PaymentEntity {
        return PaymentEntity(
            id = this.id,
            amount = this.amount,
            currency = this.currency,
            status = this.status,
            customerId = this.customerId,
            subscriptionId = this.subscriptionId,
            paymentMethodId = this.paymentMethod.id,
            gatewayTransactionId = this.gatewayTransactionId
        )
    }

    private fun PaymentEntity.toDomain(): Payment {
        return Payment(
            id = this.id,
            amount = this.amount,
            currency = this.currency,
            status = this.status,
            customerId = this.customerId,
            subscriptionId = this.subscriptionId,
            paymentMethod = PaymentMethod(this.paymentMethodId, "", ""), // 여기서는 간단히 처리, 실제로는 PaymentMethod 정보를 가져와야 함
            gatewayTransactionId = this.gatewayTransactionId
        )
    }
}