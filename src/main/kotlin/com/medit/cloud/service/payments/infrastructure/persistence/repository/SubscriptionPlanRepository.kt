package com.medit.cloud.service.payments.infrastructure.persistence.repository

import com.medit.cloud.service.payments.infrastructure.persistence.entity.SubscriptionPlanEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface SubscriptionPlanRepository : JpaRepository<SubscriptionPlanEntity, UUID> {
    fun findByName(name: String): SubscriptionPlanEntity?
}