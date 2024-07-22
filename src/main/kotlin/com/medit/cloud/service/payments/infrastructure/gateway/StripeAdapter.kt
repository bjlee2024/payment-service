package com.medit.cloud.service.payments.infrastructure.gateway

import com.medit.cloud.service.payments.application.port.out.PaymentGatewayPort
import com.medit.cloud.service.payments.domain.Payment
import com.medit.cloud.service.payments.domain.PaymentStatus
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.math.BigDecimal

import com.stripe.Stripe
import com.stripe.exception.StripeException
import com.stripe.model.Charge
import com.stripe.model.Refund
import com.stripe.param.ChargeCreateParams
import com.stripe.param.RefundCreateParams

@Component
class StripeAdapter(@Value("\${stripe.api.key}") private val apiKey: String) : PaymentGatewayPort {

    init {
        Stripe.apiKey = apiKey
    }

    override fun processPayment(payment: Payment): Payment {
        val params = ChargeCreateParams.builder()
            .setAmount(payment.amount.multiply(BigDecimal(100)).toLong())
            .setCurrency(payment.currency.toLowerCase())
            .setSource(payment.paymentMethod.token)
            .setDescription("Payment for order ${payment.id}")
            .build()

        return try {
            val charge = Charge.create(params)
            payment.copy(
                status = PaymentStatus.COMPLETED,
                gatewayTransactionId = charge.id
            )
        } catch (e: StripeException) {
            // 로깅 추가
            payment.copy(status = PaymentStatus.FAILED)
        }
    }

    override fun authorizePayment(payment: Payment): Payment {
        val params = ChargeCreateParams.builder()
            .setAmount(payment.amount.multiply(BigDecimal(100)).toLong())
            .setCurrency(payment.currency.toLowerCase())
            .setSource(payment.paymentMethod.token)
            .setDescription("Authorization for order ${payment.id}")
            .setCapture(false)
            .build()

        return try {
            val charge = Charge.create(params)
            payment.copy(
                status = PaymentStatus.AUTHORIZED,
                gatewayTransactionId = charge.id
            )
        } catch (e: StripeException) {
            // 로깅 추가
            payment.copy(status = PaymentStatus.FAILED)
        }
    }

    override fun capturePayment(payment: Payment): Payment {
        return try {
            val charge = Charge.retrieve(payment.gatewayTransactionId)
            charge.capture()
            payment.copy(status = PaymentStatus.COMPLETED)
        } catch (e: StripeException) {
            // 로깅 추가
            payment.copy(status = PaymentStatus.FAILED)
        }
    }

    override fun refundPayment(payment: Payment): Payment {
        val params = RefundCreateParams.builder()
            .setCharge(payment.gatewayTransactionId)
            .build()

        return try {
            Refund.create(params)
            payment.copy(status = PaymentStatus.REFUNDED)
        } catch (e: StripeException) {
            // 로깅 추가
            payment.copy(status = PaymentStatus.FAILED)
        }
    }

    override fun getPaymentStatus(gatewayTransactionId: String): PaymentStatus {
        return try {
            val charge = Charge.retrieve(gatewayTransactionId)
            when {
                charge.refunded -> PaymentStatus.REFUNDED
                charge.captured -> PaymentStatus.COMPLETED
                charge.amount == charge.amountCaptured -> PaymentStatus.COMPLETED
                charge.amount > charge.amountCaptured -> PaymentStatus.AUTHORIZED
                else -> PaymentStatus.FAILED
            }
        } catch (e: StripeException) {
            // 로깅 추가
            PaymentStatus.FAILED
        }
    }
}