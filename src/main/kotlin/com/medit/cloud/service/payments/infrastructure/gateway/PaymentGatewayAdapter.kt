package com.medit.cloud.service.payments.infrastructure.gateway

import com.medit.cloud.service.payments.application.port.out.PaymentGatewayPort
import com.medit.cloud.service.payments.domain.Payment
import com.medit.cloud.service.payments.domain.PaymentMethod
import com.medit.cloud.service.payments.domain.PaymentStatus
import org.springframework.stereotype.Component

@Component
class PaymentGatewayAdapter(
    private val payPalAdapter: PayPalAdapter,
    private val stripeAdapter: StripeAdapter
) : PaymentGatewayPort {

    override fun processPayment(payment: Payment): Payment {
        return getAdapter(payment.paymentMethod).processPayment(payment)
    }

    override fun authorizePayment(payment: Payment): Payment {
        return getAdapter(payment.paymentMethod).authorizePayment(payment)
    }

    override fun capturePayment(payment: Payment): Payment {
        return getAdapter(payment.paymentMethod).capturePayment(payment)
    }

    override fun refundPayment(payment: Payment): Payment {
        return getAdapter(payment.paymentMethod).refundPayment(payment)
    }

    override fun getPaymentStatus(gatewayTransactionId: String): PaymentStatus {
        // 이 메서드의 구현은 트랜잭션 ID의 형식에 따라 적절한 어댑터를 선택해야 합니다.
        // 여기서는 간단한 예시로 PayPal 어댑터를 사용합니다.
        return payPalAdapter.getPaymentStatus(gatewayTransactionId)
    }

    private fun getAdapter(paymentMethod: PaymentMethod): PaymentGatewayPort {
        return when (paymentMethod.type) {
            "PAYPAL" -> payPalAdapter
            "STRIPE" -> stripeAdapter
            else -> throw IllegalArgumentException("Unsupported payment method: ${paymentMethod.type}")
        }
    }
}