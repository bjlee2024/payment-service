package com.medit.cloud.service.payments.application.service

import com.medit.cloud.service.payments.application.port.`in`.SubscriptionUseCase
import com.medit.cloud.service.payments.application.port.out.PaymentGatewayPort
import com.medit.cloud.service.payments.application.port.out.SubscriptionPersistencePort
import com.medit.cloud.service.payments.domain.*
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.util.*

@Service
class SubscriptionService(
    private val subscriptionPersistencePort: SubscriptionPersistencePort,
    private val paymentGatewayPort: PaymentGatewayPort,
    private val kafkaTemplate: KafkaTemplate<String, Any>
) : SubscriptionUseCase {

    override fun createSubscription(customerId: UUID, planId: UUID): Subscription {
        val plan = getPlan(planId) // Assume this method exists to fetch the plan
        val subscription = Subscription(
            id = UUID.randomUUID(),
            customerId = customerId,
            plan = plan,
            status = SubscriptionStatus.ACTIVE,
            startDate = LocalDate.now(),
            endDate = null
        )
        return subscriptionPersistencePort.saveSubscription(subscription)
    }

    override fun cancelSubscription(subscriptionId: UUID): Subscription {
        val subscription = getSubscription(subscriptionId)
        val cancelledSubscription = subscription.copy(
            status = SubscriptionStatus.CANCELLED,
            endDate = LocalDate.now()
        )
        return subscriptionPersistencePort.updateSubscription(cancelledSubscription)
    }

    override fun getSubscription(subscriptionId: UUID): Subscription {
        return subscriptionPersistencePort.getSubscription(subscriptionId)
            ?: throw NoSuchElementException("Subscription not found")
    }

    override fun processRecurringPayment(subscriptionId: UUID): Payment {
        val subscription = getSubscription(subscriptionId)
        val payment = Payment(
            id = UUID.randomUUID(),
            amount = subscription.plan.amount,
            currency = subscription.plan.currency,
            status = PaymentStatus.PENDING,
            customerId = subscription.customerId,
            subscriptionId = subscription.id,
            paymentMethod = getCustomerPaymentMethod(subscription.customerId), // Assume this method exists
            gatewayTransactionId = null
        )

        val processedPayment = paymentGatewayPort.processPayment(payment)

        // Send payment result to Kafka
        val paymentResult = PaymentResult(
            paymentId = processedPayment.id,
            status = processedPayment.status,
            amount = processedPayment.amount,
            currency = processedPayment.currency,
            customerId = processedPayment.customerId,
            subscriptionId = processedPayment.subscriptionId,
            gatewayTransactionId = processedPayment.gatewayTransactionId)
        kafkaTemplate.send("payment-results", paymentResult)

        return processedPayment
    }

    fun updateSubscriptionAfterPayment(paymentResult: PaymentResult) {
        val subscriptionList = subscriptionPersistencePort.getSubscriptionsByCustomerId(paymentResult.customerId)
        subscriptionList.forEach { subscription ->
            val updatedSubscription = when (paymentResult.status) {
                PaymentStatus.COMPLETED -> extendSubscription(subscription)
                PaymentStatus.FAILED -> handleFailedPayment(subscription)
                else -> subscription // No changes for other statuses
            }

            subscriptionPersistencePort.updateSubscription(updatedSubscription)
        }
    }

    private fun extendSubscription(subscription: Subscription): Subscription {
        val newEndDate = (subscription.endDate ?: subscription.startDate).plus(subscription.plan.billingCycle.period)
        return subscription.copy(endDate = newEndDate)
    }

    private fun handleFailedPayment(subscription: Subscription): Subscription {
        // Implement logic for failed payments (e.g., set a grace period, mark for review, etc.)
        return subscription.copy(status = SubscriptionStatus.PAYMENT_FAILED)
    }

    // Assume these methods exist
    private fun getPlan(planId: UUID): SubscriptionPlan = TODO()
    private fun getCustomerPaymentMethod(customerId: UUID): PaymentMethod = TODO()
}