package com.hrv.mart.backendorder.model

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

data class OrderDate (
    val year: Int,
    val month: Int,
    val date: Int,
) {
    companion object {
        fun getMaxDate() =
            toOrderDate(LocalDate.MAX)
        fun getMinDate() =
            toOrderDate(LocalDate.MIN)
        fun getDateTimeFormat() =
            DateTimeFormatter.ofPattern("yyyy-MM-dd:HH:mm:ss", Locale.ROOT)
        private fun toOrderDate(date: LocalDate) =
            OrderDate(
                year = date.year,
                month = date.month.value,
                date = date.dayOfMonth
            )
    }
    fun parseToString(isStarting: Boolean): String{
        val time =
            if (isStarting) {
                "00:00:00"
            }
            else {
                "23:59:59"
            }
        return LocalDate.of(year, month, date).toString() + ":${time}"
    }
}