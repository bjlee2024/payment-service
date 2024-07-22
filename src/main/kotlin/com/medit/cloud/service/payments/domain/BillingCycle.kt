package com.medit.cloud.service.payments.domain

import java.time.LocalDate
import java.time.Period

enum class BillingCycle(val period: Period, val description: String) {
    DAILY(Period.ofDays(1), "매일"),
    WEEKLY(Period.ofWeeks(1), "매주"),
    BIWEEKLY(Period.ofWeeks(2), "2주마다"),
    MONTHLY(Period.ofMonths(1), "매월"),
    QUARTERLY(Period.ofMonths(3), "3개월마다"),
    BIANNUALLY(Period.ofMonths(6), "6개월마다"),
    ANNUALLY(Period.ofYears(1), "매년");

    companion object {
        fun fromPeriod(period: Period): BillingCycle {
            return values().find { it.period == period }
                ?: throw IllegalArgumentException("No BillingCycle found for period: $period")
        }
    }

    fun getNextBillingDate(startDate: LocalDate): LocalDate {
        return startDate.plus(this.period)
    }
}