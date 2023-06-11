package com.hrv.mart.backendorder.model

import com.hrv.mart.orderlibrary.model.Status

data class OrderQuery (
    val status: List<Status> = listOf(
        Status.SHIPPED,
        Status.PROCESS,
        Status.PLACED,
        Status.CANCELLED
    ),
    val startingDate: OrderDate = OrderDate.getMinDate(),
    val endingDate: OrderDate = OrderDate.getMaxDate()
)
