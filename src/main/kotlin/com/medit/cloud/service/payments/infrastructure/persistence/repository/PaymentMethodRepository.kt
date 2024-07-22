package com.medit.cloud.service.payments.infrastructure.persistence.repository

import com.medit.cloud.service.payments.infrastructure.persistence.entity.PaymentMethodEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface PaymentMethodRepository : JpaRepository<PaymentMethodEntity, UUID> {
    fun findByCustomerId(customerId: UUID): List<PaymentMethodEntity>
    fun findByCustomerIdAndIsDefault(customerId: UUID, isDefault: Boolean): PaymentMethodEntity?
}