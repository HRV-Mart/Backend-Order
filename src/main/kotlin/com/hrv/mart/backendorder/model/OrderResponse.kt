package com.hrv.mart.backendorder.model

import com.hrv.mart.cartresponse.model.CartResponse
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document("Order")
data class OrderResponse (
    val userId: String,
    val products: List<CartResponse>,
    val status: Status = Status.PROCESS,
    val dateTimeOfOrder: LocalDateTime = LocalDateTime.now()
)