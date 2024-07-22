package com.medit.cloud.service.payments.config

import com.medit.cloud.service.payments.application.port.out.PaymentGatewayPort
import com.medit.cloud.service.payments.infrastructure.gateway.PayPalAdapter
import com.medit.cloud.service.payments.infrastructure.gateway.PaymentGatewayAdapter
import com.medit.cloud.service.payments.infrastructure.gateway.StripeAdapter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class PaymentConfig {

    @Bean
    fun paymentGatewayPort(
        payPalAdapter: PayPalAdapter,
        stripeAdapter: StripeAdapter
    ): PaymentGatewayPort {
        return PaymentGatewayAdapter(payPalAdapter, stripeAdapter)
    }

    // Other beans and configurations...
}
