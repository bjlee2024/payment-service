package com.medit.cloud.service.payments.application.service

import com.medit.cloud.service.payments.application.port.`in`.PaymentUseCase
import com.medit.cloud.service.payments.application.port.out.PaymentGatewayPort
import com.medit.cloud.service.payments.application.port.out.PaymentPersistencePort
import com.medit.cloud.service.payments.domain.Payment
import com.medit.cloud.service.payments.domain.PaymentStatus
import org.springframework.stereotype.Service
import java.util.*


@Service
class PaymentService(
    private val paymentGatewayPort: PaymentGatewayPort,
    private val paymentPersistencePort: PaymentPersistencePort
) : PaymentUseCase {
    // Implementation of PaymentUseCase methods
    override fun processPayment(payment: Payment): Payment {
        TODO("Not yet implemented")
    }

    override fun authorizePayment(payment: Payment): Payment {
        TODO("Not yet implemented")
    }

    override fun capturePayment(paymentId: UUID): Payment {
        TODO("Not yet implemented")
    }

    override fun refundPayment(paymentId: UUID): Payment {
        TODO("Not yet implemented")
    }

    override fun getPaymentStatus(paymentId: UUID): PaymentStatus {
        TODO("Not yet implemented")
    }
}